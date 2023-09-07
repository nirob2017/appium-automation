package com.test.helpers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumServerSetup {
    public static AndroidDriver setupServer() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UIAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir") + "/app/nopstationCart_4.40.apk");
        return new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), options);
    }
}
