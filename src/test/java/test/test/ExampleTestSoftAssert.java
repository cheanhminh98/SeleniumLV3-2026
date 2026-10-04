package test.test;

import com.driver.DriverManager;
import com.element.Element;
import com.report.ReportManager;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import test.base.TestNGBase;

public class ExampleTestSoftAssert extends TestNGBase {

    Element addButton = new Element(By.xpath("//button[text()='Add']"));
    Element removeButton = new Element(By.xpath("//button[text()='Remove']"));
    Element enableButton = new Element(By.xpath("//button[text()='Enable']"));
    Element disableButton = new Element(By.xpath("//button[text()='Disable']"));
    Element textbox = new Element(By.xpath("//input[@type='text']"));

    @Test
    public void verifyFailedExampled() {
        DriverManager.open("https://the-internet.herokuapp.com/dynamic_controls");

        ReportManager.info("- Verify Add button is not visible");
        softAssert.assertFalse(addButton::isDisplayed, "Add button should not be visible.");

        ReportManager.info("- Verify Remove button is enabled");
        softAssert.assertTrue(removeButton::isEnabled, "Remove button should be enabled.");

        softAssert.assertEquals(removeButton.getText(), "ABC Test", "Remove button should have text Remove.");

        ReportManager.info("- Click Remove button");
        removeButton.click();

        ReportManager.info("- Verify Add button is visible");
        softAssert.assertTrue(addButton::isDisplayed, "Add button should be visible.");

        softAssert.assertEquals(addButton.getText(), "Add", "Add button should have text Add.");

        ReportManager.info("- Click Add button");
        addButton.click();

        ReportManager.info("- Click Enable button");
        enableButton.click();

        ReportManager.info("- Verify textbox is enabled");
        softAssert.assertTrue(textbox::isEnabled, "Textbox should be enabled.");

        ReportManager.info("- Click Disable button");
        disableButton.click();

        ReportManager.info("- Verify textbox is disabled");
        softAssert.assertFalse(textbox::isEnabled, "Textbox should be disabled.");
    }
}
