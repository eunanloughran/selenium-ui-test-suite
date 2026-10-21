package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.JavaScriptAlertsPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaScriptAlertsTest extends TestBase {
    private JavaScriptAlertsPage alertsPage;

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");
        alertsPage = new JavaScriptAlertsPage(driver);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/alerts.csv", numLinesToSkip = 1)
    void handlesAlert(String testId, String buttonText, String expectedResult, String alertInput, boolean buttonExists) {
        test = extent.createTest(testId);
        try {
            boolean clicked = alertsPage.clickAlertButton(buttonText, alertInput);
            if (buttonExists) {
                assertTrue(clicked, "Expected a button with text " + buttonText);
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                        ExpectedConditions.textToBePresentInElement(alertsPage.getResultElement(), expectedResult));
                assertEquals(expectedResult, alertsPage.getResultText());
                test.pass("Result: " + expectedResult);
            } else {
                assertFalse(clicked, "There should be no button with text " + buttonText);
                test.pass("No button for " + buttonText + ", as expected");
            }
        } catch (AssertionError | RuntimeException e) {
            test.fail(e);
            throw e;
        }
    }
}
