package com.org.Report;

import java.io.File;

import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.ResourceReader;
import com.org.resourceUtils.TestLinkReader;

public class ExtentManager {

	final static String BROWSER_TYPE = ResourceReader.getSettings(BrowserReader.BROWSER);
	final static String PROJECT_NAME = ResourceReader.getTestLinkSettings(TestLinkReader.testLinkProjectName);
	final static String PLAN_NAME = ResourceReader.getTestLinkSettings(TestLinkReader.testLinkPlanName);

	private static ExtentReports extent;
	private static String reportFileName = PROJECT_NAME + "_Automation_Report" + ".html";
	private static String fileSeperator = System.getProperty("file.separator");
	private static String reportFilepath = System.getProperty("user.dir") + fileSeperator + "reports";
	private static String reportFileLocation = reportFilepath + fileSeperator + reportFileName;

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance();
		return extent;
	}

	/**
	 * Create an extent report instance
	 * 
	 * @return
	 */
	public static ExtentReports createInstance() {
		String fileName = getReportPath(reportFilepath);

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);

		// htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		// htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(reportFileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(reportFileName);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		// Set environment details
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Host Name", System.getenv("COMPUTERNAME"));
		extent.setSystemInfo("Browser", BROWSER_TYPE);
		extent.setSystemInfo("Application Under Test ", PROJECT_NAME);
		extent.setSystemInfo("Types of Testing ", PLAN_NAME);

		return extent;
	}

	/**
	 * Create the report path
	 * 
	 * @param path
	 * @return
	 */
	private static String getReportPath(String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				System.out.println("Directory: " + path + " is created!");
				return reportFileLocation;
			} else {
				System.out.println("Failed to create directory: " + path);
				return System.getProperty("user.dir");
			}
		} else {
			System.out.println("Directory already exists: " + path);
		}
		return reportFileLocation;
	}

	/**
	 * Get formated testID to update test in testlink
	 * 
	 * @param result
	 * @return
	 */
	public static String getTestId(ITestResult result) {
		String formatedTestId = null;
		if (result.getParameters().length > 0) {
			formatedTestId = (String) result.getParameters()[1];
			if (formatedTestId.contains("_")) {
				formatedTestId = formatedTestId.replace("_", "-");
			}
			return formatedTestId.toUpperCase();
		} else {
			String methodName = result.getMethod().getMethodName();
			if (methodName.contains("_")) {
				String testMethod[] = methodName.split("_");
				formatedTestId = testMethod[0] + "-" + testMethod[1];
				return formatedTestId.toUpperCase();
			} else if (methodName.contains("-")) {
				String testMethod[] = methodName.split("-");
				formatedTestId = testMethod[0] + "-" + testMethod[1];
				return formatedTestId.toUpperCase();
			}

		}
		return formatedTestId;
	}

}