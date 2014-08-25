package sk.seges.acris.reporting.server.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.util.ServletUtils;
import sk.seges.sesam.pap.service.annotation.LocalService;

/**
 * communication with jasper server <br />
 * export of jasper reports
 * 
 * @author marta
 * 
 */

@LocalService
public class ReportingService implements IReportingLocalService {

	private static final long serialVersionUID = -7961507798873119425L;

	private static final Logger LOG = Logger.getLogger(ReportingService.class);

	private final ConfigurationProvider configuration;

	private final IReportDescriptionServiceLocal reportDescriptionService;

	public ReportingService(ConfigurationProvider configuration, IReportDescriptionServiceLocal reportDescriptionService) {
		this.configuration = configuration;
		this.reportDescriptionService = reportDescriptionService;
	}

	@Transactional
	@Override
	public String exportReportToHtml(Long reportDescriptionId, Map<String, String> parameters) {
		ReportDescriptionData report = reportDescriptionService.findById(reportDescriptionId);
		WSClient client = new WSClient(configuration.getJasperServerUrl() + "/services/repository",
				configuration.getJasperServerUser(), configuration.getJasperServerPassword());
		try {
			// parameters.put(Argument.RUN_OUTPUT_IMAGES_URI, "/images");
			JasperPrint print = client.runReport(report.getReportUrl(), parameters);
			HttpServletRequest request = ServletUtils.getRequest();
			HttpServletResponse response = ServletUtils.getResponse();

			JRExporter exporter = null;
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");

			exporter = new JRHtmlExporter();
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);
			StringWriter out = new StringWriter();
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
			exporter.exportReport();
			return out.getBuffer().toString();
		} catch (Exception ex) {
			LOG.error("error in proxying request to jasperserver", ex);
		}
		return null;
	}
	
	@Override
	@Transactional
	public String exportReport(Long reportDescriptionId, String exportType, Map<String, String> parameters,
			String web_id) {
		return exportReport(reportDescriptionId, exportType, (Map)parameters, web_id, "report" + "_" + new Date().getTime());
	}
	
	@Override
	public String exportReport(Long reportDescriptionId, String exportType, Map<String, Object> parameters,
			String webId, String reportName) {

		ReportDescriptionData report = reportDescriptionService.findById(reportDescriptionId);
		String params = "";
		params += generateParamsString(parameters);
		if (exportType == null) {
			exportType = ((String) parameters.get(ReportingConstants.OUTPUT)).toLowerCase();
		}

		String urlPath = generateCompleteReportUrl(exportType, report, params, configuration.getJasperServerUser(),
				configuration.getJasperServerPassword());
		if (LOG.isDebugEnabled()) {
			LOG.debug("Complete url = " + urlPath);
		}

		try {
			URL url = new URL(urlPath);
			URLConnection connection = url.openConnection();
			connection.setAllowUserInteraction(false);
			HttpURLConnection httpConnection = null;
			if (connection instanceof HttpURLConnection) {
				httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("POST");
				httpConnection.setInstanceFollowRedirects(false);
			}
			connection.connect();
			InputStream proxyIn = null;
			int code = 500;

			if (httpConnection != null) {
				proxyIn = httpConnection.getErrorStream();
				code = httpConnection.getResponseCode();
				if (code != HttpServletResponse.SC_OK) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					copy(proxyIn, baos);
					baos.close();
					throw new Exception("Status code from export " + code + ", content = " + baos.toString());
				}
			}

			if (proxyIn == null) {
				proxyIn = connection.getInputStream();
			}

			if (proxyIn != null) {
				String fileName = reportName + "." + exportType.toLowerCase();
				String reportDir = ReportingConstants.REPORTS_DIR;
				String directory = configuration.resolveRootDirectoryPath(webId) + "/" + reportDir;
				File d = new File(directory);
				if (!d.exists() || !d.isDirectory()) {
					d.mkdir();
				}
				String filePath = directory + "/" + fileName;
				if (LOG.isDebugEnabled()) {
					LOG.debug("Report output = " + filePath);
				}

				File f = new File(filePath);
				if(f.exists()){
					f.delete();
				}
				FileOutputStream fos = new FileOutputStream(f);
				copy(proxyIn, fos);
				fos.close();

				return reportDir + "/" + fileName;

			}

		} catch (Exception ex) {
			LOG.error("error in proxying request to jasperserver", ex);
		}

		return null;
	}

	protected String generateParamsString(Map<String, Object> parameters) {
		String params = "";
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				if (key != ReportingConstants.OUTPUT) {
					params += "&" + key + "=" + parameters.get(key);
				}
			}
		}
		return params;
	}

	protected String generateCompleteReportUrl(String exportType, ReportDescriptionData report, String params,
			String username, String password) {
		params += "&j_username=" + username + "&j_password=" + password;
		String prefix = configuration.getJasperServerUrl();
		String parameters = "/flow.html?_flowId=viewReportFlow&ndefined=&standAlone=true&ParentFolderUri=";
		String reportUrl = report.getReportUrl();
		if (!reportUrl.startsWith("/")) {
			reportUrl = "/" + reportUrl;
		}
		String parentUrl = reportUrl.substring(0, reportUrl.lastIndexOf("/"));
		if (parentUrl.length() <= 0)
		 {
			parentUrl = "undefined";
		// reportUrl = reportUrl.substring(reportUrl.lastIndexOf("/"));
		}

		String urlPath = prefix + parameters + parentUrl + "&reportUnit=" + reportUrl;
		return urlPath + "&output=" + exportType + params;
	}

	/**
	 * Copy all of in to out.
	 * 
	 * @param in
	 *            input stream to read from
	 * @param out
	 *            output stream to write to
	 * @throws IOException
	 *             thrown if things go wrong
	 */
	private void copy(InputStream in, OutputStream out) throws IOException {

		byte[] buffer = new byte[2 * 88192];
		int len = 0;
		while (true) {
			len = in.read(buffer, 0, buffer.length);
			if (len < 0) {
				break;
			}
			out.write(buffer, 0, len);
		}
	}
}
