package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import pages.FileUploadPage;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUploadTest extends TestBase {
    private static final Path TEST_FILES = Path.of("test_files").toAbsolutePath();
    private FileUploadPage fileUploadPage;

    @BeforeEach
    void openPage() {
        driver.get("https://the-internet.herokuapp.com/upload");
        fileUploadPage = new FileUploadPage(driver);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/uploads.csv", numLinesToSkip = 1)
    void uploadsFile(String testId, String fileName, String expectedResult) {
        test = extent.createTest(testId);
        String filePath = TEST_FILES.resolve(fileName).toString();
        try {
            if (expectedResult.equals("SUCCESS")) {
                fileUploadPage.uploadFile(filePath);
                assertEquals(fileName, fileUploadPage.getUploadedFileName());
                test.pass("Uploaded " + fileName);
            } else {
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> fileUploadPage.uploadFile(filePath));
                assertTrue(e.getMessage().contains("File not found"));
                test.pass("Rejected missing file " + fileName);
            }
        } catch (AssertionError | RuntimeException e) {
            test.fail(e);
            throw e;
        }
    }
}
