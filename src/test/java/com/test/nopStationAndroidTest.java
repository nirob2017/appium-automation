package com.test;

import com.test.helpers.AppiumServerSetup;
import com.test.pages.AddProductToCartPage;
import com.test.pages.OrderDetailsPage;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.testng.annotations.*;

import java.net.MalformedURLException;

@Listeners({ io.qameta.allure.testng.AllureTestNg.class })
public class nopStationAndroidTest {

    private static AndroidDriver driver;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        driver = AppiumServerSetup.setupServer();
    }

    @Test
    @Feature("Shopping Cart")
    @Story("Add Product to Cart")
    public void addProductToCartTest() {
        AddProductToCartPage addProductToCart = new AddProductToCartPage(driver);
        addProductToCart.addProductToCartTest();
    }

    @Test
    @Feature("Order Placement")
    @Story("Place Order")
    public void placeOrderTest() throws InterruptedException {
        OrderDetailsPage orderDetail = new OrderDetailsPage(driver);
        orderDetail.placeOrderTest();
    }

    @AfterAll
    public static void tearDownServer() {
        if (driver != null) {
            driver.quit();
        }
    }
}
