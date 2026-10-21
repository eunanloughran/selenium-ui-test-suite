package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class FileUploadPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By fileInput = By.id("file-upload");
    private final By uploadButton = By.id("file-submit");
    private final By uploadedFiles = By.id("uploaded-files");

    public FileUploadPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Picks the file and submits the form. Throws if the file doesn't exist locally. */
    public void uploadFile(String filePath) {
        if (!new File(filePath).exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        driver.findElement(fileInput).sendKeys(filePath);
        driver.findElement(uploadButton).click();
    }

    // Waits for the result page, as larger files take a moment to upload.
    public String getUploadedFileName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(uploadedFiles)).getText();
    }
}
