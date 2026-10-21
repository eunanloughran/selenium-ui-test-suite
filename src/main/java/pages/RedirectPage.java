package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class RedirectPage {
    private final WebDriver driver;

    public RedirectPage(WebDriver driver) {
        this.driver = driver;
    }

    /** Clicks the link with this text, or returns false if there isn't one. */
    public boolean clickRedirectLink(String linkText) {
        try {
            driver.findElement(By.linkText(linkText)).click();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
