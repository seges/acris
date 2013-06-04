package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.ReportingSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.utils.ConfigurationUtils;

public class DefaultReportingSettings implements ReportingSettings {

	public enum ReportingSettingsConfiguration implements Configuration {

		PRODUCE_SCREENSHOTS("report.produce.screenshots"),
		RESULT_DIRECTORY("report.result.directory"),
		SCREENSHOTS_DIRECTORY("report.screenshot.directory");

		private String key;
		
		ReportingSettingsConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}

	private Boolean produceScreenshots;
	private String resultDirectory;
	private String screenshotDirectory;

	public DefaultReportingSettings(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationBoolean(configurations, ReportingSettingsConfiguration.PRODUCE_SCREENSHOTS),
			 ConfigurationUtils.getConfigurationValue(configurations, ReportingSettingsConfiguration.RESULT_DIRECTORY),
			 ConfigurationUtils.getConfigurationValue(configurations, ReportingSettingsConfiguration.SCREENSHOTS_DIRECTORY));
	}

	public DefaultReportingSettings(ReportingSettings reportingSettings) {
		if (reportingSettings != null) {
			init(reportingSettings.isProduceScreenshots(), reportingSettings.getResultDirectory(),
				 reportingSettings.getScreenshotsDirectory());
		}
	}
	
	public DefaultReportingSettings(Boolean produceScreenshots, String resultDirectory, String screenshotDirectory) {
		init(produceScreenshots, resultDirectory, screenshotDirectory);
	}
	
	private void init(Boolean produceScreenshots, String resultDirectory, String screenshotDirectory) {
		this.produceScreenshots = produceScreenshots;
		this.resultDirectory = resultDirectory;
		this.screenshotDirectory =  screenshotDirectory;
	}

	@Override
	public Boolean isProduceScreenshots() {
		return produceScreenshots;
	}

	@Override
	public String getResultDirectory() {
		return resultDirectory;
	}

	@Override
	public String getScreenshotsDirectory() {
		return screenshotDirectory;
	}
	
	public DefaultReportingSettings merge(ReportingSettings reportingSettings) {
		if (reportingSettings == null) {
			return this;
		}
		
		if (produceScreenshots == null) {
			this.produceScreenshots = reportingSettings.isProduceScreenshots();
		}
		
		if (resultDirectory == null) {
			this.resultDirectory = reportingSettings.getResultDirectory();
		}

		if (screenshotDirectory == null) {
			this.screenshotDirectory = reportingSettings.getScreenshotsDirectory();
		}

		return this;
	}
	
}