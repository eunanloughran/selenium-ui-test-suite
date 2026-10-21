package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import pages.DropdownPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DropdownTest extends TestBase {
    private DropdownPage dropdownPage;

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        dropdownPage = new DropdownPage(driver);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/dropdown.csv", numLinesToSkip = 1)
    void selectsOption(String testId, String optionValue, String expectedText) {
        test = extent.createTest(testId);
        try {
            dropdownPage.selectOptionByValue(optionValue);
            assertEquals(expectedText, dropdownPage.getSelectedOptionText());
            test.pass("Selected option: " + expectedText);
        } catch (AssertionError | RuntimeException e) {
            test.fail(e);
            throw e;
        }
    }
}
