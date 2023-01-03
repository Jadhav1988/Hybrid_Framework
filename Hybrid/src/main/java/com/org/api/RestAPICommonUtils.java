package com.org.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;

import com.org.resourceUtils.APIURLS;
import com.org.resourceUtils.ResourceReader;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAPICommonUtils {
	
	public RequestSpecification requestSpecification;

	public RestAPICommonUtils(RequestSpecification requestSpecification) {
		this.requestSpecification = requestSpecification;
	}

	/*
	 * This method will helps to add the authentication
	 */
	public void addBasicAuthentication(String userName, String password) {
		requestSpecification.auth().basic(userName, password);
	}

	/*
	 * This method will helps to add the base path
	 */
	public void addBaseURI(String basePath) {
		requestSpecification.baseUri(basePath);
	}

	/*
	 * This method will helps to add the Headers
	 */
	public void addHeaders(String key, String value) {
		requestSpecification.headers(key, value);
	}

	/*
	 * This method will helps to add the List of headers
	 */
	public void addHeaders(Map<String, String> headers) {
		requestSpecification.headers(headers);
	}

	/*
	 * This method will helps to add the Query parms
	 */
	public void addQueryParams(String key, String value) {
		requestSpecification.queryParams(key, value);
	}

	/*
	 * This method will helps to add the List of query params
	 */
	public void addQueryParams(Map<String, String> queryParams) {
		requestSpecification.queryParams(queryParams);
	}

	/*
	 * This method will helps to add the request body
	 */
	public void addBody(String body) {
		requestSpecification.body(body);
	}

	/*
	 * This method will helps to hit the service
	 */
	public Response httpCall(String methodType, String path) {
		Response response = null;
		switch (methodType.toLowerCase()) {
		case "get":
			response = requestSpecification.get(path);
			return response;
		case "post":
			response = requestSpecification.when().post(path);
			return response;
		case "put":
			response = requestSpecification.when().put(path);
			return response;
		case "delete":
			response = requestSpecification.when().delete(path);
			return response;
		default:
			return response;
		}
	}

	/*
	 * This method will helps to validate status code
	 */
	public void validateResponseStatusCode(Response response, int statusCode) {
		Assert.assertEquals(response.statusCode(), statusCode);
	}

	/*
	 * This method will helps to get the response headers
	 */
	public String getResponseHeader(Response response, String header) {
		return response.getHeader(header);
	}

	/*
	 * This method will helps to get the list response headers
	 */
	public List<Header> getResponseHeaders(Response response) {
		return response.getHeaders().asList();
	}

	/*
	 * This method will helps to get the Reponse body as string format
	 */
	public String getResponseBody(Response response) {
		return response.getBody().asString();
	}

	/*
	 * This method will helps to get the token
	 */
	public String getAuthenticationToken() {
		Map<String, String> haders = new HashMap<String, String>();
		haders.put("Content-Type", "application/x-www-form-urlencoded");
		haders.put("Authorization", "Basic bXlhYmlsaXR5Om5MUGNJeBhobUkvM2J0aWZzTm1YQVE9PQ==");
		haders.put("Accept", "application/json");

		RequestSpecification request = RestAssured.given()
				.config(RestAssured.config().encoderConfig(
						EncoderConfig.encoderConfig().encodeContentTypeAs("x-www-form-urlencoded", ContentType.URLENC)))
				.contentType(ContentType.URLENC.withCharset("UTF-8")).headers(haders)
				.formParam("grant_type", "password").formParam("username", "3+F0JkuWacxKfh+W7XZNQ==")
				.formParam("password", "4yzxkGaBiRQbP9psAoZMBA==").formParam("scope", "openid project");

		String url=getApiUrl("test");
		Response response = request.post(url);
		Assert.assertEquals(response.statusCode(), 200);
		// System.out.println(response.statusCode());
		// System.out.println(response.jsonPath().getString("access_token"));

		return response.jsonPath().getString("access_token");
	}

	public String getApiUrl(String product) {
		String url = ResourceReader.getApiURL(APIURLS.GETURL).trim();
		
		return url;
	}


	/*
	 * This method will helps to add the Random Email
	 */
	public String getRandomEmail() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString + "@testOne.com";
	}

	/*
	 * This method will helps to get the unique number
	 */
	public String getUniqueNum() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
		return sdf.format(d);
	}
}
