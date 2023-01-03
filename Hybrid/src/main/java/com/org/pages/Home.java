package com.org.pages;

import org.testng.Assert;

import com.org.CoreUtils.CommonUserActions;
import com.org.CoreUtils.Log;
import com.org.objectRepository.HomePage;
import com.org.resourceUtils.AppReader;
import com.org.resourceUtils.ResourceReader;

public class Home extends CommonUserActions implements HomePage {

	final static String APP_URL = ResourceReader.getAppSettings(AppReader.APP_URL);

	/**
	 * Navigated to the application
	 */
	public void navigateToApplication() {
		maximizeWindow();
		getUrl(APP_URL);
		implicitWaitFor(30);
	}

	/**
	 * Logged into the application
	 * 
	 * @param email
	 * @param password
	 */
	public void loginToApp(String email, String password) {
		waitForVisibilityOfElement("Xpath", SIGN_IN_CREATE);
		clikRefresh(SIGN_IN_CREATE);

		waitForVisibilityOfElement("xpath", SIGN_IN);
		clikRefresh(SIGN_IN);

		waitForVisibilityOfElement("id", SIGN_IN_EMAIL);
		enterText("id", SIGN_IN_EMAIL, email);
		enterText("id", SIGN_IN_PASSWORD, password);

		pauseTest();
		findElement("xpath", LOGED_IN).submit();
		// waitAndClickElement("xpath", LOGED_IN);
		waitForInVisibilityOfElement("xpath", "//button[@aria-disabled='true' and class='button loading']");

		waitForVisibilityOfElement("xpath", USER_DETAILS);
		Assert.assertTrue(getTextOfElement("xpath", USER_DETAILS).contains("Jadhav"), "Log in failed...!");
	}

	/**
	 * Navigate to the pages
	 * 
	 * @param linkName
	 * @param pageHeader
	 */
	public void pageNavigate(String linkName, String pageHeader) {
		waitForVisibilityOfElement("Xpath", SECTIONS.replace("%s", linkName));
		clickJavaScript("Xpath", SECTIONS.replace("%s", linkName));

		waitForVisibilityOfElement("xpath", HEADER_TEXT);
		Assert.assertTrue(getTextOfElement("xpath", HEADER_TEXT).contains(pageHeader),
				"Navigated to some other page ..!");
		Log.info("Navigated to : " + getTitleOfThePage());
	}

	/**
	 * Verify whether the kids page redirected
	 * 
	 * @param pageHeader
	 */
	public void isKidsTabOpened() {
		waitForVisibilityOfElement("Xpath", KIDS_LINK);
		waitAndClickElement("Xpath", KIDS_LINK);

		Log.info("Navigated to : " + getTitleOfThePage());
		Assert.assertTrue(getTitleOfThePage().contains("abercrombie kids"), "Navigated to some other page ..!");
	}

	/**
	 * ADD items to the cart
	 * 
	 * @param subCatogery
	 * @param size
	 * @param quantity
	 */
	public void addItemToCart(String subCatogery) {
		waitAndClickElement("xpath", MEN_SUB_CAT.replace("%s", subCatogery));
		waitAndClickElement("xpath", FIRST_ITEM);
		waitForVisibilityOfElement("Xpath", ADDTOBAG);
		clickJavaScript("xpath", COLORS_LAST);
		clickJavaScript("xpath", SIZE_LAST);

		clickJavaScript("Xpath", ADDTOBAG);
		clickJavaScript("Xpath", ADDTOBAG);
		pauseTest();
		pauseTest();
	}

	public void viewItemFromCart() {
		pauseTest();
		waitForVisibilityOfElement("xpath", BAG);
		clickJavaScript("xpath", BAG);
		pauseTest();
		pauseTest();
	}

	public void saveToCart(String subCatogery) {
		waitAndClickElement("xpath", MEN_SUB_CAT.replace("%s", subCatogery));
		waitAndClickElement("xpath", FIRST_ITEM);
		waitForVisibilityOfElement("Xpath", ADDTOBAG);
		clickJavaScript("xpath", COLORS_LAST);
		clickJavaScript("xpath", SIZE_LAST);
		clickJavaScript("xpath", SAVE_LATER);
		pauseTest();
		pauseTest();
	}

}
