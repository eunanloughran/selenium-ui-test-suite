package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptAlertsPage {
    private final WebDriver driver;
    private final By result = By.id("result");

    public JavaScriptAlertsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Clicks the button with this text, types into the alert if text is given, and accepts it.
     * Returns false if there is no such button or no alert appears.
     */
    public boolean clickAlertButton(String buttonText, String inputText) {
        try {
            driver.findElement(By.xpath("//button[normalize-space()='" + buttonText + "']")).click();
            Alert alert = driver.switchTo().alert();
            if (inputText != null && !inputText.isEmpty()) {
                alert.sendKeys(inputText);
            }
            alert.accept();
            return true;
        } catch (NoSuchElementException | NoAlertPresentException e) {
            return false;
        }
    }

    public WebElement getResultElement() {
        return driver.findElement(result);
    }

    public String getResultText() {
        return getResultElement().getText().trim();
    }
}
