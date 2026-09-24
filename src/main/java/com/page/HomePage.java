package com.page;

import com.element.Element;
import org.openqa.selenium.By;

public class HomePage {

    private final Element addRemoveElementsLink =
            new Element(By.linkText("Add/Remove Elements"));

    public void openAddRemoveElements() {
        addRemoveElementsLink.click();
    }
}
