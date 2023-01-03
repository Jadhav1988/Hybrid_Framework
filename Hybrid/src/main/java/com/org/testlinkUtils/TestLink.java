package com.org.testlinkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.org.CoreUtils.Log;
import com.org.resourceUtils.ResourceReader;
import com.org.resourceUtils.TestLinkReader;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.Execution;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

public class TestLink {

	public String projectName = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkProjectName);
	public String planName = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkPlanName);
	public String buildName = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkBuildName);
	public String url = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkURL);
	public String devKey = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkDevKey);
	public String keyword = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkKeyword);
	/**
	 * Get connection using uri and devKey
	 * 
	 * @param url
	 * @param devKey
	 * @return
	 * @throws TestLinkAPIException
	 * @throws MalformedURLException
	 */
	public TestLinkAPI getConnection(String url, String devKey)
			throws TestLinkAPIException, MalformedURLException {
		TestLinkAPI api = new TestLinkAPI(new URL(url), devKey);
		Log.info("Connection made " + url);
		return api;
	}

	/**
	 * Get the testProject
	 * 
	 * @param projectName
	 * @param api
	 * @return
	 */
	public TestProject getProject(String projectName, TestLinkAPI api) {
		TestProject project = api.getTestProjectByName(projectName);
		return project;
	}

	/**
	 * Get test plan
	 * 
	 * @param api
	 * @param projectName
	 * @param planName
	 * @return
	 */
	public TestPlan getplan(TestLinkAPI api, String projectName, String planName) {
		TestPlan plan = api.getTestPlanByName(planName, projectName);
		return plan;
	}

	/**
	 * get the build id if the given build found
	 * 
	 * @param api
	 * @param testPlanId
	 * @param buildName
	 * @return
	 * @throws Exception
	 */
	public int getBuildIdIfMatched(TestLinkAPI api, int testPlanId,
			String buildName) {
		Build[] build = api.getBuildsForTestPlan(testPlanId);
		for (int i = 0; i < build.length; i++) {
			if (buildName.equalsIgnoreCase(build[i].getName().toLowerCase()
					.trim())) {
				Log.info(" Build Found in test Link: " + build[i].getName());
				return build[i].getId();
			}
		}
		return 0;
	}

	/**
	 * Get the latest build
	 * 
	 * @param api
	 * @param testPlanId
	 * @return
	 */
	public Build getLatestBuildFromTestPlan(TestLinkAPI api, int testPlanId) {
		Build build = api.getLatestBuildForTestPlan(testPlanId);
		return build;
	}

	/**
	 * Get test suite
	 * 
	 * @param api
	 * @param testPlanId
	 * @return
	 */
	public TestSuite[] getTestSuite(TestLinkAPI api, int testPlanId) {
		TestSuite[] suite = api.getTestSuitesForTestPlan(testPlanId);
		return suite;
	}

	/**
	 * Get automated test cases from the build
	 * 
	 * @param api
	 * @param testPlanId
	 * @param buildId
	 * @return
	 */
	public List<String> getAutomatedTestCasesExtIdSuite(TestLinkAPI api,
			int testPlanId, int buildId, String keyWord) {
		List<String> automatedTestCases = new ArrayList<>();
		TestCase[] testcases = api.getTestCasesForTestPlan(testPlanId, null,
				buildId, null, keyWord, null, null, null,
				ExecutionType.AUTOMATED, null, TestCaseDetails.FULL);
		for (int i = 0; i < testcases.length; i++) {
			if (testcases[i].getExecutionType() != null
					&& testcases[i].getExecutionType().getValue() == 2) {
				automatedTestCases.add(testcases[i].getFullExternalId());

			}
		}

		return automatedTestCases;
	}

	public List<Integer> getAutomatedTestCaseID(TestLinkAPI api,
			int testPlanId, int buildId) {
		List<Integer> automatedTestCases = new ArrayList<>();
		TestCase[] testcases = api.getTestCasesForTestPlan(testPlanId, null,
				buildId, null, null, null, null, null, ExecutionType.AUTOMATED,
				null, TestCaseDetails.FULL);
		for (int i = 0; i < testcases.length; i++) {
			if (testcases[i].getExecutionType() != null
					&& testcases[i].getExecutionType().getValue() == 2) {
				automatedTestCases.add(testcases[i].getId());

			}
		}

		return automatedTestCases;
	}

	public void updateTestCaseTestLink(String extID)
			throws TestLinkAPIException, MalformedURLException {

		TestLinkAPI api = new TestLinkAPI(new URL(url), devKey);
		TestPlan plan = getplan(api, projectName, planName);
		String vTempExtId = extID.substring(0, (extID.length() - 2));
		TestCase vTestCaseTestLink = api.getTestCaseByExternalId(vTempExtId,
				null);

		String[] vextIdl = extID.split("-");
		if (vextIdl[2].equalsIgnoreCase("P")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.PASSED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);
		} else if (vextIdl[2].equalsIgnoreCase("F")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.FAILED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);
		} else if (vextIdl[2].equalsIgnoreCase("S")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.BLOCKED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);

		}

	}

	public void updateTestCaseTestLink(String extID, String testResult)
			throws TestLinkAPIException, MalformedURLException {

		TestLinkAPI api = new TestLinkAPI(new URL(url), devKey);
		TestPlan plan = getplan(api, projectName, planName);
		TestCase vTestCaseTestLink = api.getTestCaseByExternalId(extID, null);

		if (testResult.equalsIgnoreCase("Pass")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.PASSED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);
			Log.info("Updated test " + extID + " in test link");
		} else if (testResult.equalsIgnoreCase("Fail")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.FAILED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);
			Log.info("Updated test " + extID + " in test link");
		} else if (testResult.equalsIgnoreCase("Skip")) {
			api.setTestCaseExecutionResult(vTestCaseTestLink.getId(), null,
					plan.getId(), ExecutionStatus.BLOCKED, null, buildName,
					"Automation", true, "Bug_test", null, null, null, true);
			Log.info("Updated test " + extID + " in test link");

		}

	}

	public void getFailedTestCases(TestLinkAPI api, int testPlanId,
			int buildId, String keyword) throws TestLinkAPIException,
			MalformedURLException {

		List<String> automatedTest = getAutomatedTestCasesExtIdSuite(api,
				testPlanId, buildId, keyword);

		for (int i = 0; i < automatedTest.size(); i++) {

			TestCase testCase = api.getTestCaseByExternalId(
					automatedTest.get(i), null);

			String[] strarray = automatedTest.get(i).split("-");

			Execution exe = new Execution();

			if (api.getLastExecutionResult(testPlanId, testCase.getId(),
					Integer.parseInt(strarray[1])) != null
					&& exe.getStatus() == ExecutionStatus.PASSED) {

				System.out.println(api.getLastExecutionResult(testPlanId,
						testCase.getId(), Integer.parseInt(strarray[1])));
			} else {
				System.out.println("****");
			}
		}

	}

	@Test
	public void test() throws TestLinkAPIException, MalformedURLException {

		TestLinkAPI api = new TestLinkAPI(new URL(url), devKey);
		TestPlan plan = getplan(api, projectName, planName);
		int buildId = getBuildIdIfMatched(api, plan.getId(), buildName);

		getFailedTestCases(api, plan.getId(), buildId, keyword);
	}

}
