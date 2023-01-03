package com.org.api;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CompareJson {

	/**
	 * compare two json responce
	 * 
	 * @author MohanJ
	 * @param actual
	 * @param expected
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static void compareJson(String actual, String expected)
			throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		Assert.assertEquals(mapper.readTree(actual), mapper.readTree(expected));
	}

	/**
	 * compare two json file
	 * 
	 * @author MohanJ
	 * @param actual
	 * @param expected
	 * @throws IOException
	 */
	public static void compareJson(File actual, File expected)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Assert.assertEquals(mapper.readTree(actual), mapper.readTree(expected));
	}

}
