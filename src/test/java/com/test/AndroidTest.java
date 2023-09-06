package com.test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidTest {
    public enum SwipeDirection {
        HORIZONTAL, VERTICAL
    }
    @Test
    public void androidLaunchTest() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UIAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir")+"/app/nopstationCart_4.40.apk");

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        WebElement buttonAccept = wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("btnAccept"))));
        buttonAccept.click();

        WebElement elementToSwipe = driver.findElement(AppiumBy.id("rvHomeCategories"));
        String targetElementText = "Electronics";

        // Set a maximum number of swipes to avoid infinite looping
        int maxSwipes = 3;
        int swipes = 0;
        while (swipes < maxSwipes && !isTextVisible(driver, targetElementText)) {
            swipe(driver, SwipeDirection.HORIZONTAL, elementToSwipe);
            swipes++;
        }

        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + targetElementText + "\")")).click();

        String mobileText = "Mattress Bedroom";
        swipes = 1;
        while (swipes < maxSwipes && !isTextVisible(driver, mobileText)) {
            swipe(driver, SwipeDirection.VERTICAL);
            swipes++;
        }

        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + mobileText + "\")")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("btnAddToCart"))));
        swipe(driver, SwipeDirection.VERTICAL);
        driver.findElement(AppiumBy.id("btnPlus")).click();
        driver.findElement(AppiumBy.id("btnAddToCart")).click();
        driver.quit();

    }

    public static void swipe(AndroidDriver driver, SwipeDirection direction, WebElement... elements) {
        if (elements.length > 1) throw new IllegalArgumentException("Multiple elements provided, only one is allowed");

        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.7);
        int endX = (int) (size.width * 0.3);
        int startY = size.height / 2;
        int endY = size.height / 2; // Declare endY here

        if (direction == SwipeDirection.VERTICAL) {
            startY = (int) (size.height * 0.7); // Adjust startY for vertical swipe
            endY = (int) (size.height * 0.3);  // Adjust endY for vertical swipe
            startX = endX = size.width / 2;
        }

        if (elements.length == 1) {
            endY = elements[0].getLocation().getY() + elements[0].getSize().getHeight() / 2;
        }

        new TouchAction<>(driver)
                .press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
    }

    private static boolean isTextVisible(AndroidDriver driver, String expectedText) {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.matches(expectedText);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }
}
