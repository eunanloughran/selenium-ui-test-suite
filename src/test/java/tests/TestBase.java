package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Starts a fresh Chrome for every test, and after each test saves a screenshot
 * and adds it to a shared ExtentReports HTML report.
 */
public abstract class TestBase {
    private static final Path SCREENSHOTS = Path.of("test-output", "screenshots");
    private static final Path REPORT = Path.of("test-output", "reports", "ExtentReport.html");

    protected static ExtentReports extent;
    protected WebDriver driver;
    protected ExtentTest test;

    // @BeforeAll runs once per test class, so only create the report the first time;
    // otherwise each class would start a new report and wipe out the previous classes' results.
    @BeforeAll
    static void setUpReport() throws IOException {
        if (extent != null) {
            return;
        }
        Files.createDirectories(SCREENSHOTS);
        Files.createDirectories(REPORT.getParent());
        extent = new ExtentReports();
        extent.attachReporter(new ExtentSparkReporter(REPORT.toString()));
    }

    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        // Run with -Dheadless=true to test without opening a browser window, e.g. on a CI server.
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @AfterEach
    void stopBrowser(TestInfo testInfo) {
        try {
            if (test != null) {
                captureScreenshot(testInfo.getDisplayName(), test.getStatus());
                extent.flush();
            }
        } finally {
            // driver is null if Chrome failed to start, and quit() would hide that real error.
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @AfterAll
    static void writeReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    private void captureScreenshot(String testName, Status status) {
        String fileName = String.format("%s_%s_%d.png", status.toString().toLowerCase(),
                testName.replaceAll("[^a-zA-Z0-9-_]", "_"), System.currentTimeMillis());
        Path target = SCREENSHOTS.resolve(fileName);
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            // The report lives in test-output/reports, so the image path is relative to that folder.
            test.addScreenCaptureFromPath(Path.of("..", "screenshots", fileName).toString());
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
