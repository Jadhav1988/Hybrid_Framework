package com.org.resourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Class for defining String properties
 *
 * @author
 */

public class ResourceReader {
	// ~ Static Variables & Initializers
	// --------------------------------------------------------------

	static Locale locale = Locale.ROOT;
	private static ResourceBundle browserSettings = ResourceBundle.getBundle("browser", locale);
	private static ResourceBundle mobileSettings = ResourceBundle.getBundle("mobile", locale);
	private static ResourceBundle testLinkApi = ResourceBundle.getBundle("testLinkAPI", locale);
	private static ResourceBundle appSettings = ResourceBundle.getBundle("project", locale);
	private static ResourceBundle apiURLs = ResourceBundle.getBundle("apiUrl", locale);
	private static ResourceBundle dataBase = ResourceBundle.getBundle("dataBase", locale);
	private static ResourceBundle email = ResourceBundle.getBundle("Email", locale);

	// ~ Methods
	// --------------------------------------------------------------------------------------

	/**
	 * DOCUMENT ME!
	 *
	 * @param key DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static String getTestLinkSettings(String key) {
		return testLinkApi.getString(key);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param key DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static String getAppSettings(String key) {
		return appSettings.getString(key);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param key DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static String getMobileSettings(String key) {
		return mobileSettings.getString(key);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param aName DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static ResourceBundle getResourceBundle(final String aName) {
		try (FileInputStream fis = new FileInputStream(aName)) {
			return new PropertyResourceBundle(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * To read properties file and get key-values
	 *
	 * @param key the key name of properties file
	 *
	 * @return key value
	 */
	public static String getSettings(String key) {
		return browserSettings.getString(key);
	}

	public static String getApiURL(String urlName) {
		return apiURLs.getString(urlName);
	}

	/**
	 * To read properties file and get key-values
	 *
	 * @return value of proxy
	 */
	public static boolean isProxy() {
		String value = browserSettings.getString("isProxy");
		return ((value != null) && value.equalsIgnoreCase("true")) ? true : false;
	}

	/**
	 * To read properties file and get key-values
	 *
	 * @return value of proxyAU
	 */
	public static boolean isProxyAU() {
		String value = browserSettings.getString("isProxyAU");
		return ((value != null) && value.equalsIgnoreCase("true")) ? true : false;
	}

	public static String getDataBaseProperties(String key) {
		return dataBase.getString(key);
	}

	public static String getEmail(String key) {
		return email.getString(key);
	}

}
