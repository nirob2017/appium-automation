package com.test.pages;

import com.test.helpers.BaseCommands;
import com.test.helpers.Utils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OrderDetailsPage {

    private final AndroidDriver driver;

    public OrderDetailsPage(AndroidDriver driver) {
        this.driver = driver;
    }

    public void placeOrderTest() throws InterruptedException {
        BaseCommands base = new BaseCommands(driver);

        /* Going to shopping cart by clicking top cart icon */
        base.clickOnElement("menu_cart");

        /* Clicking checkout button from shopping cart page */
        WebElement checkout = base.waitForTextElement("CHECKOUT");
        checkout.click();

        /* Selecting checkout as guest from shopping cart page */
        base.waitForElement("btnGuestCheckout");
        base.clickOnElement("btnGuestCheckout");

        /* Input all the details in checkout billing details page and click continue */
        fillingUpBillingForm();

        /* Selecting "Next Day Air" as shipping method and click continue */
        base.swipe(driver, BaseCommands.SwipeDirection.VERTICAL);
        base.clickOnElementUsingText("Next Day Air");
        base.clickOnElement("btnContinue");

        /* Selecting "Check/Money Order" as payment method and click continue */
        base.waitForElement("tvPaymentMethodDetails");
        int maxSwipe = 4;
        int swipes = 0;
        while (swipes < maxSwipe) {
            base.swipe(driver, BaseCommands.SwipeDirection.VERTICAL);
            swipes++;
        }
        base.clickOnElementUsingText("Check / Money Order");
        base.clickOnElement("btnContinue");

        /* Click Next button on payment information page */
        base.waitForElement("toolbarTitle");
        base.clickOnElementUsingClass("android.widget.Button");

        /* Clicking Confirm button to place the order */
        base.waitForElement("btnContinue");
        base.clickOnElementUsingText("CONFIRM");

        /* Verifying order place successfully with popup message "Your order has been successfully processed!" */
        base.elementIsVisible("md_button_positive");
        base.checkTextOfElementID("md_text_message","Your order has been successfully processed!");
        base.clickOnElement("md_button_positive");
    }

    private void fillingUpBillingForm() throws InterruptedException {
        BaseCommands base = new BaseCommands(driver);
        String filePath = "/src/test/java/com/test/testData/BillingDetails.xlsx";
        List<String> data = new Utils().extractDataFromExcel(System.getProperty("user.dir") + filePath, "sheet1");
        base.waitForElement("etFirstName");
        base.fillUpInputField("etFirstName", data.get(0));
        base.fillUpInputField("etLastName", data.get(1));
        base.fillUpInputField("etEmail", data.get(2));
        base.clickOnElement("countrySpinner");
        base.clickOnElementUsingText(data.get(3));
        base.fillUpInputField("etCompanyName", data.get(4));
        base.swipe(driver, BaseCommands.SwipeDirection.VERTICAL);
        base.fillUpInputField("etCity", data.get(5));
        base.fillUpInputField("etStreetAddress", data.get(6));
        base.fillUpInputField("etZipCode", data.get(7));
        base.fillUpInputField("etPhone", data.get(8));
        base.clickOnElement("btnContinue");
    }
}
