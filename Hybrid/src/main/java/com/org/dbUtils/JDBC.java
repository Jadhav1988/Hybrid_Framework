package com.org.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.org.CoreUtils.Log;
import com.org.resourceUtils.DBReader;
import com.org.resourceUtils.ResourceReader;

public class JDBC {

	protected String DBURL = ResourceReader
			.getDataBaseProperties(DBReader.DBURL);

	protected String userName = ResourceReader
			.getDataBaseProperties(DBReader.DBUserName);

	protected String password = ResourceReader
			.getDataBaseProperties(DBReader.DBPassword);

	/**
	 * Connect to the data base and returns the executed query data
	 * 
	 * @param moduleName
	 * @return
	 */
	public Object[][] getData(String query) {
		try (Connection conn = DriverManager.getConnection(DBURL, userName,
				password);
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			int colCount = rs.getMetaData().getColumnCount();
			rs.last();
			int rowCount = rs.getRow();
			rs.beforeFirst();
			// rs.next();

			Object[][] data = new Object[rowCount][colCount];
			int row = 0;
			while (rs.next()) {
				for (int i = 0; i < colCount; i++) {
					data[row][i] = rs.getObject(i + 1);
				}
				row++;
			}
			return data;
		} catch (SQLException e) {
			Log.error("SQL Error: " + e);
		}

		return null;
	}

}
