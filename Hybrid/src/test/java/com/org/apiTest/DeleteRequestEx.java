package com.org.apiTest;

import java.io.IOException;

import org.testng.annotations.Test;

import com.org.CoreUtils.Log;
import com.org.api.JerseyClient;
import com.org.resourceUtils.APIURLS;
import com.org.resourceUtils.ResourceReader;

public class DeleteRequestEx{

	JerseyClient client = JerseyClient.getInstance();
	
	public String url = ResourceReader.getApiURL(APIURLS.DELETEURL);

	@Test(description = "delete request ")
	public void deleteRequest() throws IOException {
		String responce = client.deleteClient(url.concat("/719"));
		System.out.println(responce);
		Log.info("--->" + responce);
	}

}
