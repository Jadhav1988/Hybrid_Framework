package com.org.testlinkUtils;

import org.testng.ITestResult;

public class RetryFailedTestCases{

	private int retryCnt = 0;
	private int maxRetryCnt = 2;

	public boolean retry(ITestResult result) {
		if (retryCnt < maxRetryCnt) {
			System.out.println("Retrying " + result.getName()
					+ " again and the count is " + (retryCnt + 1));
			retryCnt++;
			return true;
		}
		return false;
	}

}
