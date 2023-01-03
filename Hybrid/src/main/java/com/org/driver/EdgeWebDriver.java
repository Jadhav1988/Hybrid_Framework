package com.org.driver;

import java.io.File;
import java.util.HashMap;

import com.microsoft.edge.seleniumtools.EdgeDriver;
import com.microsoft.edge.seleniumtools.EdgeOptions;
import com.org.CoreUtils.Log;
import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.ResourceReader;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * EdgeWebDriver
 */

public class EdgeWebDriver extends BaseWebDriver {
	// ~ Methods
	// --------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initDriver() {
		System.out.println("launching Microsoft Edge browser");
		if(runOnvCommander.equalsIgnoreCase("Yes"))
			System.setProperty(BrowserReader.WEBDRIVER_EDGE_DRIVER,ResourceReader.getSettings(BrowserReader.EDGE_PATH_WIN));
		  else
		  {
			  Log.info("Initializing the browser's driver using Webdriver Manager");
			  WebDriverManager.edgedriver().setup();
		  }
		
		EdgeOptions edgeOptions=new EdgeOptions();
		 HashMap<String, Object> edgePrefs = new HashMap<>();
		 edgePrefs.put("download.default_directory",System.getProperty("user.dir") + File.separator + "libs"+File.separator+"Downloads");
		 edgeOptions.setExperimentalOption("prefs", edgePrefs);
		//System.setProperty(BrowserReader.WEBDRIVER_EDGE_DRIVER,ResourceReader.getSettings(BrowserReader.EDGE_PATH_WIN));
		
		driver.set(new EdgeDriver(edgeOptions));
		// driver = new EdgeDriver();
	    // driver.manage().deleteAllCookies();
	    // driver.manage().window().maximize();
	}

}
