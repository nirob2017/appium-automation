package com.test.pages;

import com.test.helpers.BaseCommands;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;


public class AddProductToCartPage {

    private final AndroidDriver driver;

    public AddProductToCartPage(AndroidDriver driver) {
        this.driver = driver;
    }


    public void addProductToCartTest() {
        BaseCommands base = new BaseCommands(driver);

        /* Accepting Terms & Conditions */
        WebElement acceptButton = base.waitForElement("btnAccept");
        acceptButton.click();

        /* Clicking "electronics" from "OUR CATEGORIES" list from home page */
        WebElement elementToSwipe = base.findElement("rvHomeCategories");
        int maxSwipes = 2;
        int swipes = 0;
        while (swipes < maxSwipes && !base.isTextVisible(driver, "Electronics")) {
            base.swipe(driver, BaseCommands.SwipeDirection.HORIZONTAL, elementToSwipe);
            swipes++;
        }
        base.clickOnElementUsingText("Electronics");

        /* Clicking to "Mattress Bedroom" product details page */
        swipes = 0;
        while (swipes < maxSwipes && !base.isTextVisible(driver, "Mattress Bedroom")) {
            base.swipe(driver, BaseCommands.SwipeDirection.VERTICAL);
            swipes++;
        }
        base.clickOnElementUsingText("Mattress Bedroom");

        /* Clicking plus button to increase Qty by "2" */
        base.waitForElement("btnAddToCart");
        base.swipe(driver, BaseCommands.SwipeDirection.VERTICAL);
        base.clickOnElement("btnPlus");

        /* Clicking add to cart button to add the product in his cart */
        base.clickOnElement("btnAddToCart");
    }
}
