package sk.seges.sesam.core.test.webdriver.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractReportHelper {

	private static final String RESULT_PATH_PREFIX = "target" + File.separator + "report";

	protected static final String DATE_FORMAT = "yyyy-MM-dd";
	protected static final String TIME_FORMAT = "HH-mm-ss";
	
	protected String getTimeStamp() {
		return getTimeStamp(DATE_FORMAT + "_" + TIME_FORMAT);
	}

	protected String getTimeStamp(final String simpleDateFormat) {
		Date currentDateTime = new Date(System.currentTimeMillis());
		return new SimpleDateFormat(simpleDateFormat).format(currentDateTime);
	}

	protected String getResultDirectory() {
		String result = RESULT_PATH_PREFIX;

		result = new File(result).getAbsolutePath();
		result += File.separator;

		return result;
	}
}
