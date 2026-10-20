package test.test;

import com.driver.DriverManager;
import com.element.Element;
import com.page.HomePage;
import com.report.ReportManager;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import test.base.TestNGBase;

import java.time.Duration;

public class ExampleTestHardAssert extends TestNGBase {
    private final HomePage homePage = new HomePage();

    @Test
    public void verifyExampledHardTest() {

        DriverManager.open("https://the-internet.herokuapp.com/dynamic_controls");

        ReportManager.info("- Verify Add button is not visible");

        hardAssert.assertFalse(()->homePage.isAddButtonDisplayed(), "Add button should not be visible.");

        ReportManager.info("- Verify Remove button is enabled");

        hardAssert.assertTrue(() -> homePage.isRemoveButtonEnabled(), "Remove button should be enabled.");

        hardAssert.assertEquals(() -> homePage.getRemoveButtonText(), "Remove", "Remove button should have text Remove.");

        ReportManager.info("- Click Remove button");

        homePage.clickRemoveButton();

        ReportManager.info("- Verify Add button is visible");

        hardAssert.assertTrue(() -> homePage.isAddButtonDisplayed(), "Add button should be visible.");

        hardAssert.assertEquals(() -> homePage.getAddButtonText(), "Add", "Add button should have text Add.");

        ReportManager.info("- Click Add button");

        homePage.clickAddButton();

        ReportManager.info("- Click Enable button");

        homePage.clickEnableButton();

        ReportManager.info("- Verify textbox is enabled");

        hardAssert.assertTrue(() -> homePage.isTextboxEnabled(), "Textbox should be enabled.");

        ReportManager.info("- Click Disable button");

        homePage.clickDisableButton();

        ReportManager.info("- Verify textbox is disabled");

        hardAssert.assertFalse(() -> homePage.isTextboxEnabled(), "Textbox should be disabled.");
    }
}
