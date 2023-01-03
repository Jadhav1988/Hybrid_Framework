package com.org.cucumber;

import org.testng.annotations.DataProvider;

import cucumber.api.testng.AbstractTestNGCucumberTests;

public class Runner extends AbstractTestNGCucumberTests {

	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.features();
	}
}
