package com.test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidTest {

    @Test
    public void androidLaunchTest() throws MalformedURLException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UIAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir")+"/app/nopstationCart_4.40.apk");

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
        Thread.sleep(4000);
        driver.findElement(AppiumBy.id("btnAccept")).click();

        WebElement elementToSwipe = driver.findElement(AppiumBy.id("rvHomeCategories"));
        String targetElementText = "tvProductName";

        // Set a maximum number of swipes to avoid infinite looping
        int maxSwipes = 10;
        int swipes = 0;

        while (swipes < maxSwipes) {
            // Perform a swipe
            performSwipe(driver, elementToSwipe);

            // Check if the target element is now visible
            if (isElementVisible(driver, targetElementText)) {
                // Element found, break the loop
                break;
            }

            swipes++;
        }

        driver.findElement(AppiumBy.id("tvProductName")).click();
        String mobileText = "Nokia Lumia 1020";

        boolean found = false;
        while (!found) {
            // Check if the element with the specified text is present
            if (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"" + mobileText + "\")")).size() > 0) {
                found = true;
                break;
            }

            // Swipe from bottom to top (adjust the coordinates as needed)
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.8);
            int endY = (int) (size.height * 0.2);

            TouchAction<?> action = new TouchAction<>(driver);
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                    .moveTo(PointOption.point(startX, endY))
                    .release()
                    .perform();
        }
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + mobileText + "\")")).click();
        Thread.sleep(4000);
        driver.findElement(AppiumBy.id("btnPlus")).click();
        driver.findElement(AppiumBy.id("tvSelectedAttr")).click();
        driver.findElements(By.className("android.widget.RadioButton")).get(1).click();
        Thread.sleep(500);
        driver.findElement(AppiumBy.id("tvDone")).click();
        driver.findElement(AppiumBy.id("btnAddToCart")).click();
        Thread.sleep(5000);
        driver.quit();

    }

    private static void performSwipe(AndroidDriver driver, WebElement elementToSwipe) {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8); // 90% from the right
        int endX = (int) (size.width * 0.2); // 10% from the right
        int y = elementToSwipe.getLocation().getY() + elementToSwipe.getSize().getHeight() / 2;

        TouchAction<?> action = new TouchAction<>(driver)
                .press(PointOption.point(startX, y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000))) // Adjust the duration as needed
                .moveTo(PointOption.point(endX, y))
                .release()
                .perform();
    }

    // Function to check if an element is visible
    private static boolean isElementVisible(AndroidDriver driver, String element) {
        try {
            return driver.findElement(AppiumBy.linkText(String.valueOf(element))).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException | org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }
}
