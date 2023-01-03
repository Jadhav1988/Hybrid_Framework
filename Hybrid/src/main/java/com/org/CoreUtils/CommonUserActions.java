package com.org.CoreUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.org.driver.BaseWebDriver;
import com.org.driver.WebDriverRunner;

public class CommonUserActions {

	protected BaseWebDriver driver;
	WebDriverWait wait;
	String SCREENSHOTS_DIRECTORY = System.getProperty("user.dir");

	final DateFormat GLOBAL_DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
	List<File> screenshots = new ArrayList<File>();

	/**
	 * Constructor to initialize driver
	 */
	public CommonUserActions() {
		driver = (BaseWebDriver) WebDriverRunner.getWebDriver();
		wait = new WebDriverWait(driver, 30);
	}

	/*
	 * common methods to find elements and perform operations on those elements.
	 */
	public void getUrl(String url) {
		driver.get(url);
		Log.info("Navigated to the application : " + url);
	}

	// To validate the redirected page
	public void validateRedirectPage(String locatorType, String locatorValue) {
		if (isElementVisible(locatorType, locatorValue))
			Assert.assertTrue(true, "Page not redirected to " + locatorValue);
		else
			Assert.assertTrue(false, "Page not redirected to " + locatorValue);
	}

	// To maximise the browser window
	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public ArrayList<String> getOpenWindows() {
		ArrayList<String> windows = new ArrayList<String>(driver.getWindowHandles());
		return windows;
	}

	// Implicit wait method
	public void implicitWaitFor(Integer time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		Log.info(" waited implicitly for " + time + " Seconds");
	}

	// To wait for page load
	public void pageLoadTimeOut(Integer time) {
		driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
	}

	// To get the title of the page
	public String getTitleOfThePage() {
		return driver.getTitle();
	}

	// Move to webelement just to hover
	public void moveToWebElement(String locatorType, String locatorValue) {
		Actions acts = new Actions(driver);
		WebElement vElement = findElement(locatorType, locatorValue);
		acts.moveToElement(vElement).perform();
	}

	// Move to an element and click
	public void moveToWebElementByXpathAndClick(String locatorType, String locatorValue) {

	}

	public void moveToWebElementAndClick(String locatorType, String locatorValue) {
		Actions acts = new Actions(driver);
		WebElement vElement = findElement(locatorType, locatorValue);
		acts.moveToElement(vElement).build().perform();
		acts.click();
	}

	public void moveHoverWebElement(String locatorType, String locatorValue) {
		Actions acts = new Actions(driver);
		WebElement vElement = findElement(locatorType, locatorValue);
		acts.moveToElement(vElement).build().perform();
	}

	// Click on a perticular item in the list of webelements
	public boolean parseListClick(List<WebElement> elements, String msg) {
		try {
			for (WebElement element : elements) {
				final String txt = element.getText().trim();
				if (txt.equalsIgnoreCase(msg)) {
					element.click();
					Log.info("clicked on" + txt);
					return true;
				}
			}
		} catch (Exception e) {
			catchException(e);
		}
		return false;
	}

	// Get the tooltip on a perticular item in the list of webelements
	public boolean parseListTooltip(String LocatorVal, String msg) {
		try {
			List<WebElement> list = getListOfElements(LocatorVal);
			for (WebElement element : list) {
				final String txt = element.getAttribute("title");
				if (txt.equalsIgnoreCase(msg)) {
					Log.info("ToolTip Matched with Excepted: " + txt);
					return true;
				}
			}
		} catch (Exception e) {
			catchException(e);
		}
		return false;
	}

	// Compate the list items with the user input and return the result
	public boolean parseListItems(List<WebElement> elements, String msg) {
		try {
			for (WebElement element : elements) {

				final String txt = element.getText().trim();
				Log.info("**********getext= " + txt);
				if (txt.equalsIgnoreCase(msg))
					return true;
			}
		} catch (Exception e) {
			catchException(e);
		}
		return false;
	}

	// Compate the list items with the user input and return the result
	public int parseListItemsCount(List<WebElement> elements, String msg) {
		int count = 0;
		try {
			for (WebElement element : elements) {
				count = count + 1;
				final String txt = element.getText().trim();
				if (txt.equalsIgnoreCase(msg))
					return count;
			}
		} catch (Exception e) {
			catchException(e);
		}
		return count;
	}

	// To find a specific String item in the list of Strings passed
	public boolean parseListStringItems(List<String> elements, String msg) {
		try {
			for (String element : elements) {
				if (element.equalsIgnoreCase(msg)) {
					// pauseTest();
					return true;
				}
			}
		} catch (Exception e) {
			catchException(e);
		}
		// pauseTest();
		return false;
	}

	// get text of the dynamic element by replacing xpath dynamically
	public String getTextOfElementByXpathReplace(String elementXpath, String toreplace, String val) {
		return driver.findElement(By.xpath(elementXpath.replace(toreplace, val))).getText();
	}

	public boolean checkItemIsActive(String locatorType, String locatorValue, String element) {
		if (isElementVisible(locatorType, locatorValue)) {
			if (parseListItems(findElementList(locatorType, locatorValue), element)) {

				Log.info(element + " is Selected and active");
				return true;
			}
		}
		return false;
	}

	// to pause the script
	public void pauseTest() {
		driver.pause(1000);
	}

	// To switch to alert
	public void handleAlerts(String alertmsg, String btn) {
		driver.alertHandler(alertmsg, btn);
	}

	public boolean isAlertPresent() {
		return driver.isAlertDisplayed();
	}

	// Switch to parent/default window
	public void switchToDefault() {
		driver.switchTo().defaultContent();
	}

	// Switch to frame
	public void switchToFrame(String frameName) {
		driver.switchTo().frame(frameName);
	}

	public String getTextOfElement(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		return vElement.getText();
	}

	public String getTitleOfElement(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		return vElement.getAttribute("title");
	}

	// To get the attribute of the webelement by locator xpath
	public String getAttributeOfElement(String locatorType, String locatorValue, String attribute) {
		return findElement(locatorType, locatorValue).getAttribute(attribute);
	}

	public void clickWebElement(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		vElement.click();
		pauseTest();
	}

	// To select an element in dropdown list
	public void selectElementInDropDown(String locatorType, String locatorValue, String sOption, String sValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		selectAction(sValue, vElement, sOption);
	}

	private void selectAction(String sValue, WebElement selectElement, String sOption) {
		Select oSelect = new Select(selectElement);
		sOption = sOption.toUpperCase();
		switch (sOption) {
		case "VISIBLETEXT":
			oSelect.selectByVisibleText(sValue);
			break;
		case "VALUE":
			oSelect.selectByValue(sValue);
			break;
		case "INDEX":
			oSelect.selectByIndex(Integer.parseInt(sValue));
			break;
		default:
			Log.warn("Locator is NOT provided Correctly. Please give Valid Locator Type...!!!");
			break;
		}
	}

	public String getSelectedValueInDropDown(String locatorType, String locatorValue) {
		WebElement element = findElement(locatorType, locatorValue);
		Select select = new Select(element);
		String selectedOption = select.getFirstSelectedOption().getText();
		Log.info("Selected option: " + selectedOption);
		return selectedOption;
	}

	public List<String> getAllValueInDropDownList(String locatorType, String locatorValue) {
		List<String> liOptionName = new ArrayList<String>();
		WebElement vElement = findElement(locatorType, locatorValue);
		Select oSelect = new Select(vElement);
		List<WebElement> liAllOptions = oSelect.getOptions();
		if (liAllOptions.size() > 0) {
			for (int i = 0; i < liAllOptions.size(); i++) {
				liOptionName.add(liAllOptions.get(i).getText().trim());
			}
			return liOptionName;
		} else {
			System.out.println("No Search Value present...!!!");
		}
		return liOptionName;
	}

	/**
	 * Method to validate dropdown values with the expected values
	 * 
	 * @param locatorType
	 * @param locatorValue
	 * @param expValues
	 */
	public void verifyDropDownValuesWithExpected(String locatorType, String locatorValue, List<String> expValues) {
		List<String> actValues = getAllValueInDropDownList(locatorType, locatorValue);
		if (actValues.size() == expValues.size()) {
			Assert.assertTrue(actValues.equals(expValues),
					"Dropdown values--> " + actValues + " are not matched with the expected values-->" + expValues);
			Log.info("Dropdown values--> " + actValues + " matches with the expected values-->" + expValues);
		} else {
			Assert.fail("Dropdown values are not matching with the expected values");
		}

	}

	// To find an element by xpath and replace the xpath string dynamically
	public WebElement findWebElementByXpathReplace(String xpathElement, String toreplace, String val) {
		return findElement("Xpath", xpathElement.replace(toreplace, val));
	}

	// To click on an element by xpath and replace the xpath string dynamically
	public void clickWebElementByXpathReplace(String xpathElement, String toreplace, String val) {
		findElement("Xpath", xpathElement.replace(toreplace, val)).click();
		pauseTest();
	}

	public boolean isElementVisible(String locatorType, String locatorValue) {
		List<WebElement> vElementList = findElementList(locatorType, locatorValue);
		return vElementList.size() != 0;
	}

	/**
	 * Checks whether the element is displayed or not
	 * 
	 * @param locatorType
	 * @param locatorValue
	 * @return true if element is displayed
	 */

	public boolean isDisplayed(String locatorType, String locatorValue) {
		try {
			findElement(locatorType, locatorValue).isDisplayed();
			Log.info("Element displyed: " + locatorValue);
			return true;
		} catch (NoSuchElementException | StaleElementReferenceException e) {
			return false;
		}
	}

	public void enterText(String locatorType, String locatorValue, String searchText) {
		WebElement vElement = findElement(locatorType, locatorValue);
		vElement.clear();
		vElement.sendKeys(searchText);
		Log.info("Enterd text :" + searchText);
	}

	public void clearTextInEditField(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		vElement.clear();
		pauseTest();
	}

	// update the value of editfield with new value
	public void renameTextInEditField(String locatorType, String locatorValue, String textToEnter) {
		findElement(locatorType, locatorValue).sendKeys(Keys.chord(Keys.CONTROL, "a"), textToEnter);
		pauseTest();
	}

	// To drage an element from one location to another using webelements
	public void dragElements(WebElement srcElement, WebElement destElement) {
		driver.dragAndDrop(srcElement, destElement);
	}

	// Navigate to specific url
	public void goToUrl(String urlTxt) {
		driver.navigate().to(urlTxt);
		pauseTest();
	}

	// To check whether the element is selected or not by locator xpath
	public boolean isElementSelected(String locatorType, String locatorValue) {
		return findElement(locatorType, locatorValue).isSelected();
	}

	// To check whether the element is selected or not by locator xpath
	public boolean isCheckBoxSelected(String locatorType, String locatorValue) {
		return findElement(locatorType, locatorValue).isSelected();
	}

	// Create and return a list of elements with the locator xpath
	public List<String> createActualListItems(String SUB_SEARCH_ITEMS) {
		List<WebElement> search_list = findElementList("xpath", SUB_SEARCH_ITEMS);
		List<String> searchItems = new ArrayList<>();
		for (WebElement item : search_list) {
			searchItems.add(item.getText());
		}
		pauseTest();
		return searchItems;
	}

	// Create and return a list of elements with the locator xpath
	public List<String> createActualListAttributeValues(String SUB_SEARCH_ITEMS) {
		List<WebElement> search_list = findElementList("xpath", SUB_SEARCH_ITEMS);
		List<String> searchItems = new ArrayList<>();
		for (WebElement item : search_list) {
			searchItems.add(item.getAttribute("value"));
		}
		pauseTest();
		return searchItems;
	}

	// Create and return a list of elements with the locator xpath
	public List<String> createActualListItems(String locatorType, String locatorValue) {
		List<WebElement> search_list = findElementList(locatorType, locatorValue);
		List<String> searchItems = new ArrayList<>();
		for (WebElement item : search_list) {
			searchItems.add(item.getText());
		}
		pauseTest();
		return searchItems;
	}

	// To find an element and click on that element by locator xpath
	public boolean checkAndClickOnElement(String locatorType, String locatorValue) {
		pauseTest();
		if (isElementVisible(locatorType, locatorValue)) {
			clickWebElement(locatorType, locatorValue);
			driver.pause(2000);
			return true;
		}
		pauseTest();
		return false;
	}

	// To find element by locator name

	public WebElement findElement(String locatorType, String locatorValue) {
		locatorType = locatorType.toUpperCase();
		WebElement vElement = null;
		switch (locatorType) {
		case "ID":
			vElement = driver.findElement(By.id(locatorValue));
			break;
		case "NAME":
			vElement = driver.findElement(By.name(locatorValue));
			break;
		case "XPATH":
			vElement = driver.findElement(By.xpath(locatorValue));
			break;
		case "CLASS":
			vElement = driver.findElement(By.className(locatorValue));
			break;
		case "LINKTEXT":
			vElement = driver.findElement(By.linkText(locatorValue));
			break;
		case "PARTIALLINKTEXT":
			vElement = driver.findElement(By.partialLinkText(locatorValue));
			break;
		case "CSS":
			vElement = driver.findElement(By.cssSelector(locatorValue));
			break;
		case "TAGNAME":
			vElement = driver.findElement(By.tagName(locatorValue));
			break;
		default:
			Log.warn("Locator is NOT provided Correctly. Please give Valid Locator Type...!!!");
			break;
		}
		return vElement;
	}

	public List<WebElement> findElementList(String locatorType, String locatorValue) {
		locatorType = locatorType.toUpperCase();
		List<WebElement> vElementList = null;
		switch (locatorType) {
		case "ID":
			vElementList = driver.findElements(By.id(locatorValue));
			break;
		case "NAME":
			vElementList = driver.findElements(By.name(locatorValue));
			break;
		case "XPATH":
			vElementList = driver.findElements(By.xpath(locatorValue));
			break;
		case "CLASS":
			vElementList = driver.findElements(By.className(locatorValue));
			break;
		case "LINKTEXT":
			vElementList = driver.findElements(By.xpath(locatorValue));
			break;
		case "PARTIALLINKTEXT":
			vElementList = driver.findElements(By.partialLinkText(locatorValue));
			break;
		case "CSS":
			vElementList = driver.findElements(By.cssSelector(locatorValue));
			break;
		case "TAGNAME":
			vElementList = driver.findElements(By.tagName(locatorValue));
			break;
		default:
			Log.warn("Locator is NOT provided Correctly. Please give Valid Locator Type...!!!");
			break;
		}
		return vElementList;

	}

	// Wait for an element and click on element using locator xpath
	public void waitAndClickElement(String locatorType, String locatorValue) {
		waitForVisibilityOfElement(locatorType, locatorValue);
		Log.info("Cliked on " + getTextOfElement(locatorType, locatorValue));
		clickWebElement(locatorType, locatorValue);
	}

	public void waitUntilAttributePrasent(By locator, String attribute, String value) {
		wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
	}

	public void clikRefresh(String locatorValue) {
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(By.xpath(locatorValue))));
		pauseTest();
		driver.findElement(By.xpath(locatorValue)).click();
	}

	/**
	 * wait for visibility of element
	 * 
	 * @param locatorType
	 * @param locatorValue
	 * @return
	 */
	public void waitForVisibilityOfElement(String locatorType, String locatorValue) {
		locatorType = locatorType.toUpperCase();
		switch (locatorType) {
		case "ID":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
			break;
		case "NAME":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorValue)));
			break;
		case "XPATH":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
			break;
		case "CLASS":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorValue)));
			break;
		case "LINKTEXT":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locatorValue)));
			break;
		case "PARTIALLINKTEXT":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(locatorValue)));
			break;
		case "CSS":
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorValue)));
			break;
		default:
			Log.warn("Locator is NOT provided Correctly. Please give Valid Locator Type...!!!");
			break;
		}
		pauseTest();
	}

	/**
	 * Wait for the visibility of element using
	 * 
	 * @param locatorType
	 * @param locatorValue
	 * @param timeUnitSec
	 */
	public void waitForInVisibilityOfElement(String locatorType, String locatorValue) {
		locatorType = locatorType.toUpperCase();
		switch (locatorType) {
		case "ID":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(locatorValue)));
			break;
		case "NAME":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(locatorValue)));
			break;
		case "XPATH":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorValue)));
			break;
		case "CLASS":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(locatorValue)));
			break;
		case "LINKTEXT":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText(locatorValue)));
			break;
		case "PARTIALLINKTEXT":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText(locatorValue)));
			break;
		case "CSS":
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locatorValue)));
			break;
		default:
			Log.warn("Locator is NOT provided Correctly. Please give Valid Locator Type...!!!");
			break;
		}
	}

	/**
	 * Wait until attributes value contains some value
	 * 
	 * @param locator
	 * @param attribute
	 * @param attributeValue
	 */
	public void refreshPage() {
		driver.navigate().refresh();
		pauseTest();
	}

	public void navigateBack() {
		driver.navigate().back();
		pauseTest();
	}

	public void ieCertificateIssue() {
		driver.get("javascript:document.getElementById('overridelink').click();");
	}

	// Click an element using Java script by locator id
	public void clickJavaScript(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		driver.executeScript("arguments[0].click();", vElement);
		Log.info("Clicked on :" + getTextOfElement(locatorType, locatorValue));
		// pauseTest();
	}

	// Click an element using Java script by locator id
	public void clickJavaScript(WebElement vElement) {
		driver.executeScript("arguments[0].click();", vElement);
		// pauseTest();
	}

	public void scrollWindow(String locatorType, String locatorValue) {
		WebElement element = findElement(locatorType, locatorValue);
		driver.executeScript("arguments[0].scrollIntoView();", element);
		pauseTest();
		Log.info("Scroll window ...!");
	}

	public String getTextJavaScript(String locatorType, String locatorValue) {
		WebElement element = findElement(locatorType, locatorValue);
		String text = (String) driver.executeScript("return arguments[0].innerHTML;", element);
		return text.trim();
	}

	public void setTopBarVisibale(String locatorType, String locatorValue) {
		WebElement element = findElement(locatorType, locatorValue);
		driver.executeScript("arguments[0].style.height='auto'; arguments[0].style.visibility='visible';", element);

	}

	// This method will return the date iDays after the current
	// date(iDays+Current
	// Date) and date format will be sFormat
	@SuppressWarnings("static-access")
	public String getRequiredDate(int iDays, String sFormat) {

		String sDate = null;
		try {
			Date date = new Date();
			SimpleDateFormat s;
			s = new SimpleDateFormat(sFormat);
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
			calendar.add(calendar.DAY_OF_MONTH, iDays);
			date = calendar.getTime();
			if (date.toString().contains("Sat")) {
				iDays = iDays + 2;
			} else if (date.toString().contains("Sun")) {
				iDays = iDays + 1;
			}

			calendar = Calendar.getInstance();
			date = calendar.getTime();
			calendar.add(calendar.DAY_OF_MONTH, iDays);
			date = calendar.getTime();
			System.out.println(s.format(date));
			sDate = s.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			System.out.println("Failed to return date as per expected format." + e.getMessage());
		}
		return sDate;
	}

	public void catchException(Exception e) {
		e.printStackTrace();
		Log.error(e.toString());
		Log.error("failed with exception: " + e.getMessage());
	}

	public void takeScreenShot() {
		String filePath = "/img";
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filePath + "/test.png"));
			System.out.println("***Placed screen shot in " + filePath + " ***");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// To select an element in dropdown list public WebElement
	public WebElement getSelectedValInDropDown(String locatorType, String locatorVal) {
		WebElement selectElement = findElement(locatorType, locatorVal);
		Select oSelect = new Select(selectElement);
		return oSelect.getFirstSelectedOption();
	}

	// Find a text and replace with user input
	public String findAndReplaceText(String element, String toReplace, String valToreplace) {
		return element.replace(toReplace, valToreplace);
	}

	// To check whether the checkbox is selected or not
	public boolean isCheckBoxSelectedByXpathReplace(String elementXpath, String eleToReplace, String replaceVal) {
		return findWebElementByXpathReplace(elementXpath, eleToReplace, replaceVal).isSelected();
	}

	// To select an element in dropdown list
	public void selectElementInDropDownByXpathReplace(String xPathSelect, String valToReplace, String replaceWith,
			String sValue) {
		WebElement selectElement = findWebElementByXpathReplace(xPathSelect, valToReplace, replaceWith);
		Select oSelect = new Select(selectElement);
		oSelect.selectByVisibleText(sValue);
	}

	// To select an element in dropdown list
	public WebElement getSelectedValInDropDownXpathReplace(String xPathSelect, String valToReplace,
			String replaceWith) {
		WebElement selectElement = findWebElementByXpathReplace(xPathSelect, valToReplace, replaceWith);
		Select oSelect = new Select(selectElement);
		return oSelect.getFirstSelectedOption();
	}

	// Log 4j
	public void configureLog4jPropertiesFile() {

		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		String jvmName = bean.getName();
		long pid = Long.valueOf(jvmName.split("@")[0]);

		System.setProperty("logfile.name", System.getProperty("user.dir") + File.separator + "Logs" + File.separator
				+ new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date()) + "_" + pid + "_Test_Logs.log");

		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator + "libs" + File.separator + "log4j.properties");

		Log.info("***  ******  ******  ******  ***");
		Log.info("***  ******  ******  ******  ***");
		Log.info("Host Name : " + System.getenv("COMPUTERNAME"));
		Log.info("User Name : " + System.getProperty("user.name"));
		Log.info("Operating System : " + System.getProperty("os.name"));
		Log.info("User Directory: " + System.getProperty("user.dir"));
		Log.info("***  ******  ******  ******  ***");
		Log.info("***  ******  ******  ******  ***");
		removeLogFile();
	}

	/**
	 * Remove log file if there are more then 10 log files in the folders
	 */
	public void removeLogFile() {
		File directory = new File(System.getProperty("user.dir") + File.separator + "Logs");
		int fileCount = directory.list().length;
		// System.out.println("File Count:" + fileCount);

		List<String> listOfFiles = new ArrayList<>();
		String files[] = directory.list();
		if (fileCount > 10) {

			for (int i = 0; i < files.length; i++) {
				listOfFiles.add(files[i]);
				listOfFiles.get(0);
				File file = new File(directory + File.separator + listOfFiles.get(0));

				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
					break;
				}
			}

		} else {
			System.out.println("Log files are not more than 10");
		}
	}

	/**
	 * Extract data from string seperated with |
	 * 
	 * @param data
	 * @return
	 */
	public String[] extractData(String data) {
		return data != null ? data.split("\\|") : new String[] {};
	}

	public void closeBrower() {
		driver.close();
	}

	/**
	 * Capture screenShot
	 * 
	 * @param driver
	 * @param fileName
	 * @throws IOException
	 */
	public void captureScreenShot(WebDriver driver, String fileName) throws IOException {
		File imageFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		GregorianCalendar gcalendar = new GregorianCalendar();

		String failureImageFileName = SCREENSHOTS_DIRECTORY + File.separator + "Screenshots" + File.separator
				+ GLOBAL_DATE_FORMAT.format(new java.util.Date()) + File.separator
				+ formatInteger(gcalendar.get(Calendar.HOUR)) + "-" + formatInteger(gcalendar.get(Calendar.MINUTE))
				+ "_" + gcalendar.getTimeInMillis() + "_" + fileName + ".png";

		File failureImageFile = new File(failureImageFileName);
		FileUtils.moveFile(imageFile, failureImageFile);
		screenshots.add(failureImageFile);
		Log.error("Something went wrong. Check Screenshot.");
	}

	/**
	 * Dependent method to screenshot
	 * 
	 * @param input
	 * @return
	 */
	public String formatInteger(int input) {
		return String.format("%02d", input);
	}

	/**
	 * Wait until DOM is ready
	 */
	public void waitUntilPageLoad() {
		wait.until(webDriver -> driver.executeScript("return document.readyState", "loaded")
				|| driver.executeScript("return document.readyState", "complete")
				|| driver.executeScript(" return jQuery.active", "0"));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	/**
	 * Returns the list of webElement
	 * 
	 * @param xpath
	 * @return
	 */
	public List<WebElement> getListOfElements(String xpath) {
		List<WebElement> element = driver.findElements(By.xpath(xpath));
		return element;
	}

	public void setDropdownValueEqualIgnoreCase(By fieldId, String fieldValue) {
		Select dropDown = new Select(driver.findElement(fieldId));
		int index = 0;
		for (WebElement option : dropDown.getOptions()) {
			if (option.getText().equalsIgnoreCase(fieldValue))
				break;
			index++;
		}
		dropDown.selectByIndex(index);
	}

	public void refreshWindow() {
		driver.navigate().refresh();
	}

	/**
	 * Wait until the element is dissapers
	 * 
	 * @param elementXpath
	 * @return
	 */
	public boolean waitUntilElementNotDisplayed(String elementXpath) {
		try {
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

			boolean present = wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
					.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(elementXpath)));
			return present;
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}
	}

	/**
	 * Click displayed element
	 * 
	 * @param locatorXpath
	 */
	public void clickMoreThanOneWebElement(String locatorXpath) {
		List<WebElement> ele = getListOfElements(locatorXpath);
		for (int i = 0; i < ele.size(); i++) {
			try {
				ele.get(i).click();
				Log.info("Clicked on " + ele.get(i).getText().trim());
				break;
			} catch (Exception e) {
				// Log.info("error: " + e);
			}

		}
	}

	/***
	 * Get all the options from the select drop down
	 * 
	 * @param locatorType
	 * @param locatorValue
	 * @return
	 */
	public List<String> getAllOptionFromSelect(String locatorType, String locatorValue) {
		List<String> options = new ArrayList<String>();
		Select dropdown = new Select(findElement(locatorType, locatorValue));
		List<WebElement> dropDownOption = dropdown.getOptions();
		for (WebElement webElement : dropDownOption) {
			options.add(webElement.getText());
		}
		return options;
	}

	/**
	 * Cancel PDF in browser download window
	 */
	public void cancelPDF() {
		((JavascriptExecutor) driver).executeScript(
				"return document.querySelector('print-preview-app').shadowRoot.querySelector('print-preview-sidebar').shadowRoot.querySelector('print-preview-destination-settings').shadowRoot.querySelector('cr-button.cancel-button').click();");
		Log.info("CANCEL PDF Downlaod In print APP");
	}

	public void savePDF() {
		((JavascriptExecutor) driver).executeScript(
				"return document.querySelector('print-preview-app').shadowRoot.querySelector('print-preview-sidebar').shadowRoot.querySelector('print-preview-button-strip').shadowRoot.querySelector('cr-button.action-button').click();");
		Log.info("SAVE PDF Downlaod In print APP");
	}

	public boolean isEnabled(String locatorType, String locatorValue) {
		try {
			findElement(locatorType, locatorValue).isEnabled();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void switchToNewWindow(String locatorType, String locatorValue) {
		WebElement vElement = findElement(locatorType, locatorValue);
		pauseTest();
		driver.executeScript("window.open(arguments[0])", vElement);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}

	/**
	 * divide integer value and return the round up integer
	 * 
	 * @param num
	 * @param divisor
	 * @return
	 */
	public int divideAndRoundUp(int num, int divisor) {
		if (num == 0 || divisor == 0) {
			return 0;
		}
		int sign = (num > 0 ? 1 : -1) * (divisor > 0 ? 1 : -1);

		if (sign > 0) {
			return (int) ((num + divisor - 1) / divisor);
		} else {
			return (int) (num / divisor);
		}
	}

}
