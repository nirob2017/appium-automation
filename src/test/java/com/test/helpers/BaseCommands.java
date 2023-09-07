package com.test.helpers;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseCommands {

    private final AndroidDriver driver;

    public BaseCommands(AndroidDriver driver) {
        this.driver = driver;
    }

    public void clickOnElement(String elementID) {
        driver.findElement((AppiumBy.id(elementID))).click();
    }

    public WebElement findElement(String elementID) {
        return driver.findElement((AppiumBy.id(elementID)));
    }

    public WebElement waitForElement(String elementID) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        return wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id(elementID))));
    }

    public WebElement waitForTextElement(String text) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")")));
    }

    public void clickOnElementUsingText(String text) {
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + text + "\")")).click();
    }

    public void fillUpInputField(String elementID, String data) {
        driver.findElement(AppiumBy.id(elementID)).sendKeys(data);
    }

    public void clickOnElementUsingClass(String className) {
        driver.findElement(AppiumBy.className(className)).click();
    }

    public void checkTextOfElementID(String elementID, String text) {
        driver.findElement(AppiumBy.id(elementID)).getText().contains(text);
    }

    public boolean elementIsVisible(String elementID) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(500));
            return wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id(elementID)))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTextVisible(AndroidDriver driver, String expectedText) {
        try {
            String pageSource = driver.getPageSource();
            return pageSource.matches(expectedText);
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }

    public void swipe(AndroidDriver driver, SwipeDirection direction, WebElement... elements) {
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

    public enum SwipeDirection {
        HORIZONTAL, VERTICAL
    }
}
