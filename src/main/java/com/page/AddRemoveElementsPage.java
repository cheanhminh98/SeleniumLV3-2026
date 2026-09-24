package com.page;

import com.element.Element;
import org.openqa.selenium.By;

public class AddRemoveElementsPage {

    private final Element addElementButton =
            new Element(By.xpath("//button[normalize-space()='Add Element']"));

    private final Element deleteButton =
            new Element(By.xpath("//button[normalize-space()='Delete']"));

    public void addElement() {
        addElementButton.click();
    }

    public boolean isDeleteButtonDisplayed() {
        return deleteButton.isDisplayed();
    }

    public void deleteElement() {
        deleteButton.click();
    }
}