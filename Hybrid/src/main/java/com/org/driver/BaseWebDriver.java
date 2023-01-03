package com.org.driver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;

import com.org.CoreUtils.Log;
import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.PAUSE_LENGTH;
import com.org.resourceUtils.ResourceReader;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;

/**
 * BaseWebDriver - Base class for the WebDriver
 */

public abstract class BaseWebDriver implements WebDriver {
	// ~ Instance Variables
	// ---------------------------------------------------------------------------

	static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	protected String runOnvCommander = ResourceReader.getSettings(BrowserReader.vCommander);

	// ~ Constructors
	// ---------------------------------------------------------------------------------

	/**
	 * creates and initlaizes the WebDriver.
	 *
	 * @return initlaizes the WebDriver
	 */
	public BaseWebDriver() {
		initDriver();
	}

	// ~ Methods
	// --------------------------------------------------------------------------------------

	/**
	 * Initializes the getWebDriver() according to the browser type. This will only
	 * Initializes the getWebDriver(), but will not launch the getWebDriver().
	 */
	public abstract void initDriver();

	public WebElement findElement(By by) {
		try {
			WebElement element = getWebDriver().findElement(by);
			return element;
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("There is no such webelement:" + by);
		}
	}

	public WebElement findElement(String element) {
		return findElement(By.xpath(element));
	}

	public List<WebElement> findElements(By by) {
		return getWebDriver().findElements(by);
	}

	public List<WebElement> findElements(String elements) {
		return getWebDriver().findElements(By.xpath(elements));
	}

	public WebElement findElementByLocation(int x, int y) {
		return (WebElement) ((JavascriptExecutor) getWebDriver())
				.executeScript("return document.elementFromPoint(" + x + "," + y + ")");
	}

	public void alertHandler() {
		if (isAlertDisplayed()) {
			Alert alert = getWebDriver().switchTo().alert();
			Log.warn("Alert present! " + alert.getText());
			alert.accept();
		}
	}

	public void alertHandler(String name, String btn) {
		if (isAlertDisplayed()) {
			Alert alert = getWebDriver().switchTo().alert();
			Log.warn("Alert present! " + alert.getText());
			String alrtMsg = alert.getText();
			if (alrtMsg.startsWith(name)) {
				if (btn.equalsIgnoreCase("change")) {
					alert.accept();
				} else if (btn.equalsIgnoreCase("Yes")) {
					alert.accept();
				} else {
					alert.dismiss();
				}
			} else {
				alert.dismiss();
			}
		}
	}

	public boolean isAlertDisplayed() {
		try {
			getWebDriver().switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public void alertHandler(boolean code) {
		Alert alert = getWebDriver().switchTo().alert();
		Log.warn("Alert present! " + alert.getText());
		if (code)
			alert.accept();
		else
			alert.dismiss();
	}

	public boolean areElementsVisible(String by) {
		getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			if (findElements(by).size() > 1) {
				for (int i = 0; i < findElements(by).size(); i++) {
					if (findElements(by).get(i).isDisplayed()) {
						System.err.println(by + "by found: " + findElements(by).size());
						return true;
					}
				}
				return false;
			} else
				return false;
		} catch (Exception e) {
			return false;
		} finally {
			getWebDriver().manage().timeouts().implicitlyWait(PAUSE_LENGTH.AJAX.value(), TimeUnit.SECONDS);
		}
	}

	public void close() {
		getWebDriver().close();
	}

	public void click(WebElement element) {
		Actions action = new Actions(getWebDriver());
		action.click(element);
		action.build().perform();
	}

	public void doubleClick(By by) {
		Actions action = new Actions(getWebDriver());
		action.doubleClick(getWebDriver().findElement(by));
		action.build().perform();
	}

	public void dragAndDrop(WebElement pElementName, WebElement pElementName2) {
		Actions builder = new Actions(getWebDriver());
		Action dragAndDrop = builder.clickAndHold(pElementName).moveToElement(pElementName2).release(pElementName2)
				.build();
		dragAndDrop.perform();
	}

	public void dragAndDrop(WebElement pElementName, int x, int y) {
		Actions builder = new Actions(getWebDriver());
		Action dragAndDrop = builder.clickAndHold(pElementName).moveByOffset(x, y).release().build();
		dragAndDrop.perform();
	}

	public void moveByOffset(int x1, int y1) {
		Actions builder = new Actions(getWebDriver());
		Action moveTo = builder.moveByOffset(x1, y1).build();
		moveTo.perform();
	}

	public void clickOnElementPosition(WebElement element, int x, int y) {
		((JavascriptExecutor) getWebDriver()).executeScript("window.scrollTo(" + x + "," + y + ");");
		Actions builder = new Actions(getWebDriver());
		Action click = builder.moveToElement(element, x, y).click().build();
		click.perform();
	}

	public void clickAt(WebElement element, int x, int y) {
		Actions builder = new Actions(getWebDriver());
		Action click = builder.moveToElement(element, x, y).click().build();
		click.perform();
	}

	public void clickAndHold() {
		Actions builder = new Actions(getWebDriver());
		Action clickNHold = builder.clickAndHold().build();
		clickNHold.perform();
	}

	public void executeScript(String script) {
		((JavascriptExecutor) getWebDriver()).executeScript(script);
	}

	public boolean executeScript(String script, String value) {
		return ((JavascriptExecutor) getWebDriver()).executeScript(script).equals(value);
	}

	public Object executeScript(String script, WebElement webElement) {
		return ((JavascriptExecutor) getWebDriver()).executeScript(script, webElement);
	}

	public void pause(int p) {
		try {
			Thread.sleep(p);
		} catch (InterruptedException e) {
		}
	}

	public void get(String url) {
		// Log.info("Go to the URL " + url);
		try {
			getWebDriver().get(url);
		} catch (UnhandledAlertException e) {
			alertHandler();
			get(url);
		}
	}

	public String getCurrentUrl() {
		return getWebDriver().getCurrentUrl();
	}

	public String getAlertText() {
		return getWebDriver().switchTo().alert().getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPageSource() {
		return getWebDriver().getPageSource();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return getWebDriver().getTitle();
	}

	public static WebDriver getWebDriver() {
		return driver.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getWindowHandle() {
		return getWebDriver().getWindowHandle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getWindowHandles() {
		return getWebDriver().getWindowHandles();
	}

	public void hardRefresh() {
		Actions actionObject = new Actions(getWebDriver());
		getWebDriver().manage().deleteAllCookies();
		// webDriver.navigate().refresh();
		actionObject.keyDown(Keys.CONTROL).sendKeys(Keys.F5).keyUp(Keys.CONTROL).perform();
		// webDriver.navigate().refresh();
		getWebDriver().get(getWebDriver().getCurrentUrl() + "?shljapa=0");
	}

	public void clickBackSpace() {
		Actions actionObject = new Actions(getWebDriver());
		actionObject.sendKeys(Keys.BACK_SPACE).perform();
	}

	public void clickEnterButton() {
		Actions actionObject = new Actions(getWebDriver());
		actionObject.sendKeys(Keys.ENTER).perform();
	}

	public void clickBackSpaceLoop(int j) {
		Actions actionObject = new Actions(getWebDriver());
		for (int i = 0; i < j; i++)
			actionObject.sendKeys(Keys.BACK_SPACE).perform();
	}

	public boolean isElementPresent(String by) {
		try {
			Log.info("Find element with locator: " + by);
			getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			List<WebElement> list = findElements(By.xpath(by));
			if (list.size() == 0) {
				Log.info("Can't find an element with locator: " + by);
				return false;
			} else {
				Log.info("Element with locator: " + by + " was found");
				return true;
			}
		} catch (UnhandledAlertException e) {
			alertHandler();
			return isElementPresent(by);
		} finally {
			getWebDriver().manage().timeouts().implicitlyWait(PAUSE_LENGTH.AJAX.value(), TimeUnit.SECONDS);
		}
	}

	public boolean isElementVisible(String by) {
		if (isElementPresent(by)) {
			try {
				getWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				List<WebElement> elements = getWebDriver().findElements(By.xpath(by));
				for (WebElement element : elements) {
					if (element.isDisplayed()) {
						System.out.println("We found " + (elements.indexOf(element) + 1) + " from " + elements.size());
						Log.info("Element with locator: " + by + " was found and visible: " + element.isDisplayed());
						return true;
					}
				}
				Log.info("Element with locator: " + by + " was found BUT NOT VISIBLE");
				return false;

			} catch (StaleElementReferenceException e) {
				Log.error(e.toString());
				return false;
			} finally {
				getWebDriver().manage().timeouts().implicitlyWait(PAUSE_LENGTH.AJAX.value(), TimeUnit.SECONDS);
			}
		}
		return false;
	}

	public void browserBackButtonClick() {
		getWebDriver().navigate().back();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Options manage() {
		return getWebDriver().manage();
	}

	public void mouseHover(WebElement pElementName) {
		mouseHover(pElementName, 3, 3);
	}

	public void mouseHover(WebElement pElementName, int x, int y) {
		Actions builder = new Actions(getWebDriver());
		Action dragAndDrop = builder.moveToElement(pElementName, x, y).build();
		dragAndDrop.perform();
	}

	public void mouseHoverAction(WebElement element) {
		Actions builder = new Actions(getWebDriver());
		Actions hover = builder.moveToElement(element);
		hover.perform();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Navigation navigate() {
		return getWebDriver().navigate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void quit() {
		getWebDriver().quit();
	}

	public void scrollToPosition(int x, int y) {
		executeScript("window.scrollTo(" + x + "," + y + ");");
	}

	public void selectFrame(By by) {
		try {
			WebElement myFrame = getWebDriver().findElement(by);
			getWebDriver().switchTo().frame(myFrame);
		} catch (Exception e) {
			System.err.println("Couldn't switch to: " + by.toString());
		}
	}

	public void selectOriginalContext() {
		getWebDriver().switchTo().defaultContent();
	}

	public void switchWindow(int w) {
		Set<String> set = getWebDriver().getWindowHandles();
		List<String> mainList = new ArrayList<String>();
		mainList.addAll(set);
		if (w != 0)
			getWebDriver().close();
		getWebDriver().switchTo().window(mainList.get(mainList.size() - 1 + w));
	}

	public void selectOption(WebElement select, String option) {
		new Select(select).selectByVisibleText(option);
	}

	public void selectOptionByValue(WebElement select, String value) {
		new Select(select).selectByValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TargetLocator switchTo() {
		return getWebDriver().switchTo();
	}

	public static void setCheckboxValue(WebElement e, boolean b) {
		if (b) {
			if (!e.isSelected()) {
				e.click();
			}
		} else {
			if (e.isSelected()) {
				e.click();
			}
		}
	}

	public void setAttributeByClass(String className, String attribute, String value) {
		executeScript("document.getElementsByClassName('" + className + "')[0].setAttribute('" + attribute + "','"
				+ value + "');");
	}

	public void setAttributeById(String id, String attribute, String value) {
		executeScript("document.getElementById('" + id + "').setAttribute('" + attribute + "','" + value + "');");
	}

	public void dragAndDropHorizontal(int x1, int y1, int distance) {
		dragAndDrop(x1, y1, x1 + distance, y1);
	}

	public void dragAndDropVerical(int x1, int y1, int distance) {
		dragAndDrop(x1, y1, x1, y1 + distance);
	}

	private void dragAndDrop(int x1, int y1, int x2, int y2) {
		Log.warn("We move cursor from " + x1 + ":" + y1 + " to " + x2 + ":" + y2);

		try {
			Robot mouse = new Robot();
			mouse.mouseMove(x1, y1);
			mouse.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			mouse.mouseMove(x2, y2);
			mouse.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			mouse.delay(1000);
			mouse.mouseMove(x1, y1);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void takeScreenShot(String methodName) { String filePath = "/img";
	 * File scrFile =
	 * ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.FILE); //The
	 * below method will save the screen shot in d drive with test method name try {
	 * FileUtils.copyFile(scrFile, new File(filePath+methodName+".png"));
	 * System.out.println("***Placed screen shot in "+filePath+" ***"); } catch
	 * (IOException e) { e.printStackTrace(); } }
	 */

	// Appium
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AppiumDriver<WebElement> getAppiumDriver() {
		return (AppiumDriver) getWebDriver();
	}

	/**
	 * Convenience method for tapping a position on the screen
	 * 
	 * @param fingers  - number of fingers/appendages to tap with
	 * @param x        - x coordinate
	 * @param y        - y coordinate
	 * @param duration - time in milliseconds
	 */
	public void tap(int fingers, int x, int y, int duration) {
		getAppiumDriver().tap(fingers, x, y, duration);
	}

	/**
	 * 
	 * @param fingers
	 * @param element
	 * @param duration
	 */
	public void tap(int fingers, WebElement element, int duration) {
		getAppiumDriver().tap(fingers, element, duration);
	}

	/**
	 * Convenience method for pinching an element on the screen. "pinching" refers
	 * to the action of two appendages pressing the screen and sliding towards each
	 * other.
	 * 
	 * NOTE: This convenience method places the initial touches around the element
	 * at a distance, if this would happen to place one of them off the screen,
	 * appium will return an outOfBounds error. In this case, revert to using the
	 * MultiTouchAction api instead of this method.
	 * 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void pinch(int x, int y) {
		getAppiumDriver().pinch(x, y);
	}

	public void pinch(WebElement element) {
		getAppiumDriver().pinch(element);
	}

	public void zoom(int x, int y) {
		getAppiumDriver().zoom(x, y);
	}

	public void zoom(WebElement element) {
		getAppiumDriver().zoom(element);
	}

	/**
	 * Convenience method for swiping across the screen
	 * 
	 * @param startx   - starting x coordinate
	 * @param starty   - starting y coordinate
	 * @param endx     - ending x coordinate
	 * @param endy     - ending y coordinate
	 * @param duration - amount of time in milliseconds for the entire swipe action
	 *                 to take
	 */
	public void swipe(int startx, int starty, int endx, int endy, int duration) {
		getAppiumDriver().swipe(startx, starty, endx, endy, duration);
	}

	public void scroolToDown() {
		JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile: scroll", scrollObject);
	}

	@SuppressWarnings("rawtypes")
	public void rotateLandScape() {
		WebDriver augmentedDriver = new Augmenter().augment((AppiumDriver) getWebDriver());
		((Rotatable) augmentedDriver).rotate(ScreenOrientation.LANDSCAPE);
	}

	@SuppressWarnings("rawtypes")
	public void rotatePortrait() {
		WebDriver augmentedDriver = new Augmenter().augment((MobileDriver) getWebDriver());
		((Rotatable) augmentedDriver).rotate(ScreenOrientation.PORTRAIT);
	}

	@SuppressWarnings("rawtypes")
	public void PerformLongPress(int x1, int y1) {
		Log.warn("perform long press");
		TouchAction action = new TouchAction((MobileDriver) getWebDriver());
		action.longPress(x1, y1).release().perform();
	}

	@SuppressWarnings("rawtypes")
	public void PerformingLongPress(int x1, int y1, int x2, int y2) {
		Log.warn("perform long press");
		TouchAction action = new TouchAction((MobileDriver) getWebDriver());
		action.longPress(x1, y1).perform().moveTo(x2, y2).perform();
		// action.moveTo(x2, y2);
	}

	@SuppressWarnings("rawtypes")
	public void DragWayPoint(int x1, int y1, int x2, int y2) {
		Log.warn("perform long press");
		TouchAction action = new TouchAction((MobileDriver) getWebDriver());
		action.longPress(x1, y1).moveTo(x2, y2).release().perform();
	}

	@SuppressWarnings("rawtypes")
	public void longPress(String val) {
		WebElement element = getWebDriver().findElement(By.xpath(val));
		TouchAction action = new TouchAction((MobileDriver) getWebDriver());
		action.longPress(element).release().perform();
	}

	@SuppressWarnings("rawtypes")
	public void resetApp() {
		((AppiumDriver) getWebDriver()).resetApp();
	}

}
