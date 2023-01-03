package com.org.dbUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.org.CoreUtils.Log;
import com.org.resourceUtils.DBReader;
import com.org.resourceUtils.ResourceReader;

public class SQLConnection {

	/**
	 * Connection and session objects
	 */
	private static Connection connection = null;
	private static Session session = null;

	/**
	 * Data base resource properties
	 */
	public static String sshHost = ResourceReader
			.getDataBaseProperties(DBReader.SSH_HOSTNAME);

	public static String sshuser = ResourceReader
			.getDataBaseProperties(DBReader.SSH_USERNAME);

	public static int localPort = DBReader.LOCAL_PORTNUMBER;

	public static String remoteHost = ResourceReader
			.getDataBaseProperties(DBReader.REMOTE_HOSTNAME);

	public static int remotePort = DBReader.REMOTE_PORTNUMBER;

	public static String dbuserName = ResourceReader
			.getDataBaseProperties(DBReader.DATABASE_USERNAME);

	public static String dbpassword = ResourceReader
			.getDataBaseProperties(DBReader.DATABASE_USER_PASSWORD);

	public static String driverName = ResourceReader
			.getDataBaseProperties(DBReader.SQL_DRIVER);

	public static String localSSHUrl = ResourceReader
			.getDataBaseProperties(DBReader.LOCAL_SSHURL);

	/**
	 * Connect to the SSH tunnel and Data base
	 * 
	 * @param dataBaseName
	 * @throws SQLException
	 */
	private static void connectToServer(String dataBaseName)
			throws SQLException {
		connectSSH();
		connectToDataBase(dataBaseName);
	}

	/**
	 * Connect to the SSH Tunnel
	 * 
	 * @throws SQLException
	 */
	private static void connectSSH() throws SQLException {

		String SshKeyFilepath = System.getProperty("user.dir") + File.separator
				+ "data" + File.separator + "g4_dev";

		try {
			java.util.Properties config = new java.util.Properties();
			JSch jsch = new JSch();
			session = jsch.getSession(sshuser, sshHost, 22);
			jsch.addIdentity(SshKeyFilepath);
			config.put("StrictHostKeyChecking", "no");
			config.put("ConnectionAttempts", "4");
			session.setConfig(config);
			session.connect();

			Log.info("SSH Connected");

			Class.forName(driverName).newInstance();
			int assinged_port = session.setPortForwardingL(localPort,
					remoteHost, remotePort);

			Log.info("localhost:" + assinged_port + " -> " + remoteHost + ":"
					+ remotePort);
			Log.info("Port Forwarded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connect to the data base
	 * 
	 * @param dataBaseName
	 * @throws SQLException
	 */
	private static void connectToDataBase(String dataBaseName)
			throws SQLException {
		try {

			// mysql database connectivity
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName(localSSHUrl);
			dataSource.setPortNumber(localPort);
			dataSource.setUser(dbuserName);
			dataSource.setAllowMultiQueries(true);

			dataSource.setPassword(dbpassword);
			dataSource.setDatabaseName(dataBaseName);

			connection = dataSource.getConnection();

			Log.info("Connection to server successful!:" + connection + "\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the SSH Tunnel and Data base connection
	 */
	private static void closeConnections() {
		CloseDataBaseConnection();
		CloseSSHConnection();
	}

	/**
	 * Close the data base connection
	 */
	private static void CloseDataBaseConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				Log.info("Closing Database Connection");
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Close the SSH Tunnel
	 */
	private static void CloseSSHConnection() {
		if (session != null && session.isConnected()) {
			Log.info("Closing SSH Connection");
			session.disconnect();
		}
	}

	/**
	 * Execute any query - works ONLY FOR single query (one SELECT or one DELETE
	 * etc)
	 * 
	 * @param query
	 * @param dataBaseName
	 * @return
	 */
	private static ResultSet executeMyQuery(String query, String dataBaseName) {
		ResultSet resultSet = null;

		try {
			connectToServer(dataBaseName);
			Statement stmt = connection.createStatement();
			resultSet = stmt.executeQuery(query);
			Log.info("Database connection success");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}

	/**
	 * Get all the live database names from DB
	 * 
	 * @return
	 */
	public static List<String> getAllDBNames() {
		List<String> organisationDbNames = new ArrayList<String>();
		ResultSet resultSet = executeMyQuery("show databases", "DB1");
		try {
			while (resultSet.next()) {
				String actualValue = resultSet.getString("Database");
				organisationDbNames.add(actualValue);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnections();
		}
		return organisationDbNames;
	}

	/**
	 * Delete data base
	 * 
	 * @param DataBasesNamesList
	 */
	public static void deleteDataBasesByName(List<String> DataBasesNamesList) {
		try {
			connectSSH();
			int dataBasesAmount = DataBasesNamesList.size();
			for (int i = 0; i < dataBasesAmount; i++) {
				connectToDataBase(DataBasesNamesList.get(i));

				Statement stmt = connection.createStatement();
				stmt.executeUpdate("DROP database `"
						+ DataBasesNamesList.get(i) + "`");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			CloseDataBaseConnection();
			closeConnections();
		}
	}

	/**
	 * return the grid column data
	 * 
	 * @param moduleName
	 * @return
	 * @throws SQLException
	 */
	public Object[][] getGridColumns(String moduleName) throws SQLException {
		connectToServer("tpc");

		ResultSet rs = executeMyQuery(
				"SELECT query_views.qv_name,query_columns.qcol_name,query_columns.qcol_heading,query_columns.qcol_sortable,query_columns.qcol_sorttype FROM query_views INNER JOIN query_columns ON query_columns.qv_id = query_views.qv_id WHERE query_views.qv_name='"
						+ moduleName
						+ "' and  query_columns.qcol_sortable='y' ORDER BY query_columns.qcol_heading",
				"tpc");
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
		closeConnections();
		return data;
	}

}
