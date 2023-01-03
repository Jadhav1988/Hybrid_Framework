package com.org.testlinkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.org.excelUtils.ExcelUtils;
import com.org.resourceUtils.ResourceReader;
import com.org.resourceUtils.TestLinkReader;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.model.Attachment;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;

/**
 * UpdateTestLinkTests
 */
public class UpdateTestLinkTests {

	final String apiKey = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkDevKey);
	final String SERVER_URL = ResourceReader
			.getTestLinkSettings(TestLinkReader.testLinkURL);

	URL url;
	int testCaseExternalId;
	int projectID;

	/**
	 * Sets the test case keyword as regression
	 * 
	 * @param xlsxFilePath
	 * @throws Exception
	 */
	public void setTestCaseKeywordAsRegression(String fileName, String sheetName)
			throws Exception {
		url = new URL(SERVER_URL);
		TestLinkAPI testlinkAPI = new TestLinkAPI(url, apiKey);

		ExcelUtils.setExcelFile("TestLink_Execution.xlsx", "Automation_Sheet");
		int usedRows = ExcelUtils.getRowUsed();

		for (int i = 1; i <= usedRows; i++) {
			String vProjectName = ExcelUtils.getCellData(i, 1);
			String vExternalID = ExcelUtils.getCellData(i, 2);
			List<String> vCustFieldValue = new ArrayList<String>();
			vCustFieldValue.add(ExcelUtils.getCellData(i, 4));
			testCaseExternalId = Integer.parseInt(vExternalID.split("-")
					.clone()[1]);

			TestProject[] testProjects = testlinkAPI.getProjects();
			for (int j = 0; j < testProjects.length; j++) {
				String tempProjectName = testProjects[j].getName();
				if (tempProjectName.equalsIgnoreCase(vProjectName)) {
					projectID = testProjects[j].getId();
				}
			}

			TestCase vTestCase = testlinkAPI.getTestCaseByExternalId(
					vExternalID, null);
			int vTestCaseID = vTestCase.getId();

			String[] arrExId = vExternalID.split("-");
			int vExternalIDInt = Integer.parseInt(arrExId[1]);
			Attachment[] tempAttach = testlinkAPI.getTestCaseAttachments(
					vTestCaseID, vExternalIDInt);

			FileOutputStream f = new FileOutputStream(new File("example.html"));
			ObjectOutputStream object = new ObjectOutputStream(f);
			for (Attachment at : tempAttach) {
				object.writeObject(at.getContent().toString());
			}
			f.close();
			System.out.println(vExternalID
					+ " - Test Case Updated Successfully to Updated...!!!");

		}

	}

	/**
	 * Updates the given test case as AUTOMATED in TestLink, TestcaseID in ->>
	 * 2nd row of given sheet ProjectName in ->> 1st row in given sheet
	 * 
	 * @param fileName
	 * @param sheetName
	 * @throws Exception
	 */
	public void setTestCaseExecutionTypeAsAutomated(String fileName,
			String sheetName) throws Exception {
		url = new URL(SERVER_URL);
		TestLinkAPI testlinkAPI = new TestLinkAPI(url, apiKey);

		ExcelUtils.setExcelFile(fileName, sheetName);
		int usedRows = ExcelUtils.getRowUsed();
		for (int i = 1; i <= usedRows; i++) {
			String projectName = ExcelUtils.getCellData(i, 1);
			String testID = ExcelUtils.getCellData(i, 2);
			testCaseExternalId = Integer.parseInt(testID.split("-").clone()[1]);

			TestProject[] testProjects = testlinkAPI.getProjects();
			for (int j = 0; j < testProjects.length; j++) {
				String tempProjectName = testProjects[j].getName();
				if (tempProjectName.equalsIgnoreCase(projectName)) {
					projectID = testProjects[j].getId();
				}
			}

			TestCase vTestCase = testlinkAPI.getTestCaseByExternalId(testID,
					null);
			int testCaseInternalID = vTestCase.getId();
			testlinkAPI.setTestCaseExecutionType(projectID, testCaseInternalID,
					testCaseExternalId, 1, ExecutionType.AUTOMATED);
			System.out.println(projectName + " --> " + testID
					+ " - Test Updated As Automated");
		}
		System.out.println("Test case updation completed !!! ");

	}

}