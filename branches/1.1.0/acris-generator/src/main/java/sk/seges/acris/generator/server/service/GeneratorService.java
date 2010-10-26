package sk.seges.acris.generator.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorService;
import sk.seges.acris.generator.server.processor.HTMLNodeSplitter;
import sk.seges.acris.generator.server.processor.HtmlPostProcessing;
import sk.seges.acris.generator.server.processor.IContentInfoProvider;
import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.util.Tuple;

/**
 * @author psimun
 *
 */
public class GeneratorService implements IGeneratorService {

	private static final long serialVersionUID = 6944837756691206504L;
	
	protected TokenProvider tokenProvider;

	protected String offlineContentTargetPath;

	protected HtmlPostProcessing htmlPostProcessing;
	
	private static Log log = LogFactory.getLog(GeneratorService.class);

	private IContentInfoProvider contentInfoProvider;
	
	private String indexFileName;
	
	public GeneratorService(HtmlPostProcessing htmlPostProcessing, String offlineContentTargetPath, String indexFileName, TokenProvider tokenProvider,
			IContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
		this.indexFileName = indexFileName;
		this.htmlPostProcessing = htmlPostProcessing;
		this.offlineContentTargetPath = offlineContentTargetPath;
		this.tokenProvider = tokenProvider;
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
		List<String> result = contentInfoProvider.getAvailableNiceurls(lang, webId);
		
		if (log.isDebugEnabled()) {
			log.debug("Available tokens for webId: " + webId + " and language " + lang);
			for (String niceUrl: result) {
				log.debug(niceUrl);
			}
		}
		
		return result;
	}

	public Tuple<String, String> readHtmlBodyFromFile(String filename) {
		String content = readTextFromFile(filename);
		return new Tuple<String, String>(new HTMLNodeSplitter().getHeaderText(content), new HTMLNodeSplitter().getBody(content));
	}
	
	public String readTextFromFile(String filename) {

		URL url = null;
		
		if (filename.startsWith("http://")) {
			try {
				url = new URL(filename);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Unable to load file: " + filename);
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
	public String getOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content,
			GeneratorToken token, String currentServerURL) {

		if (content != null) {
			content = content.replaceAll("<br></br>","<br/>");
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Generating offline content for niceurl: " + token.getNiceUrl() + ", language: " + 
					token.getLanguage() + " and webId: " + token.getWebId());
			log.debug("			entryPointFileName: " + entryPointFileName);
			log.debug("			header: " + header);
			log.debug("			contentWrapper: " + contentWrapper);
			log.debug("			content: " + content);
			log.debug("			currentServerURL: " + currentServerURL);
		}

		String headerContent = readTextFromFile(entryPointFileName);
		String doctype = new HTMLNodeSplitter().readDoctype(headerContent);

		if (log.isDebugEnabled()) {
			log.debug("			headerContent: " + headerContent);
		}
		
		String entryPoint = new HTMLNodeSplitter().joinHeaders(headerContent, header);
		entryPoint = new HTMLNodeSplitter().replaceBody(entryPoint, contentWrapper);
		content = (doctype == null ? "" : ("<" + doctype + ">")) + new HTMLNodeSplitter().replaceRootContent(entryPoint, content);

		String result = content;
		
		if (htmlPostProcessing.setProcessorContent(content, token, contentInfoProvider)) {
			result = htmlPostProcessing.getHtml();	
		}

		if (contentInfoProvider.isDefaultContent(token)) {
			token.setNiceUrl(GeneratorToken.DEFAULT_TOKEN);
			entryPoint = new HTMLNodeSplitter().joinHeaders(headerContent, header);
			entryPoint = new HTMLNodeSplitter().replaceBody(entryPoint, contentWrapper);
			content = (doctype == null ? "" : ("<" + doctype + ">")) + new HTMLNodeSplitter().replaceRootContent(entryPoint, content);

			if (htmlPostProcessing.setProcessorContent(content, token, contentInfoProvider)) {
				writeDefaultContentTextToFile(htmlPostProcessing.getHtml(), token);
			}
		}

		return result;
	}

	protected StringFile createDefaultFile(File dirFile) {
		return createFile(dirFile, indexFileName);
	}
	
	protected StringFile createFile(File dirFile, String filename) {
		StringFile file = new StringFile(dirFile, filename);

		if (!file.exists()) {
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
					if (log.isDebugEnabled()) {
						log.debug("Directory " + file.getParentFile().getAbsolutePath() + " does not exists. Creating a new file.");
					}
				}
				if (log.isDebugEnabled()) {
					log.debug("File " + file.getAbsolutePath() + " does not exists. Creating an empty new file.");
				}

				if (!file.createNewFile()) {
					log.error("Unable to create empty file " + file.getAbsolutePath() + ".");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return file;
	}

	protected void writeDefaultContentTextToFile(String content, GeneratorToken token) {
		File dirFile = new File(getOfflineContentTargetPath(token));

		StringFile file = createDefaultFile(dirFile);

		if (log.isDebugEnabled()) {
			log.debug("Writing offline content for nice-url " + token.getNiceUrl() + " [ " + 
					token.getLanguage() + " ] for " + token.getWebId() + " to file " + file.getAbsolutePath());
		}

		try {
			file.writeTextToFile(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void writeTextToFile(String content, GeneratorToken token) {
		File dirFile = new File(getOfflineContentTargetPath(token));

		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Directory " + dirFile.getAbsolutePath() + " cannot be created.");
			}
		}
		
		StringFile file = createFile(dirFile, token.getNiceUrl() + File.separator + "index.html");

		if (log.isDebugEnabled()) {
			log.debug("Writing offline content for nice-url " + token.getNiceUrl() + " [ " + 
					token.getLanguage() + " ] for " + token.getWebId() + " to file " + file.getAbsolutePath());
		}

		try {
			file.writeTextToFile(content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}