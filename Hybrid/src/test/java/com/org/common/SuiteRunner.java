package com.org.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.TestNG;

import com.org.CoreUtils.Log;
import com.org.resourceUtils.BrowserReader;
import com.org.resourceUtils.ResourceReader;
import com.org.testlinkUtils.XmlGenerator;

/**
 * BeforRunService - Base class to start automation by clearing reports dir and
 * find out launcher files to for the specific build or release
 */
public class SuiteRunner {

	static XmlGenerator generator;

	protected static Logger log = LogManager.getLogger(SuiteRunner.class
			.getName());

	static String isAutoXml = ResourceReader
			.getSettings(BrowserReader.AUTO_XML);
	static String filesToRun = "";

	public static void main(String[] args) throws Exception {
		try {
			if (isAutoXml.equalsIgnoreCase("Yes")) {
				generator = new XmlGenerator();
				filesToRun = "Auto.xml";
			}
		} catch (Exception e) {
			Log.info("There is a problem with tesstNg - continue without testNg ");
		}

		System.out.println("*******filestorun: " + filesToRun);
		File folderToSearchIn = new File("testLaunchers/"); // for local run

		if (filesToRun.isEmpty()) {
			filesToRun = ResourceReader
					.getSettings(BrowserReader.FILES_TO_RUN);
		}

		System.out.println("*******filestorun: " + filesToRun);
		System.out.println("*******folders to searchin: " + folderToSearchIn);
		List<String> suitefile = new ArrayList<>();

		List<String> fileNames = new ArrayList<>();

		// Split string
		String arrayString[] = filesToRun.split("\\|");
		for (int i = 0; i < arrayString.length; i++) {
			fileNames.add(arrayString[i]);
			Log.info("String added to the string: " + arrayString[i]);
		}

		Log.info("List of suites:" + fileNames);
		Log.info("current directory: " + System.getProperty("user.dir"));
		Log.info("******* final filestorun: " + filesToRun);
		Log.info("*******final folders to searchin: " + folderToSearchIn);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<suite name=\"all suites\">\n"
				+ "    <suite-files>\n");

		// get list of files under testlaunchers directory
		@SuppressWarnings("rawtypes")
		Collection files = FileUtils.listFiles(folderToSearchIn,
				new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
		String finalStringOfFiles = files.toString().substring(0,
				files.toString().length() - 1);
		// System.out.println("*****files list: " + finalStringOfFiles);
		if (filesToRun.equals("all")) {
			for (String file : finalStringOfFiles.split(",")) {
				// System.out.println("&&&&&& file in for loop: " + file);
				if (!file.contains("all.xml")) {
					stringBuilder.append("        <suite-file path=\"./"
							+ file.replace('\\', '/')
									.replace("/testLaunchers/", "")
									.substring(2) + "\" />\n");
				}
			}
		} else {
			for (String fileToRun : filesToRun.split("\\|")) {
				for (String file : finalStringOfFiles.split(",")) {
					// System.out.println("&&&&&& file in for loop: " + file);
					if (file.contains(fileToRun)) {
						String[] temp = file.split("testLaunchers");
						suitefile.add(file.trim());
						stringBuilder.append("        <suite-file path=\"./"
								+ temp[1].replace('\\', '/').substring(1)
								+ "\" />\n");
					}
				}
			}
		}

		stringBuilder.append("    </suite-files>\n" + "</suite>");

		File file = new File("testLaunchers/all.xml"); // for local
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file),
				Charset.forName("UTF-8"))) {
			writer.write(stringBuilder.toString());
		}
		Log.info("files to run for the execution: " + filesToRun);
		List<String> suiteToExecute = new ArrayList<>();
		suiteToExecute.add("testLaunchers\\all.xml");
		TestNG testng = new TestNG();
		System.out.println("Suites: " + suitefile);
		testng.setTestSuites(suitefile);
		testng.run();
	}

}
