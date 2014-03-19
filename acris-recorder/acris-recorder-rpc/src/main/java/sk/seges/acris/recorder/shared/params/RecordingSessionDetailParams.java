package sk.seges.acris.recorder.shared.params;

public interface RecordingSessionDetailParams {

	public static final String ANTIALIASING_FONTS = "antialiasingFonts";

	boolean isAntialiasingFonts();

	void setAntialiasingFonts(boolean antialiasingFonts);

	public static final String BROWSER_HEIGHT = "browserHeight";

	int getBrowserHeight();

	void setBrowserHeight(int browserHeight);

	public static final String BROWSER_NAME = "browserName";

	String getBrowserName();

	void setBrowserName(String browserName);

	public static final String BROWSER_VERSION = "browserVersion";

	String getBrowserVersion();

	void setBrowserVersion(String browserVersion);

	public static final String BROWSER_WIDTH = "browserWidth";

	int getBrowserWidth();

	void setBrowserWidth(int browserWidth);

	public static final String CITY = "city";

	String getCity();

	void setCity(String city);

	public static final String COLOR_DEPTH = "colorDepth";

	int getColorDepth();

	void setColorDepth(int colorDepth);

	public static final String COUNTRY = "country";

	String getCountry();

	void setCountry(String country);

	public static final String HOST_NAME = "hostName";

	String getHostName();

	void setHostName(String hostName);

	public static final String INITIAL_URL = "initialUrl";

	String getInitialUrl();

	void setInitialUrl(String initialUrl);

	public static final String IP_ADDRESS = "ipAddress";

	String getIpAddress();

	void setIpAddress(String ipAddress);

	public static final String JAVA_ENABLED = "javaEnabled";

	boolean isJavaEnabled();

	void setJavaEnabled(boolean javaEnabled);

	public static final String LANGUAGE = "language";

	String getLanguage();

	void setLanguage(String language);

	public static final String OS_NAME = "osName";

	String getOsName();

	void setOsName(String osName);

	public static final String REFERRER = "referrer";

	String getReferrer();

	void setReferrer(String referrer);

	public static final String SCREEN_HEIGHT = "screenHeight";

	int getScreenHeight();

	void setScreenHeight(int screenHeight);

	public static final String SCREEN_WIDTH = "screenWidth";

	int getScreenWidth();

	void setScreenWidth(int screenWidth);

	public static final String STATE = "state";

	String getState();

	void setState(String state);

	public static final String USER_AGENT = "userAgent";

	String getUserAgent();

	void setUserAgent(String userAgent);
}
