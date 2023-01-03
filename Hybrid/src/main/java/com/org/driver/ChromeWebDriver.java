package com.org.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.org.CoreUtils.Log;
import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.ResourceReader;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * ChromeWebDriver
 */

public class ChromeWebDriver extends BaseWebDriver {
	// ~ Methods
	// --------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initDriver() {
		if (runOnvCommander.equalsIgnoreCase("Yes"))
			setDriverSystemProperty();
		else {
			Log.info("Initializing the browser's driver using Webdriver Manager");
			WebDriverManager.chromedriver().setup();
		}

		// set additional chrome options
		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
//    chromeOptions.addArguments("no-sandbox");

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory",
				System.getProperty("user.dir") + File.separator + "libs" + File.separator + "Downloads");
		chromeOptions.setExperimentalOption("prefs", chromePrefs);

		final List<String> optionList = new ArrayList<String>();
		// chromeOptions.add
		optionList.add("--incognito");
		optionList.add("--ignore-certificate-errors");
		optionList.add("--disable-infobars");
		optionList.add("--disable-notifications");
		optionList.add("--disable-default-apps");
		optionList.add("--disable-web-security");
		optionList.add("--disable-site-isolation-trials");
		optionList.add("--disable-logging");
		optionList.add("--disable-bundled-ppapi-flash");
		optionList.add("--disable-gpu-compositing");
		optionList.add("--disable-gpu-shader-disk-cache");
		optionList.add("--disable-blink-features=AutomationControlled");
		optionList.add("--disable-extensions");
		optionList.add("--disable-web-security");
		optionList.add("--allow-running-insecure-content");
		
		chromeOptions.addArguments(optionList);
		driver.set(new ChromeDriver(chromeOptions));
	}

	/**
	 * this method sets the BrowserProperties.WEBDRIVER_CHROME_DRIVER system
	 * property based on the operating system.
	 */
	private void setDriverSystemProperty() {

		Log.info("Initializing the browser's driver using the executable driver file");
		final String osInfo = ResourceReader.getSettings(BrowserReader.OPERATING_SYSTEM);

		if (osInfo.equalsIgnoreCase(BrowserReader.CHROME_PATH_MAC)) {
			System.setProperty(BrowserReader.WEBDRIVER_CHROME_DRIVER,
					ResourceReader.getSettings(BrowserReader.CHROME_PATH_MAC));
		} else if (osInfo.equalsIgnoreCase(BrowserReader.CHROME_PATH_LINUX32)) {
			System.setProperty(BrowserReader.WEBDRIVER_CHROME_DRIVER,
					ResourceReader.getSettings(BrowserReader.CHROME_PATH_LINUX32));
		} else if (osInfo.equalsIgnoreCase(BrowserReader.CHROME_PATH_LINUX64)) {
			System.setProperty(BrowserReader.WEBDRIVER_CHROME_DRIVER,
					ResourceReader.getSettings(BrowserReader.CHROME_PATH_LINUX64));
		} else {
			System.setProperty(BrowserReader.WEBDRIVER_CHROME_DRIVER,
					ResourceReader.getSettings(BrowserReader.CHROME_PATH_WIN));
		}
	}
}
