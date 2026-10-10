package com.page;

import com.element.Element;
import org.openqa.selenium.By;

import java.time.Duration;

public class HomePage {

    private final Element addButton = new Element(By.xpath("//button[text()='Add']"));

    private final Element removeButton = new Element(By.xpath("//button[text()='Remove']"));

    private final Element enableButton = new Element(By.xpath("//button[text()='Enable']"));

    private final Element disableButton = new Element(By.xpath("//button[text()='Disable']"));

    private final Element textbox = new Element(By.xpath("//input[@type='text']"));
    private final Element invalidTextBox = new Element(By.xpath("//input[@type='text111111']"));

    public String getInvalidTextBox(Duration timeout) {
        return invalidTextBox.getText(timeout);
    }

    public boolean isAddButtonDisplayed() {
        return addButton.isDisplayed();
    }

    public String getAddButtonText() {
        return addButton.getText();
    }

    public boolean isRemoveButtonEnabled() {
        return removeButton.isEnabled();
    }

    public String getRemoveButtonText() {
        return removeButton.getText();
    }

    public void clickRemoveButton() {
        removeButton.click();
    }

    public void clickAddButton() {
        addButton.click();
    }

    public void clickEnableButton() {
        enableButton.click();
    }

    public void clickDisableButton() {
        disableButton.click();
    }

    public boolean isTextboxEnabled() {
        return textbox.isEnabled();
    }
}
