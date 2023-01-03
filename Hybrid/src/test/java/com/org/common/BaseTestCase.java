package com.org.common;

import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.org.CoreUtils.CommonUserActions;
import com.org.CoreUtils.Log;
import com.org.dbUtils.SQLConnection;
import com.org.driver.BaseWebDriver;
import com.org.driver.WebDriverRunner;

public abstract class BaseTestCase {

	protected WebDriver driver;

	/**
	 * Definition of page, that will be used in test
	 */
	public abstract void initPages();

	// Declare all the class names that you want to test

	public SQLConnection jdbcReader;

	public CommonUserActions commonUserAction = new CommonUserActions();
	String TestCaseID = "";

	@BeforeClass(alwaysRun = true)
	public void launchBrowser() {
		commonUserAction.configureLog4jPropertiesFile();
		driver = (BaseWebDriver) WebDriverRunner.getWebDriver();
		initPages();
		// eapCommon.maximizeWindow();
		// eapCommon.implicitWaitFor(30);
		// eapCommon.pageLoadTimeOut(30);
		Log.warn("Browser is opened on the page");
	}

	@AfterClass(alwaysRun = true)
	public void closeBrowser() {
		WebDriverRunner.closingWebDriver();
	}

	@BeforeMethod(alwaysRun = true)
	public void startTest(Method method) {
		Log.startTestCase(this.getClass().getSimpleName() + "_" + method.getName());

	}

	@AfterMethod(alwaysRun = true)
	public void failureCatcher(ITestResult testResult) throws IOException {
		Log.endTestCase(this.getClass().getSimpleName() + "_" + testResult.getName());
		if (!testResult.isSuccess()) {
			Log.error("Test Failed.. " + this.getClass().getSimpleName() + "_" + testResult.getName());
			Log.info("*****************************************");
			Log.error("******************  Message  *******************\n" + testResult.getThrowable().getMessage()
					+ "\n");
			WebDriver driver = BaseWebDriver.getWebDriver();
			commonUserAction.captureScreenShot(driver, this.getClass().getSimpleName() + "_" + testResult.getName());
		}
	}

}
