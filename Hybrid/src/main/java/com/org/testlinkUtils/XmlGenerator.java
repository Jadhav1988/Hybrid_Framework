package com.org.testlinkUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.org.CoreUtils.Log;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class XmlGenerator {

	TestLink link = new TestLink();
	FileFunctions file = new FileFunctions();

	public XmlGenerator() throws ClassNotFoundException, TestLinkAPIException,
			ParserConfigurationException, IOException, ConfigurationException,
			InterruptedException {
		generateDocument();
	}

	/**
	 * Get all the classes which are present in the given packages
	 * 
	 * @param packageName
	 * @return classes
	 */
	public List<String> getClasses(String packageName) {
		List<String> classNames = new FastClasspathScanner(packageName).scan()
				.getNamesOfAllClasses();
		return classNames;
	}

	/**
	 * Get all the classes and user defined methods from the given package
	 * 
	 * @param packageName
	 * @return classes and user defined method
	 * @throws ClassNotFoundException
	 */
	public ListMultimap<String, String> getClassesAndMethods(
			String packageName, List<String> testLinkCases)
			throws ClassNotFoundException {

		List<String> classes = getClasses(packageName);
		Log.info("List of classes in a packages: " + classes);

		ListMultimap<String, String> multimap = ArrayListMultimap.create();
		for (int i = 0; i < classes.size(); i++) {
			Class<?> classObj = Class.forName(classes.get(i));
			Method[] method = classObj.getDeclaredMethods();
			for (int j = 0; j < method.length; j++) {
				for (int k = 0; k < testLinkCases.size(); k++) {
					if (method[j]
							.getName()
							.toString()
							.toLowerCase()
							.concat("_")
							.trim()
							.contains(
									testLinkCases.get(k).toLowerCase()
											.replaceAll("-", "_")
											.concat("_".trim()))) {
						multimap.put(classes.get(i), method[j].getName()
								.toString());

						Log.info("Case Match..!"
								+ method[j].getName().toString().toLowerCase()
										.concat("_").trim()
								+ "=="
								+ (testLinkCases.get(k).toLowerCase()
										.concat("_".trim())));
					}
				}

			}
		}
		Log.info("List of classes and methods: " + multimap);
		return multimap;
	}

	/**
	 * Create XML Document as per the parameters
	 * 
	 * @param suiteName
	 * @param testName
	 * @param classesMethod
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public void createXmlDoc(String suiteName, String testName,
			HashMultimap<String, String> classesMethod)
			throws ParserConfigurationException, IOException {
		try {
			Log.info("Creating XML .......");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element suite = doc.createElement("suite");
			doc.appendChild(suite);
			suite.setAttribute("name", suiteName);
			suite.setAttribute("verbose", "1");

			Element listeners = doc.createElement("listeners");
			suite.appendChild(listeners);

			Element listener = doc.createElement("listener");
			listeners.appendChild(listener);
			listener.setAttribute("class-name",
					"com.org.Report.ExtentReportListner");

			Set<String> keys = classesMethod.keySet();
			for (String key : keys) {

				Element test = doc.createElement("test");
				suite.appendChild(test);
				test.setAttribute("name", key);

				Element classes = doc.createElement("classes");
				test.appendChild(classes);

				Element clas = doc.createElement("class");
				classes.appendChild(clas);
				clas.setAttribute("name", key);
				Log.info("Included class " + key);

				Element methods = doc.createElement("methods");
				clas.appendChild(methods);

				Collection<String> values = classesMethod.get(key);
				for (String value : values) {

					Element include = doc.createElement("include");
					methods.appendChild(include);
					include.setAttribute("name", value);
					Log.info("Included method " + value);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(
					System.getProperty("user.dir") + File.separator
							+ "testLaunchers" + File.separator + "Auto.xml");
			transformer.transform(source, result);
			Log.info("Xml created and saved ..."
					+ System.getProperty("user.dir") + File.separator
					+ "testLaunchers" + File.separator + "Auto.xml");

			// writeToPropertiesFile("filestorun", "Auto.xml");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

	/**
	 * Returns the testID's which are not implemented-either the tests are in
	 * data provider are not automated)
	 * 
	 * @param multimap
	 * @param testLinkCases
	 * @return
	 */
	public Set<String> notPartOfAutoXML(HashMultimap<String, String> multimap,
			List<String> testLinkCases) {
		Set<String> notAddedTest = new HashSet<String>();
		for (int i = 0; i < testLinkCases.size(); i++) {
			String testID = testLinkCases.get(i).toLowerCase()
					.replaceAll("-", "_").concat("_".trim());
			if (!multimap.values().stream()
					.anyMatch(list -> list.contains(testID))) {
				notAddedTest.add(testLinkCases.get(i));
			}

		}
		return notAddedTest;
	}

	/**
	 * Write key and value to the properties file without overriding the whole
	 * file
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void writeToPropertiesFile(String key, String value)
			throws IOException {
		FileInputStream in = new FileInputStream(System.getProperty("user.dir")
				+ File.separator + "resources" + File.separator
				+ "browser.properties");
		Properties props = new Properties();
		props.load(in);
		in.close();

		FileOutputStream out = new FileOutputStream(
				System.getProperty("user.dir") + File.separator + "resources"
						+ File.separator + "browser.properties");
		props.setProperty(key, value);
		props.store(out, null);
		out.close();
		Log.info("File name is written in  properties file");
	}

	@Test()
	public void generateDocument() throws ClassNotFoundException,
			ParserConfigurationException, TestLinkAPIException, IOException,
			ConfigurationException, InterruptedException {

		TestLinkAPI api = link.getConnection(link.url, link.devKey);
		TestProject project = link.getProject(link.projectName, api);
		TestPlan plan = link.getplan(api, project.getName(), link.planName);
		int build = link.getBuildIdIfMatched(api, plan.getId(), link.buildName);

		List<String> testCases = link.getAutomatedTestCasesExtIdSuite(api,
				plan.getId(), build, link.keyword);

		System.out.println("Tests From Build: " + testCases.size() + " ->"
				+ testCases);

		List<String> packages = file.getPackagesAsRequired();
		HashMultimap<String, String> classesMethods = HashMultimap.create();
		for (int i = 0; i < packages.size(); i++) {
			ListMultimap<String, String> classesMethod = getClassesAndMethods(
					packages.get(i), testCases);
			classesMethods.putAll(classesMethod);
		}
		System.out.println("Not part of autoXML: "
				+ notPartOfAutoXML(classesMethods, testCases).size() + " ->"
				+ notPartOfAutoXML(classesMethods, testCases));

		createXmlDoc("Test suite Name", "test check", classesMethods);
	}

}
