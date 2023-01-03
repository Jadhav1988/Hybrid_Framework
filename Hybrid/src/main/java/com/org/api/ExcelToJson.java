package com.org.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExcelToJson {

	/**
	 * Get the excel data in list of maps
	 * 
	 * @author MohanJ
	 * @param fileName
	 * @param sheetName
	 * @param headerData
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<Map<String, String>> getExcelDataAsList(String fileName,
			String sheetName, String headerData) throws FileNotFoundException,
			IOException {

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(fileName));

		List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		Map<Integer, String> header = new HashMap<Integer, String>();
		String[] arg = headerData.split(",");

		for (int i = 0; i < arg.length; i++) {
			if (!arg[i].equals(""))
				header.put(i, arg[i]);
		}

		for (Row row : wb.getSheet(sheetName)) {

			if (row.getRowNum() == 0) {
				continue;
			}

			Map<String, String> tempMap = new HashMap<String, String>();
			DataFormatter formatter = new DataFormatter();
			for (Cell cell : row) {
				if (header.get(cell.getColumnIndex()) != null) {

					String value = formatter.formatCellValue(cell);
					tempMap.put(header.get(cell.getColumnIndex()), value);

				}
			}

			tempList.add(tempMap);
		}

		return tempList;
	}

	/**
	 * Convert list of maps to JSON
	 * 
	 * @author MohanJ
	 * @param map
	 * @return
	 * @throws JsonProcessingException
	 */
	public String convertToJson(List<Map<String, String>> map)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(map);
		return jsonResult;
	}
}
