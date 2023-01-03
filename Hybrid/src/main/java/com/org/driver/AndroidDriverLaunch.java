package com.org.driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by user .
 */

public class AndroidDriverLaunch {

	public static WebDriver getAndroidDriver() {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("deviceName", "my_android");
		try {
			return new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
