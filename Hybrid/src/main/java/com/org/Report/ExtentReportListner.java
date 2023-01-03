package com.org.Report;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.org.CoreUtils.Log;
import com.org.testlinkUtils.TestLink;

public class ExtentReportListner implements ITestListener, ISuiteListener {

	EmailConfig email;
	TestLink testLink = new TestLink();
	GraphUtils graph = new GraphUtils();

	long startTime;
	long endTime;
	long totalTime;
	int passCount;
	int failCount;
	int skipCount;
	Date startDate;
	Date endDate;

	public void onStart(ITestContext context) {
		Log.info("*** Test class " + context.getName() + " Started ***");
		startDate = context.getStartDate();
		startTime = System.currentTimeMillis();
	}

	public void onFinish(ISuite iTestContext) {
		Log.info(("*** Test Suite " + iTestContext.getName() + " Ending ***"));
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();

		String piechat = graph.drawPieChart(passCount, failCount, skipCount, "Pie chart");

		String destinationPath = System.getProperty("user.dir") + File.separator + "reports" + File.separator
				+ "ResultPieChart.png";

		graph.saveImage(piechat, destinationPath);

		try {
			totalTime = endTime - startTime;
			email = new EmailConfig(startDate, endDate, getTotalTestCases(), passCount, failCount,
					formatToDigitalClock(totalTime));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onTestStart(ITestResult result) {
		Log.info(("*** Running test method " + result.getMethod().getMethodName() + "..."));
		ExtentTestManager.startTest(result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		Log.info(ExtentManager.getTestId(result) + " Passed...");
		ExtentTestManager.getTest().log(Status.PASS, "Test passed");

		try {
			testLink.updateTestCaseTestLink(ExtentManager.getTestId(result), "Pass");
		} catch (Exception e) {
			Log.error("Issue with test link" + ExtentManager.getTestId(result) + " Id not updated");
		}
		passCount++;
	}

	public void onTestFailure(ITestResult result) {
		Log.error(ExtentManager.getTestId(result) + " Failed...");
		ExtentTestManager.getTest().log(Status.FAIL, result.getThrowable());

		/*
		 * WebDriver driver = BaseWebDriver.getWebDriver(); String base64Screenshot =
		 * "data:image/png;base64," + ((TakesScreenshot)
		 * driver).getScreenshotAs(OutputType.BASE64);
		 * ExtentTestManager.getTest().addScreenCaptureFromBase64String(
		 * base64Screenshot);
		 */

		try {
			testLink.updateTestCaseTestLink(ExtentManager.getTestId(result), "Fail");
		} catch (Exception e) {
			Log.error("Issue with test link" + ExtentManager.getTestId(result) + " Id not updated");
		}
		failCount++;
	}

	public void onTestSkipped(ITestResult result) {
		Log.info(ExtentManager.getTestId(result) + " Skipped...");
		ExtentTestManager.getTest().log(Status.SKIP, result.getThrowable());

		skipCount++;
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
	}

	@Override
	public void onFinish(ITestContext context) {
		Log.info("** Test class " + context.getName() + " execution finshed **");
		endDate = context.getEndDate();
		endTime = System.currentTimeMillis();
	}

	@Override
	public void onStart(ISuite suite) {
		Log.info("*** Test Suite " + suite.getName() + " Started ***");
	}

	public int getTotalTestCases() {
		return passCount + failCount + skipCount;
	}

	/**
	 * get the time in hh:mm:ss format
	 * 
	 * @param miliSeconds
	 * @return
	 */
	private String formatToDigitalClock(long miliSeconds) {
		int hours = (int) (TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24);
		int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60);
		int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60);
		if (hours > 0)
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		else if (minutes > 0)
			return String.format("00:%02d:%02d", minutes, seconds);
		else if (seconds > 0)
			return String.format("00:00:%02d", seconds);
		else
			return "00:00:00";
	}

}