package sk.seges.acris.generator.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import sk.seges.acris.etc.Countries;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorService;
import sk.seges.acris.generator.server.processor.ContentInfoProvider;
import sk.seges.acris.generator.server.processor.GWTHTMLHeaderProcessing;
import sk.seges.acris.generator.server.processor.HTMLPostProcessing;
import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.io.StringFile;

/**
 * @author fat
 *
 */
public class GeneratorService extends PersistentRemoteService implements IGeneratorService {

	private static final long serialVersionUID = 6944837756691206504L;
	
	@Autowired
	protected TokenProvider tokenProvider;

    @Autowired
	@Qualifier("virtual.server.name")
	protected String virtualServerName;
	
	@Autowired
	@Qualifier("virtual.server.port")
	protected Integer virtualServerPort;

	@Autowired
	@Qualifier("virtual.server.protocol")
	protected String virtualServerProtocol;

	@Autowired
	@Qualifier("locale.sensitive.server")
	protected Boolean localeSensitiveServer;

	@Autowired
	@Qualifier("google.analytics.script")
	protected String googleAnalyticsScript;

	@Autowired
	@Qualifier("offline.content.taget.path")
	protected String offlineContentTargetPath;

	@Autowired
	protected HTMLPostProcessing htmlPostProcessing;
	
	private ContentInfoProvider contentInfoProvider;
	
	public GeneratorService(ContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
	
	public GeneratorToken getLastProcessingToken() {
		return tokenProvider.getTokenForProcessing();
	}
	
	public void setTokenProvider(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}
	
	public boolean saveContent(GeneratorToken token, String contentText) {
		tokenProvider.setTokenForProcessing(token);
		return true;
	}

	public List<String> getAvailableNiceurls(String lang, String webId) {
		return contentInfoProvider.getAvailableNiceurls(lang, webId);
	}

	public String getDomainForLanguage(String webId, String language) {
		for (Countries country: Countries.values()) {
			if (language.equals(country.getLang())) {
				return country.getDomain();
			}
		}
		return null;
 	}

	public String getVirtualServerName() {
		return virtualServerName;
	}

	public Integer getVirtualServerPort() {
		return virtualServerPort;
	}

	public String getVirtualServerProtocol() {
		return virtualServerProtocol;
	}

	public Boolean isLocaleSensitiveServer() {
		return localeSensitiveServer;
	}
	
	public String getGoogleAnalyticsScript() {
		return googleAnalyticsScript;
	}

	public String readTextFromFile(String filename) {

		URL url;
		
		if (filename.startsWith("http://")) {
			try {
				url = new URL(filename);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Unable to load file: " + filename);
			}
	
			if (url == null) {
				throw new RuntimeException("File or directory should exists: " + filename);
			}

			try {
				return readTextFromURL(url);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} 

		final StringFile file = StringFile.getFileDescriptor(filename);

		try {
			return file.readTextFromFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String readTextFromURL(URL url) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuffer content = new StringBuffer();
		
		String line;

		while ((line = br.readLine()) != null) {
			content.append(line);
		}
	
		br.close();

		return content.toString();
	}

	protected String getOfflineContentTargetPath(GeneratorToken token) {
		return offlineContentTargetPath;
	}
	
	@Override
	public String getOfflineContentHtml(String headerFilename, String content,
			GeneratorToken token) {

		GWTHTMLHeaderProcessing htmlProcessing = new GWTHTMLHeaderProcessing(headerFilename);

		if (htmlPostProcessing.setProcessorContent(content, token.getWebId(), token.getLanguage())) {
			content = htmlPostProcessing.getHtml();	
		}

		String html = htmlProcessing.getDoctypeDefinition()
				+ htmlProcessing.getHtmlDefinition(token.getLanguage())
				+ htmlProcessing.readHeaderFromFile(contentInfoProvider.getContentKeywords(token), 
						contentInfoProvider.getContentDescription(token), 
						contentInfoProvider.getContentTitle(token), 
						token.getLanguage())
				+ content + "</html>";

		String htmlEndTags = "</BODY></html>";

		int endTagsIndex = html.toLowerCase().indexOf(htmlEndTags.toLowerCase());

		if (endTagsIndex != -1) {
			html = html.substring(0, endTagsIndex) + googleAnalyticsScript + html.substring(endTagsIndex);
		}

				
		return html;
	}

	@Override
	public void writeTextToFile(String content, GeneratorToken token) {
		File dirFile = new File(getOfflineContentTargetPath(token));

		if (dirFile == null) {
			throw new RuntimeException("File " + offlineContentTargetPath + " does not exists.");
		}

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Directory " + dirFile.getAbsolutePath() + " cannot be created.");
			}
		}
		
		StringFile file = new StringFile(dirFile, token.getNiceUrl());

		if (!file.exists()) {
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		try {
			file.writeTextToFile(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
}