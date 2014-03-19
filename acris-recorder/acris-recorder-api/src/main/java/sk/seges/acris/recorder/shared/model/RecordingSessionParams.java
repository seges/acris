package sk.seges.acris.recorder.shared.model;

import java.io.Serializable;

public interface RecordingSessionParams extends Serializable {

	String getBrowserName();
	void setBrowserName(String browserName);

	String getBrowserVersion();
	void setBrowserVersion(String browserVersion);

	String getUserAgent();
	void setUserAgent(String userAgent);

	String getOsName();
	void setOsName(String osName);

	int getScreenWidth();
	void setScreenWidth(int screenWidth);

	int getScreenHeight();
	void setScreenHeight(int screenHeight);

	int getBrowserWidth();
	void setBrowserWidth(int browserWidth);

	int getBrowserHeight();
	void setBrowserHeight(int browserHeight);

	int getColorDepth();
	void setColorDepth(int colorDepth);

	boolean isJavaEnabled();
	void setJavaEnabled(boolean javaEnabled);

	boolean isAntialiasingFonts();
	void setAntialiasingFonts(boolean antialiasingFonts);

	String getIpAddress();
	void setIpAddress(String ipAddress);

	String getHostName();
	void setHostName(String hostName);

	String getLanguage();
	void setLanguage(String language);

	String getReferrer();
	void setReferrer(String referrer);

	String getCity();
	void setCity(String city);

	String getCountry();
	void setCountry(String country);

	String getState();
	void setState(String state);

}
