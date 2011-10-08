package sk.seges.sesam.core.test.selenium.report.printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.AbstractReportHelper;
import sk.seges.sesam.core.test.selenium.report.model.TestResult;

public class HtmlReportPrinter extends AbstractReportHelper implements ReportPrinter, LogChute {

	enum TemplateLocation {
		FILE("file"), CLASSPATH("classpath");
		
		String loader;
		
		TemplateLocation(String loader) {
			this.loader = loader;
		}
		
		public String getLoader() {
			return loader;
		}
	}
	
	private static final String CLASSPATH_TEMPLATE_PREFIX = "classpath:";
	private static final String DEFAULT_TEMPLATE_FILE = CLASSPATH_TEMPLATE_PREFIX + "sk/seges/sesam/selenium/report/standard/default.vm";

	private ReportSettings reportSettings;

	private String testMethod;
	private BufferedWriter writer;
	
	private VelocityEngine ve;
	private VelocityContext context;
	
	public HtmlReportPrinter(ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
	}

	private TemplateLocation getTemplateLocation() {
		if (getTemplateRawPath().startsWith(CLASSPATH_TEMPLATE_PREFIX)) {
			return TemplateLocation.CLASSPATH;
		}
		
		return TemplateLocation.FILE;
	}

	private String getTemplatePath() {
		if (getTemplateLocation().equals(TemplateLocation.CLASSPATH)) {
			return getTemplateRawPath().substring(CLASSPATH_TEMPLATE_PREFIX.length());
		}
		return getTemplateRawPath();
	}
	
	private String getTemplateRawPath() {
		String templatePath = reportSettings.getHtml().getTemplatePath();		
		if (templatePath == null) {
			return DEFAULT_TEMPLATE_FILE;
		}
		return templatePath;
	}
	
	@Override
	public void initialize(ReportSettings reportSettings, TestResult testInfo) {
		try {
			ve = new VelocityEngine();
	
			context = new VelocityContext();
			context.put("result", testInfo);

			ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
			
			if (getTemplateLocation().equals(TemplateLocation.CLASSPATH)) {
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			} else {
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
			}
			
			ve.init();
		
	        testMethod = null;
	
		} catch (Exception e) {
        	System.out.println(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void print(TestResult testInfo) {
		if (testMethod == null) {
			testMethod = testInfo.getTestMethod();

			if (testMethod != null) {
		        File reportFile = new File(getResultDirectory() + reportSettings.getHtml().getSupport().getDirectory(), testInfo.getTestCase().getSimpleName() + "_" + testMethod + "_" + getTimeStamp() + ".html");
		        try {
		        	if (!reportFile.getParentFile().exists()) {
		        		reportFile.getParentFile().mkdirs();
		        	}
			        reportFile.createNewFile();
			        String outFileName = reportFile.getAbsolutePath();
					writer = new BufferedWriter(new FileWriter(new File(outFileName)));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void finish(TestResult testInfo) {

        InputStream input = this.getClass().getClassLoader().getResourceAsStream(getTemplatePath());
        if (input == null) {
        	return;
        }

        InputStreamReader reader = new InputStreamReader(input);

        try {
			if (!ve.evaluate(context, writer, getTemplatePath(), reader)) {
			    throw new RuntimeException("Failed to convert the template into html.");
			}

	        writer.flush();
	        writer.close();
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void init(RuntimeServices arg0) throws Exception {
	}

	@Override
	public boolean isLevelEnabled(int arg0) {
		return true;
	}

	@Override
	public void log(int arg0, String arg1) {
		System.out.println(arg1);
	}

	@Override
	public void log(int arg0, String arg1, Throwable arg2) {
		System.out.println(arg1 + arg2);
	}
}