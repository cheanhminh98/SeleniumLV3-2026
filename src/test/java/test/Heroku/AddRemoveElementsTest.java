package test.Heroku;

import com.assertion.Assertion;
import com.page.AddRemoveElementsPage;
import com.page.HomePage;
import org.testng.annotations.Test;

import test.base.TestNGBase;

public class AddRemoveElementsTest extends TestNGBase {

    private final HomePage homePage = new HomePage();
    private final AddRemoveElementsPage addRemoveElementsPage =
            new AddRemoveElementsPage();

    @Test
    public void verifyAddElement() {
        homePage.openAddRemoveElements();
        addRemoveElementsPage.addElement();
        Assertion.assertTrue(
                addRemoveElementsPage::isDeleteButtonDisplayed,
                "Delete button should be displayed after adding an element"
        );

        addRemoveElementsPage.
                isDeleteButtonDisplayed();

//        Assertion.assertTrue(addRemoveElementsPage.isDeleteButtonDisplayed(), "Delete button should be displayed after adding an element");
        Assertion.assertAll();
    }
}