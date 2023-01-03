package com.org.apiTest;

import java.io.IOException;

import org.testng.annotations.Test;

import com.org.CoreUtils.Log;
import com.org.api.JerseyClient;
import com.org.resourceUtils.APIURLS;
import com.org.resourceUtils.ResourceReader;

public class PutRequestEx {

	JerseyClient client = JerseyClient.getInstance();

	public String url = ResourceReader.getApiURL(APIURLS.PUTURL);

	@Test(description = "post request test ")
	public void postRequest() throws IOException {
		String responce = client.putRequest(url.concat("/719"));
		System.out.println(responce);
		Log.info("--->" + responce);
	}

}
