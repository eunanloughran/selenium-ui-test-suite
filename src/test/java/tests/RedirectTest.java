package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.RedirectPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedirectTest extends TestBase {
    private RedirectPage redirectPage;

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/status_codes");
        redirectPage = new RedirectPage(driver);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/redirects.csv", numLinesToSkip = 1)
    void followsStatusCodeLink(String testId, String linkText, String expectedPath, boolean linkExists) {
        test = extent.createTest(testId);
        try {
            boolean clicked = redirectPage.clickRedirectLink(linkText);
            if (linkExists) {
                assertTrue(clicked, "Expected a link with text " + linkText);
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(expectedPath));
                test.pass("Redirected to " + driver.getCurrentUrl());
            } else {
                assertFalse(clicked, "There should be no link with text " + linkText);
                test.pass("No link for " + linkText + ", as expected");
            }
        } catch (AssertionError | RuntimeException e) {
            test.fail(e);
            throw e;
        }
    }
}
