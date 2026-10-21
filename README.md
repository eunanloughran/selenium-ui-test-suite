# Selenium UI Test Suite

Automated browser tests for five features of [the-internet.herokuapp.com](https://the-internet.herokuapp.com), a practice site for test automation. Written in Java with Selenium WebDriver and JUnit 5, using the Page Object Model and data-driven tests from CSV files. Every run produces an HTML report with a screenshot of each test.

## What's tested

| Feature | Test class | Cases | What's checked |
| --- | --- | --- | --- |
| Login | `LoginTest` | 3 | Valid login reaches the secure area. A wrong username or password shows the matching error. |
| JavaScript alerts | `JavaScriptAlertsTest` | 4 | Alert, confirm and prompt (with typed text) show the right result. A missing button is handled. |
| Status code links | `RedirectTest` | 6 | Links for 200, 301, 404 and 500 go to the right page. Codes with no link are handled. |
| Dropdown | `DropdownTest` | 2 | Each option can be selected and shows the right text. |
| File upload | `FileUploadTest` | 4 | Text and image files upload and show their name. Missing local files are rejected before upload. |

That's 19 test cases in total. The test data lives in `src/test/resources/*.csv`, so adding a case means adding a row, not writing code.

## Running it

Requires Java 17+, Maven and Google Chrome. Selenium downloads the matching ChromeDriver automatically.

```bash
mvn test                    # runs all tests in Chrome
mvn test -Dheadless=true    # runs without opening browser windows
```

Afterwards, open `test-output/reports/ExtentReport.html` to see the report. Screenshots are saved in `test-output/screenshots/`.

## Design

- **Page Object Model.** Each page has a class in `src/main/java/pages` that knows its locators and actions, such as `LoginPage.enterUsername()`. Tests describe *what* to check, and if the site's HTML changes, only the page class needs updating.
- **Data-driven tests.** JUnit 5's `@ParameterizedTest` with `@CsvFileSource` runs each test method once per CSV row.
- **Explicit waits.** `WebDriverWait` waits for elements and page changes instead of fixed sleeps, so tests don't fail on a slow upload or page load.
- **Reporting.** `TestBase` starts a fresh Chrome for each test, takes a screenshot afterwards, and adds both to one ExtentReports HTML report.

```
src/main/java/pages/   Page objects: LoginPage, JavaScriptAlertsPage, RedirectPage, DropdownPage, FileUploadPage
src/test/java/tests/   Tests and TestBase (browser setup, screenshots, report)
src/test/resources/    CSV test data
test_files/            Files used by the upload tests
```

## Credits

Built as a pair project for a university software testing module with Aaron Noade. Eunan Loughran wrote the test code, and the test strategy was written jointly.
