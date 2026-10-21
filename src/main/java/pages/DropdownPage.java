package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class DropdownPage {
    private final WebDriver driver;
    private final By dropdown = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectOptionByValue(String value) {
        new Select(driver.findElement(dropdown)).selectByValue(value);
    }

    public String getSelectedOptionText() {
        return new Select(driver.findElement(dropdown)).getFirstSelectedOption().getText();
    }
}
