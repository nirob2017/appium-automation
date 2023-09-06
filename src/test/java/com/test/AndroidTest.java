package com.test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileCommand;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AndroidTest {
    public enum SwipeDirection {
        HORIZONTAL, VERTICAL
    }

    protected static AndroidDriver driver;

    @BeforeAll
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UIAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir") + "/app/nopstationCart_4.40.apk");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
    }

    @Test
    public void addProductToCartTest() throws IOException {
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

        String productText = "Mattress Bedroom";
        swipes = 0;
        while (swipes < maxSwipes && !isTextVisible(driver, productText)) {
            swipe(driver, SwipeDirection.VERTICAL);
            swipes++;
        }

        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + productText + "\")")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("btnAddToCart"))));
        swipe(driver, SwipeDirection.VERTICAL);
        driver.findElement(AppiumBy.id("btnPlus")).click();
        driver.findElement(AppiumBy.id("btnAddToCart")).click();
    }

    @Test
    public void placeOrderTest() throws InterruptedException {
        driver.findElement(AppiumBy.id("menu_cart")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement checkout = wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + "CHECKOUT" + "\")")));
        checkout.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("btnGuestCheckout"))));
        driver.findElement(AppiumBy.id("btnGuestCheckout")).click();

        List<String> data = extractDataFromExcel(System.getProperty("user.dir") + "/src/test/java/com/test/BillingDetails.xlsx", "sheet1");
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("etFirstName"))));
        driver.findElement(AppiumBy.id("etFirstName")).sendKeys(data.get(0));
        driver.findElement(AppiumBy.id("etLastName")).sendKeys(data.get(1));
        driver.findElement(AppiumBy.id("etEmail")).sendKeys(data.get(2));
        driver.findElement(AppiumBy.id("countrySpinner")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + data.get(3) + "\")")).click();
        driver.findElement(AppiumBy.id("etCompanyName")).sendKeys(data.get(4));
        swipe(driver, SwipeDirection.VERTICAL);
        driver.findElement(AppiumBy.id("etCity")).sendKeys(data.get(5));
        driver.findElement(AppiumBy.id("etStreetAddress")).sendKeys(data.get(6));
        driver.findElement(AppiumBy.id("etZipCode")).sendKeys(data.get(7));
        driver.findElement(AppiumBy.id("etPhone")).sendKeys(data.get(8));
        driver.findElement((AppiumBy.id("btnContinue"))).click();
        Thread.sleep(2000);
        swipe(driver, SwipeDirection.VERTICAL);
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + "Next Day Air" + "\")")).click();
        swipe(driver, SwipeDirection.VERTICAL);
        driver.findElement((AppiumBy.id("btnContinue"))).click();
        int maxSwipe = 5;
        int swipes = 0;
        while (swipes < maxSwipe) {
            swipe(driver, SwipeDirection.VERTICAL);
            swipes++;
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("tvPaymentMethodDetails"))));
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + "Check / Money Order" + "\")")).click();
        driver.findElement((AppiumBy.id("btnContinue"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("toolbarTitle"))));
        driver.findElement(AppiumBy.className("android.widget.Button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("btnContinue")))).isDisplayed();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"" + "CONFIRM" + "\")")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated((AppiumBy.id("md_button_positive"))));
        driver.findElement(AppiumBy.id("md_text_message")).getText().contains("Your order has been successfully processed!");
        driver.findElement((AppiumBy.id("md_button_positive"))).click();
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

    public List<String> extractDataFromExcel(String filePath, String sheetName) {
        List<String> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            // Assuming the second row contains data
            Row dataRow = sheet.getRow(1);

            for (int i = 0; i < dataRow.getLastCellNum(); i++) {
                Cell cell = dataRow.getCell(i);
                String value = "";

                if (cell != null) {
                    if (cell.getCellType() == CellType.STRING) {
                        value = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                    }
                }

                data.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
