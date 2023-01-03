package com.org.api;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.org.CoreUtils.Log;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClient {

	/**
	 * The HTTP client
	 * 
	 * @author MohanJ
	 */
	private static JerseyClient httpClient = null;

	/**
	 * Instantiate a new jersey client
	 * 
	 * @author MohanJ
	 */
	private JerseyClient() {

	}

	/**
	 * Get the instance of the jersey client
	 * 
	 * @author MohanJ
	 * @return client instance
	 */
	public static JerseyClient getInstance() {
		if (httpClient == null) {
			synchronized (JerseyClient.class) {
				if (httpClient == null) {
					httpClient = new JerseyClient();
				}
			}
		}
		return httpClient;
	}

	/**
	 * Get request
	 * 
	 * @author MohanJ
	 * @param url
	 * @return
	 */
	public String getRequest(String url) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * Get request with header
	 * 
	 * @author MohanJ
	 * @param url
	 * @param headers
	 * @return
	 */
	public String getRequest(String url, Map<String, String> headers) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		WebResource.Builder builder = webResource
				.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				Log.info("key: " + entry.getKey());
				Log.info("value: " + entry.getValue());
				builder.header(key, value);
			}
		}

		ClientResponse response = builder.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("payload " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * post Request
	 * 
	 * @author MohanJ
	 * @param url
	 * @param payload
	 * @return
	 */
	public String postRequest(String url, String payload) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,
						payload);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("Payload" + payload);
		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * post request with headers
	 * 
	 * @author MohanJ
	 * @param url
	 * @param headers
	 * @param payload
	 * @return
	 */
	public String postRequest(String url, Map<String, String> headers,
			String payload) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);

		WebResource.Builder builder = webResource
				.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				Log.info("key: " + entry.getKey());
				Log.info("value: " + entry.getValue());
				builder.header(key, value);
			}
		}

		ClientResponse response = builder.post(ClientResponse.class, payload);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("Payload" + payload);
		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * put request
	 * 
	 * @author MohanJ
	 * @param url
	 * @return
	 */
	public String putRequest(String url) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * Put request with payload
	 * 
	 * @author MohanJ
	 * @param url
	 * @param paylaod
	 * @return
	 */
	public String putRequest(String url, String payload) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class,
						payload);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("Payload" + payload);
		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * put request
	 * 
	 * @author MohanJ
	 * @param url
	 * @param headers
	 * @return
	 */
	public String putRequest(String url, Map<String, String> headers) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		WebResource.Builder builder = webResource
				.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				Log.info("key: " + entry.getKey());
				Log.info("value: " + entry.getValue());
				builder.header(key, value);
			}
		}

		ClientResponse response = builder.put(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * delete client
	 * 
	 * @author MohanJ
	 * @param url
	 * @return
	 */
	public String deleteClient(String url) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}

	/**
	 * Delete request
	 * 
	 * @author MohanJ
	 * @param url
	 * @param headers
	 * @return
	 */
	public String deleteClient(String url, Map<String, String> headers) {
		String jsonResponce = null;

		Client client = Client.create();

		WebResource webResource = client.resource(url);
		WebResource.Builder builder = webResource
				.accept(MediaType.APPLICATION_JSON);
		builder.type(MediaType.APPLICATION_JSON);

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				Log.info("key: " + entry.getKey());
				Log.info("value: " + entry.getValue());
				builder.header(key, value);
			}
		}
		ClientResponse response = builder.delete(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		jsonResponce = response.getEntity(String.class);

		Log.info("URL " + url + " and the responce :" + response);

		return jsonResponce;
	}
}
