package com.org.driver;

import org.openqa.selenium.WebDriver;

import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.ResourceReader;

/**
 * WebDriverFactory
 */

public class WebDriverFactory {
	// ~ Methods
	// --------------------------------------------------------------------------------------

	/**
	 * WebDriver Factory class which is responsible to return the correct
	 * webdriver instance based on the type
	 *
	 * @param aType
	 *            - one of the BrowserType - iexplore, firefox, chrome, android,
	 *            iPhone
	 *
	 * @return WebDriver instance
	 */
	// public static WebDriver getWebDriver(final String aType)
	public static WebDriver getWebDriverByType() {
		final WebDriver driver;
		String browserType = ResourceReader
				.getSettings(BrowserReader.BROWSER);
		// switch (aType)
		switch (browserType) {
		// case BrowserType.IEXPLORE :
		case "iexplore":
			driver = new IEWebDriver();
			break;
		case "firefox":
			driver = new FireFoxWebDriver();
			break;
		case "edge":
			driver = new EdgeWebDriver();
			break;
		/*
		 * case "android" : driver = new DeviceLauncher(); break; case "ios" :
		 * driver = new DeviceLauncher(); break;
		 */
		default:
			// default launching is chrome browser
			driver = new ChromeWebDriver();
			break;
		}
		return driver;
	}
}
