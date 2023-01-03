package com.org.excelUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.CoreUtils.Log;

public class ExcelUtils {

	private static XSSFSheet ExcelWSheet;

	private static XSSFWorkbook ExcelWBook;

	private static XSSFCell Cell;

	/**
	 * set the File path and to open the Excel file
	 * 
	 * @param fileName
	 * @param SheetName
	 * @throws Exception
	 */
	public static void setExcelFile(String fileName, String SheetName)
			throws Exception {
		String vXlsxFilePath = "data/" + fileName;
		try {
			FileInputStream ExcelFile = new FileInputStream(vXlsxFilePath);

			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * returns the test data from the Excel cell
	 * 
	 * @param RowNum
	 * @param ColNum
	 * @return
	 * @throws Exception
	 */
	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			DataFormatter formatter = new DataFormatter();
			String CellData = formatter.formatCellValue(Cell);
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Get the list of rows which matches the criteria and run mode
	 * 
	 * @param dataMatch
	 * @param colNum
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getRowContains(String dataMatch, int colNum)
			throws Exception {
		List<Integer> rowNumber = new ArrayList<Integer>();
		int rowCount = ExcelUtils.getRowUsed();
		for (int i = 0; i <= rowCount; i++) {
			if (ExcelUtils.getCellData(i, colNum).equalsIgnoreCase(dataMatch)
					&& ExcelUtils.getCellData(i, 0).equalsIgnoreCase("Yes")) {
				rowNumber.add(i);
			}
		}
		return rowNumber;
	}

	/**
	 * Get cell data if run mode as yes
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getRowContains() throws Exception {
		List<Integer> rowNumber = new ArrayList<Integer>();
		int rowCount = ExcelUtils.getRowUsed();
		for (int i = 0; i <= rowCount; i++) {
			if (ExcelUtils.getCellData(i, 0).equalsIgnoreCase("Yes")) {
				rowNumber.add(i);
			}
		}
		return rowNumber;
	}

	/**
	 * get the total row used in the sheet
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getRowUsed() throws Exception {
		try {
			int RowCount = ExcelWSheet.getLastRowNum();
			return RowCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	/**
	 * Get data by filter type
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param filterName
	 * @param filterColumNumber
	 * @return
	 * @throws Exception
	 */
	public static Object[][] getDataByFilterType(String fileName,
			String sheetName, String filterName, int filterColumNumber)
			throws Exception {

		setExcelFile(fileName, sheetName);

		List<Integer> matchedRows = ExcelUtils.getRowContains(filterName,
				filterColumNumber);
		int columns = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();

		Object Data[][] = new Object[matchedRows.size()][columns];

		for (int i = 0; i < matchedRows.size(); i++) {

			int noOfColumn = ExcelWSheet.getRow(matchedRows.get(i))
					.getPhysicalNumberOfCells();

			for (int j = 0; j < noOfColumn; j++) {

				String data = ExcelUtils.getCellData(matchedRows.get(i), j);

				Data[i][j] = data;
			}
		}

		return Data;
	}

	/**
	 * returns the data if the run mode set to yes
	 * 
	 * @param fileName
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static Object[][] getDataFromExcel(String fileName, String sheetName)
			throws Exception {

		setExcelFile(fileName, sheetName);
		String vXlsxFilePath = "data/" + fileName;
			FileInputStream ExcelFile = new FileInputStream(vXlsxFilePath);

			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);

		List<Integer> matchedRows = ExcelUtils.getRowContains();
		Integer columns = ExcelWSheet.getRow(0).getPhysicalNumberOfCells();

		Object Data[][] = new Object[matchedRows.size()][columns];

		for (int i = 0; i < matchedRows.size(); i++) {

			int noOfColumn = ExcelWSheet.getRow(matchedRows.get(i))
					.getPhysicalNumberOfCells();

			for (int j = 0; j < noOfColumn; j++) {

				String data = ExcelUtils.getCellData(matchedRows.get(i), j);

				Data[i][j] = data;
			}
		}

		return Data;
	}
	
	/**
	 * Method to read xls file
	 * @param fileName
	 * @param expData
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void getDataFromXLSFile(String fileName,String expData)
			throws Exception {
			InputStream ExcelFileToRead = new FileInputStream(System.getProperty("user.dir")
					+ "/libs" + "/Downloads/"+fileName);
			HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
			HSSFSheet sheet=wb.getSheetAt(0);
			HSSFRow row; 
			HSSFCell cell;
			@SuppressWarnings("rawtypes")
			Iterator rows = sheet.rowIterator();
			outerloop:
			while (rows.hasNext())
			{
				row=(HSSFRow) rows.next();
				@SuppressWarnings("rawtypes")
				Iterator cells = row.cellIterator();
				while (cells.hasNext())
				{
					cell=(HSSFCell) cells.next();
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					{
						if(cell.getStringCellValue().replace("\n", "").contains(expData)) {
						Log.info("Sheet contains required value -->"+cell.getStringCellValue());
						break outerloop;
						} 
						}
					}
				}
	}
}
