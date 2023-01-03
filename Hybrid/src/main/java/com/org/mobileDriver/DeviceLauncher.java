package com.org.mobileDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.org.driver.BaseWebDriver;
import com.org.resourceUtils.ResourceReader;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

/**
 * Created by Jyothsna
 * To Launch Appium Driver for Android device.
 **/
public class DeviceLauncher extends BaseWebDriver{

	// Get all the required inputs from mobile.properties file

	private static String dType = ResourceReader.getMobileSettings("deviceType");
	private static String pName = ResourceReader.getMobileSettings("platformName");
	private static String pVersion = ResourceReader.getMobileSettings("platformVersion");
	private static String dName = ResourceReader.getMobileSettings("deviceName");
	private static String apkName = ResourceReader.getMobileSettings("appName");
	private static String ipAddress = ResourceReader.getMobileSettings("ipaddress");
	private static String portNo = ResourceReader.getMobileSettings("appiumPort");

	static File classpathRoot = new File(System.getProperty("user.dir"));
	static File appDir = new File(classpathRoot, "/");
	static File app = new File(appDir, apkName);
	WebDriver driver = BaseWebDriver.getWebDriver();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void initDriver(){
		/* Start APPIUM SERVER
			try {
				startAppiumServer();
			} catch ( IOException | InterruptedException e1) {
				e1.printStackTrace();
				return null;
			}
			
			
		 */	
		//WebDriver driver = BaseWebDriver.getWebDriver();
		//set capabilities for driver
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("automationName", "Appium");			
		capabilities.setCapability("platformName", pName);
		capabilities.setCapability("platformVersion", pVersion);
		capabilities.setCapability("deviceName",dName);
		capabilities.setCapability("app", app.getAbsolutePath());
		if (dType.equalsIgnoreCase("android")){
			try {
				driver = new AndroidDriver(new URL("http://" + ipAddress + ":" + portNo + "/wd/hub")  , capabilities);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				driver = null;
			}
		}else if(dType.equalsIgnoreCase("ios")){
			capabilities.setCapability("waitForAppScript", "$.delay(8000); $.acceptAlert(); true;");
			try {
				driver = new IOSDriver(new URL("http://" + ipAddress + ":" + portNo + "/wd/hub")  , capabilities);

			} catch (MalformedURLException e) {
				e.printStackTrace();
				driver = null;
			}
		}else{
			System.out.println("driver not found");
			driver = null;
		}
	}

	// method to START APPIUM SERVER
	@SuppressWarnings("unused")
	private static void startAppiumServer() throws ExecuteException, IOException, InterruptedException{
		stopAppiumServer();		
		org.apache.commons.exec.CommandLine command = new org.apache.commons.exec.CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
		command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);
		command.addArgument("--address", false);  
		command.addArgument(ipAddress);  
		command.addArgument("--port", false);  
		command.addArgument(portNo);  
		command.addArgument("--session-override", true);
		command.addArgument("--full-reset", false); 
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.execute(command, resultHandler);
		Thread.sleep(5000);  

		System.out.println("Appium server started");  
	}

	// method to stop APPIUM SERVER
	public static void stopAppiumServer() throws IOException{
		String[] command = {"/usr/bin/killall","-KILL","node"};
		Runtime.getRuntime().exec(command);
		System.out.println("Appium Server stopped");
	}


}
