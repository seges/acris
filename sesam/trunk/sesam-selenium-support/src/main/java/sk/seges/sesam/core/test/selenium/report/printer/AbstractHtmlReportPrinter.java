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

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.SupportHelper;
import sk.seges.sesam.core.test.selenium.report.model.api.ReportData;
import sk.seges.sesam.core.test.selenium.report.model.api.TemplateLocation;

public abstract class AbstractHtmlReportPrinter<T extends ReportData> extends SupportHelper<T> implements ReportPrinter<T>, LogChute {

	protected ReportSettings reportSettings;

	protected VelocityEngine ve;
	protected VelocityContext context;

	private BufferedWriter writer;

	protected AbstractHtmlReportPrinter(ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
	}

	protected boolean isHtmlReportEnabled() {
		if (reportSettings.getHtml() == null) {
			return false;
		}
		
		if (reportSettings.getHtml().getSupport() == null) {
			return false;
		}

		return (reportSettings.getHtml().getSupport().getEnabled() != null && reportSettings.getHtml().getSupport().getEnabled() == true);
	}

	protected abstract String getDefaultTemplatePath();
	protected abstract String getTemplateRawPath(ReportSettings settings);
	
	private String getTemplatePath() {
		if (!isHtmlReportEnabled()) {
			return getDefaultTemplatePath();
		}
		
		String templatePath = getTemplatePath(getTemplateRawPath(reportSettings));
		if (templatePath == null) {
			return getDefaultTemplatePath();
		}
		return templatePath;
	}

	protected String getTemplatePath(String path) {
		if (getTemplateLocation().equals(TemplateLocation.CLASSPATH)) {
			return path.substring(Report.CLASSPATH_TEMPLATE_PREFIX.length());
		}
		return path;
	}
	
	protected TemplateLocation getTemplateLocation() {
		if (getTemplateRawPath(reportSettings).startsWith(Report.CLASSPATH_TEMPLATE_PREFIX)) {
			return TemplateLocation.CLASSPATH;
		}
		
		return TemplateLocation.FILE;
	}

	@Override
	public void initialize(T resultData) {
		if (!isHtmlReportEnabled()) {
			return;
		}

		if (ve != null) {
			//already initialized
			return;
		}
		
		try {
			
			setResultData(resultData);

			ve = new VelocityEngine();
	
			context = new VelocityContext();
			context.put("result", resultData);

			ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
			
			if (getTemplateLocation().equals(TemplateLocation.CLASSPATH)) {
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			} else {
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
			}
			
			ve.init();
			
		} catch (Exception e) {
        	System.out.println(e);
			throw new RuntimeException(e);
		}

		getOutputDirectory(reportSettings.getHtml().getSupport());
	}
	
	protected abstract String getReportFileName(T resultData);
	
	@Override
	public void print(T resultData) {
		if (!isHtmlReportEnabled()) {
			return;
		}

		if (writer != null) {
			//already initialized
			return;
		}
		
		String fileName = getOutputDirectory(reportSettings.getHtml().getSupport()) + File.separator + getReportFileName(resultData);
        File reportFile = new File(getResultDirectory(), fileName);
        try {
        	if (!reportFile.getParentFile().exists()) {
        		reportFile.getParentFile().mkdirs();
        	}
	        reportFile.createNewFile();
	        String outFileName = reportFile.getAbsolutePath();
	        resultData.setFileName(fileName);
			writer = new BufferedWriter(new FileWriter(new File(outFileName)));
		} catch (IOException e) {
        	System.out.println(e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void finish(T resultData) {
		if (!isHtmlReportEnabled()) {
			return;
		}

		if (writer == null || ve == null) {
			//already finished or not initialized
			return;
		}
		
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
	        try {
				writer.close();
			} catch (IOException e1) {}
        	System.out.println(e);
			throw new RuntimeException(e);
		}
        
        writer = null;
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