package sk.seges.acris.generator.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import sk.seges.acris.etc.Countries;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorService;
import sk.seges.acris.generator.server.processor.ContentInfoProvider;
import sk.seges.acris.generator.server.processor.GWTHTMLProcessing;
import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.io.StringFile;

/**
 * @author fat
 *
 */
public class GeneratorService extends PersistentRemoteService implements IGeneratorService {

	private static final long serialVersionUID = 6944837756691206504L;
	
	@Autowired
	private TokenProvider tokenProvider;

    @Autowired
	@Qualifier("virtual.server.name")
	private String virtualServerName;
	
	@Autowired
	@Qualifier("virtual.server.port")
	private Integer virtualServerPort;

	@Autowired
	@Qualifier("virtual.server.protocol")
	private String virtualServerProtocol;

	@Autowired
	@Qualifier("locale.sensitive.server")
	private Boolean localeSensitiveServer;

	@Autowired
	@Qualifier("google.analytics.script")
	private String googleAnalyticsScript;

	@Autowired
	@Qualifier("offline.content.taget.path")
	private String offlineContentTargetPath;

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

	public List<String> getAvailableTokens(String lang, String webId) {
		return contentInfoProvider.getAvailableTokens(lang, webId);
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

	public void writeTextToFile(String headerFilename, String content, GeneratorToken token) {

		File dirFile = new File(offlineContentTargetPath);

		if (dirFile == null) {
			throw new RuntimeException("File " + offlineContentTargetPath + " does not exists.");
		}
		
		StringFile file = new StringFile(dirFile, "#" + contentInfoProvider.getNiceUrl(token) + ".html");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		GWTHTMLProcessing htmlProcessing = new GWTHTMLProcessing(headerFilename);

		String html = htmlProcessing.getDoctypeDefinition()
				+ htmlProcessing.getHtmlDefinition(token.getLanguage())
				+ htmlProcessing.readHeaderFromFile(contentInfoProvider.getContentKeywords(token), 
						contentInfoProvider.getContentDescription(token), 
						contentInfoProvider.getContentTitle(token), 
						token.getLanguage())
				+ content + "</html>";

		Map<String, String> languages = contentInfoProvider.getSupportedLanguagesWithDomains(token.getWebId());

		for (Entry<String, String> supportedLanguage : languages.entrySet()) {
			
			GeneratorToken localizedToken = new GeneratorToken();
			localizedToken.setToken(token.getToken());
			localizedToken.setWebId(token.getWebId());
			localizedToken.setLanguage(supportedLanguage.getKey());
			
			final boolean contentExists = contentInfoProvider.exists(localizedToken);

			if (!contentExists) {
				continue;
			}

			if (localeSensitiveServer) {
				int langBarIndex = html.indexOf("junit.html?locale=" + supportedLanguage.getKey());

				if (langBarIndex != -1) {
					langBarIndex = langBarIndex - languages.get(token.getLanguage()).length() - 1;

					html = html.substring(0, langBarIndex) + supportedLanguage.getValue()
							+ html.substring(langBarIndex + languages.get(token.getLanguage()).length());
				}
			}

			html = html.replace("junit.html?locale=" + supportedLanguage.getKey(), contentInfoProvider.getNiceUrl(localizedToken));
		}

		String htmlEndTags = "</BODY></html>";

		int endTagsIndex = html.toLowerCase().indexOf(htmlEndTags.toLowerCase());

		if (endTagsIndex != -1) {
			html = html.substring(0, endTagsIndex) + googleAnalyticsScript + html.substring(endTagsIndex);
		}

		try {
			file.writeTextToFile(html);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
