package test.test;

import com.driver.DriverManager;
import com.report.ReportManager;
import com.page.HomePage;
import org.testng.annotations.Test;
import test.base.TestNGBase;

import java.time.Duration;

public class ExampleTestSoftAssert extends TestNGBase {

    private final HomePage homePage = new HomePage();

    @Test
    public void verifyElementRetry() {
        homePage.getInvalidTextBox(Duration.ofSeconds(10));
    }

    @Test
    public void verifyFailedExampled() {

        DriverManager.open("https://the-internet.herokuapp.com/dynamic_controls");

        ReportManager.info("- Verify Add button is not visible");

        softAssert.assertTrue(() -> homePage.isAddButtonDisplayed(), "Add button should not be visible.");

        ReportManager.info("- Verify Remove button is enabled");

        softAssert.assertTrue(() -> homePage.isRemoveButtonEnabled(), "Remove button should be enabled.");

        softAssert.assertEquals(() -> homePage.getRemoveButtonText(), "Remove", "Remove button should have text Remove.");

        ReportManager.info("- Click Remove button");

        homePage.clickRemoveButton();

        ReportManager.info("- Verify Add button is visible");

        softAssert.assertTrue(() -> homePage.isAddButtonDisplayed(), "Add button should be visible.");

        softAssert.assertEquals(() -> homePage.getAddButtonText(), "Add", "Add button should have text Add.");

        ReportManager.info("- Click Add button");

        homePage.clickAddButton();

        ReportManager.info("- Click Enable button");

        homePage.clickEnableButton();

        ReportManager.info("- Verify textbox is enabled");

        softAssert.assertTrue(() -> homePage.isTextboxEnabled(), "Textbox should be enabled.");

        ReportManager.info("- Click Disable button");

        homePage.clickDisableButton();

        ReportManager.info("- Verify textbox is disabled");

        softAssert.assertFalse(() -> homePage.isTextboxEnabled(), "Textbox should be disabled.");
    }
}
