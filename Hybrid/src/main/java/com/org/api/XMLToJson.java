package com.org.api;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Node;

import com.org.CoreUtils.Log;

public class XMLToJson {

	/**
	 * The Xml to Json
	 */
	private static XMLToJson xmlToJson = null;

	/**
	 * Instantiates a new XML to Json
	 */
	private XMLToJson() {

	}

	/**
	 * Get the single instance of XMLToJson
	 * 
	 * @author MohanJ
	 * @return
	 */
	public static XMLToJson getInstance() {
		if (xmlToJson == null) {
			synchronized (XMLToJson.class) {
				if (xmlToJson == null) {
					xmlToJson = new XMLToJson();
				}
			}
		}
		return xmlToJson;
	}

	/**
	 * Xml node to string
	 * 
	 * @author MohanJ
	 * @param node
	 * @return
	 */
	private String xmlNodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));

		} catch (TransformerException ex) {
			Log.info("Xml node to strating conversation is failure" + ex);
		}
		return sw.toString();
	}

	/**
	 * get the JSON object
	 * 
	 * @author MohanJ
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public JSONObject getJSONObject(Path filePath) throws IOException {
		return getJSONObject(new String(Files.readAllBytes(filePath)));
	}

	/**
	 * get the JSON object
	 * 
	 * @author MohanJ
	 * 
	 * @param xmlNode
	 * @return
	 */
	public JSONObject getJSONObject(Node xmlNode) {
		return getJSONObject(xmlNodeToString(xmlNode));
	}

	/**
	 * get the json object
	 * 
	 * @param xmlAsString
	 * @return
	 */
	public JSONObject getJSONObject(String xmlAsString) {
		JSONObject xmlJSONObject = null;

		try {
			xmlJSONObject = XML.toJSONObject(xmlAsString);
			Log.info("converted to json from xml: " + xmlJSONObject);
		} catch (JSONException jException) {
			Log.error("Error while converting from XML to JSON ");
		}
		return xmlJSONObject;
	}

}
