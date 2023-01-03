package com.org.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;


public class SafariLauncher {

	protected static WebDriver driver;

	public static WebDriver getSafariDriver() {
		driver = new SafariDriver();
		return driver;
	}
}
