package sk.seges.acris.recorder.client.session;

import sk.seges.acris.recorder.shared.params.RecordingSessionDetailParams;
import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;

public class RecordingSessionDetailParamsJSO extends BaseJSONModel implements RecordingSessionDetailParams {

	public RecordingSessionDetailParamsJSO() {};

	public RecordingSessionDetailParamsJSO(String json) {
		super(JSONModel.fromJson(json));
	}

	@Override
	public boolean isAntialiasingFonts() {
		return data.getBoolean(ANTIALIASING_FONTS);
	}

	@Override
	public void setAntialiasingFonts(boolean antialiasingFonts) {
		data.set(ANTIALIASING_FONTS, antialiasingFonts);
	}

	@Override
	public int getBrowserHeight() {
		return data.getInteger(BROWSER_HEIGHT);
	}

	@Override
	public void setBrowserHeight(int browserHeight) {
		data.set(BROWSER_HEIGHT, browserHeight);
	}

	@Override
	public String getBrowserName() {
		return data.get(BROWSER_NAME);
	}

	@Override
	public void setBrowserName(String browserName) {
		data.set(BROWSER_NAME, browserName);
	}

	@Override
	public String getBrowserVersion() {
		return data.get(BROWSER_VERSION);
	}

	@Override
	public void setBrowserVersion(String browserVersion) {
		data.set(BROWSER_VERSION, browserVersion);
	}

	@Override
	public int getBrowserWidth() {
		return data.getInteger(BROWSER_WIDTH);
	}

	@Override
	public void setBrowserWidth(int browserWidth) {
		data.set(BROWSER_WIDTH, browserWidth);
	}

	@Override
	public String getCity() {
		return data.get(CITY);
	}

	@Override
	public void setCity(String city) {
		data.set(CITY, city);
	}

	@Override
	public int getColorDepth() {
		return data.getInteger(COLOR_DEPTH);
	}

	@Override
	public void setColorDepth(int colorDepth) {
		data.set(COLOR_DEPTH, colorDepth);
	}

	@Override
	public String getCountry() {
		return data.get(COUNTRY);
	}

	@Override
	public void setCountry(String country) {
		data.set(COUNTRY, country);
	}

	@Override
	public String getHostName() {
		return data.get(HOST_NAME);
	}

	@Override
	public void setHostName(String hostName) {
		data.set(HOST_NAME, hostName);
	}

	@Override
	public String getInitialUrl() {
		return data.get(INITIAL_URL);
	}

	@Override
	public void setInitialUrl(String initialUrl) {
		data.set(INITIAL_URL, initialUrl);
	}

	@Override
	public String getIpAddress() {
		return data.get(IP_ADDRESS);
	}

	@Override
	public void setIpAddress(String ipAddress) {
		data.set(IP_ADDRESS, ipAddress);
	}

	@Override
	public boolean isJavaEnabled() {
		return data.getBoolean(JAVA_ENABLED);
	}

	@Override
	public void setJavaEnabled(boolean javaEnabled) {
		data.set(JAVA_ENABLED, javaEnabled);
	}

	@Override
	public String getLanguage() {
		return data.get(LANGUAGE);
	}

	@Override
	public void setLanguage(String language) {
		data.set(LANGUAGE, language);
	}

	@Override
	public String getOsName() {
		return data.get(OS_NAME);
	}

	@Override
	public void setOsName(String osName) {
		data.set(OS_NAME, osName);
	}

	@Override
	public String getReferrer() {
		return data.get(REFERRER);
	}

	@Override
	public void setReferrer(String referrer) {
		data.set(REFERRER, referrer);
	}

	@Override
	public int getScreenHeight() {
		return data.getInteger(SCREEN_HEIGHT);
	}

	@Override
	public void setScreenHeight(int screenHeight) {
		data.set(SCREEN_HEIGHT, screenHeight);
	}

	@Override
	public int getScreenWidth() {
		return data.getInteger(SCREEN_WIDTH);
	}

	@Override
	public void setScreenWidth(int screenWidth) {
		data.set(SCREEN_WIDTH, screenWidth);
	}

	@Override
	public String getState() {
		return data.get(BROWSER_WIDTH);
	}

	@Override
	public void setState(String state) {
		data.set(STATE, state);
	}

	@Override
	public String getUserAgent() {
		return data.get(USER_AGENT);
	}

	@Override
	public void setUserAgent(String userAgent) {
		data.set(USER_AGENT, userAgent);
	}
}