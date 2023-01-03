package com.org.apiTest;

import java.io.IOException;

import org.testng.annotations.Test;

import com.org.CoreUtils.Log;
import com.org.api.JerseyClient;

public class GetRequestEx {

	JerseyClient client = JerseyClient.getInstance();

	String url = "http://dummy.restapiexample.com/api/v1/employees";

	@Test(description = "get request test ")
	public void getRequest() throws IOException {
		String responce = client.getRequest(url);
		System.out.println(responce);
		Log.info("--->" + responce);
	}

}
