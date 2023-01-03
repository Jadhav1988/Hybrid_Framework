package com.org.testlinkUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This file has been used to create AUTO.XML file
 * 
 * @author MohanJ
 */
public class FileFunctions {

	/**
	 * Returns the list of directories from the given source folder
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public List<String> getDirectories(File dir) throws IOException {
		List<String> folders = new ArrayList<>();
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				folders.add(file.getPath());
				folders.addAll(getDirectories(file));
			}
		}
		return folders;
	}

	/**
	 * get the character occurrence from the given string
	 * 
	 * @param str
	 * @param cha
	 * @return
	 */
	public int getOccuringChar(String str, char cha) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == cha)
				count++;
		}
		return count;

	}

	/**
	 * format the test packages as required to generate AUTO.XML
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> getPackagesAsRequired() throws IOException {
		List<String> formatedPackageName = new ArrayList<>();
		try {
			List<String> listFolder = getDirectories(new File("/src/test/java"));

			for (int i = 0; i < listFolder.size(); i++) {
				String str = listFolder.get(i).replaceAll("\\\\", ".")
						.replaceAll("src.test.java.", " ").trim();
				if (getOccuringChar(str, '.') >= 1) {
					formatedPackageName.add(listFolder.get(i)
							.replaceAll("\\\\", ".")
							.replaceAll("src.test.java.", " ").trim());
				}
			}
		} catch (NullPointerException e) {
			List<String> listFolder = getDirectories(new File(
					"target/test-classes"));
			for (int i = 0; i < listFolder.size(); i++) {
				String str = listFolder.get(i).replaceAll("\\\\", ".")
						.replaceAll("target.test-classes.", " ").trim();
				if (getOccuringChar(str, '.') >= 1) {
					formatedPackageName.add(listFolder.get(i)
							.replaceAll("\\\\", ".")
							.replaceAll("target.test-classes.", " ").trim());
				}
			}
		}
		return formatedPackageName;

	}
}
