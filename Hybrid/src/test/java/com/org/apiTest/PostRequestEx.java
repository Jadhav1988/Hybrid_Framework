package com.org.apiTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.testng.annotations.Test;

import com.org.CoreUtils.Log;
import com.org.api.CompareJson;
import com.org.api.JerseyClient;
import com.org.api.XMLToJson;
import com.org.resourceUtils.APIURLS;
import com.org.resourceUtils.ResourceReader;

public class PostRequestEx {

	XMLToJson jsonReq = XMLToJson.getInstance();

	JerseyClient client = JerseyClient.getInstance();

	public final String API_DEFINATION_DIR = "api_defination" + File.separator;

	public String url = ResourceReader.getApiURL(APIURLS.POSTURL);

	@Test(description = "post request test ")
	public void postRequest() throws IOException {

		// converts xml to json as payload
		Path filePath = Paths.get(API_DEFINATION_DIR + "Test.xml");
		JSONObject payload = jsonReq.getJSONObject(filePath);
		payload = payload.getJSONObject("Employee");
		System.out.println(payload);

		String responce = client.postRequest(url, payload.toString());
		System.out.println(responce);
		String s2 = responce;
		Log.info("--->" + responce);

		// compare two json
		CompareJson.compareJson(responce, s2);
	}

}
