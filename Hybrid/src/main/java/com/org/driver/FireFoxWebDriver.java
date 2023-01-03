package com.org.driver;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * FireFoxWebDriver
 */
public class FireFoxWebDriver extends BaseWebDriver
{
  //~ Methods --------------------------------------------------------------------------------------

  /**
   * this is to initialize the firefox driver
   */

  @Override public void initDriver()
  {
    //System.setProperty(BrowserReader.WEBDRIVER_FIREFOX_DRIVER, ResourceReader.getSettings(BrowserReader.FIREFOX_PATH));
	  WebDriverManager.firefoxdriver().setup();
	  final FirefoxProfile fProfile = new FirefoxProfile();
    fProfile.setPreference("plugin.default.state", 2);
//    DesiredCapabilities caps = DesiredCapabilities.firefox();
//    caps.setCapability(FirefoxDriver.PROFILE, fProfile);
//    caps.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
    final FirefoxOptions options = new FirefoxOptions();
    options.setCapability(FirefoxDriver.PROFILE, fProfile);
    options.setCapability(FirefoxDriver.PROFILE, fProfile);
    driver.set(new FirefoxDriver(options));
  }
}
