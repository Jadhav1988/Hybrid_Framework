package com.org.CoreUtils;

import org.apache.log4j.Logger;

public class Log {

	private static Logger Log= Logger.getLogger(Log.class.getSimpleName());

	/**
	 * To print Log for the start test case
	 * 
	 * @param startTestCaseMessage
	 */
	public static void startTestCase(String startTestCaseMessage) {
		System.out.println();
		Log.info("********************* " + "S_T_A_R_T"
				+ " *********************");
		Log.info("********************* " + startTestCaseMessage
				+ " *********************");
	}

	/**
	 * This is to print Log for the ending of the test case
	 * 
	 * @param endTestCaseMessage
	 */
	public static void endTestCase(String endTestCaseMessage) {
		Log.info("********************* " + "-E---N---D-"
				+ " *********************");
		Log.info("********************* " + endTestCaseMessage
				+ " *********************");
		System.out.println();
	}

	public static void info(String infoMessage) {
		Log.info(infoMessage);
	}

	public static void warn(String warnMessage) {
		Log.warn(warnMessage);
	}

	public static void error(String errorMessage) {
		Log.error(errorMessage);
	}

	public static void fatal(String fatalMessage) {
		Log.fatal(fatalMessage);
	}

	public static void debug(String debugMessage) {
		Log.debug(debugMessage);
	}

}
