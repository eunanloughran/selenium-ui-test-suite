package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends TestBase {
    private static final String SECURE_URL = "https://the-internet.herokuapp.com/secure";
    private LoginPage loginPage;

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/login");
        loginPage = new LoginPage(driver);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/login.csv", numLinesToSkip = 1)
    void logsIn(String testId, String username, String password, String expectedOutcome) {
        test = extent.createTest(testId);
        try {
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
            loginPage.clickLogin();

            switch (expectedOutcome) {
                case "Success" -> {
                    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlToBe(SECURE_URL));
                    assertTrue(loginPage.getFlashMessage().contains("You logged into a secure area!"));
                }
                case "InvalidUsername" -> assertFailedWith("Your username is invalid!");
                case "InvalidPassword" -> assertFailedWith("Your password is invalid!");
                default -> throw new IllegalArgumentException("Unknown expected outcome in login.csv: " + expectedOutcome);
            }
            test.pass(testId + ": " + expectedOutcome);
        } catch (AssertionError | RuntimeException e) {
            test.fail(e);
            throw e;
        }
    }

    private void assertFailedWith(String expectedError) {
        String message = loginPage.getFlashMessage();
        assertTrue(message.contains(expectedError), "Expected \"" + expectedError + "\" but got: " + message);
        assertNotEquals(SECURE_URL, driver.getCurrentUrl(), "Invalid login should not reach the secure area");
    }
}
