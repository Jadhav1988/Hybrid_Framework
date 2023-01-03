package com.org.driver;

import static java.lang.Thread.currentThread;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class WebDriverRunner {
	protected static Map<Long, WebDriver> THREAD_WEB_DRIVER = new ConcurrentHashMap<Long, WebDriver>(4);
	protected static Collection<Thread> ALL_WEB_DRIVERS_THREADS = new ConcurrentLinkedQueue<Thread>();

	public static void setWebDriver(){
		
	}
	public static WebDriver getWebDriver(){
		return gettingWebDriver();
	}
	
	public static WebDriver settingWebDriver(WebDriver webDriver) {
		THREAD_WEB_DRIVER.put(currentThread().getId(), webDriver);
		return webDriver;
	}
	public static WebDriver gettingWebDriver() {
		WebDriver webDriver = THREAD_WEB_DRIVER.get(currentThread().getId());
		return webDriver != null ? webDriver : settingWebDriver(WebDriverFactory.getWebDriverByType());
	}
	protected static void closeWebDriver(Thread thread) {
		ALL_WEB_DRIVERS_THREADS.remove(thread);
		WebDriver webdriver = THREAD_WEB_DRIVER.remove(thread.getId());
		if (webdriver != null) {
			System.out.println(" === CLOSE WEBDRIVER: " + thread.getId() + " -> " + webdriver);
			try {
				webdriver.quit();
			} catch (UnreachableBrowserException ignored) {
				// It happens for Firefox. It's ok: browser is already closed.
			}  
		}
	}
	public static void closingWebDriver() {
		closeWebDriver(currentThread());
	}
}

