package com.org.driver;

import java.io.IOException;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * IEWebDriver
 */

public class IEWebDriver extends BaseWebDriver
{
  //~ Methods --------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override public void initDriver()
  {
    //System.setProperty(BrowserReader.WEBDRIVER_IE_DRIVER, ResourceReader.getSettings(BrowserReader.IE_PATH));
    
    WebDriverManager.iedriver().setup();

    final DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
    ieCapabilities.setCapability("ie.setProxyByServer", true);
    ieCapabilities.setCapability("enablePersistentHover", true);
    ieCapabilities.setCapability("ignoreZoomSetting", true);
    ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    ieCapabilities.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

    final InternetExplorerOptions options = new InternetExplorerOptions(ieCapabilities);
    try
    {
      Runtime.getRuntime().exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 255");
    }
    catch (final IOException e)
    {
    }
    driver.set(new InternetExplorerDriver(options));
    //driver = new InternetExplorerDriver(options);
    //driver.manage().deleteAllCookies();
    //driver.manage().window().maximize();
  }
}
