package sk.seges.acris.generator.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HTMLNodeSplitter;
import sk.seges.acris.generator.server.processor.HtmlPostProcessor;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.domain.TokenPersistentDataProvider;
import sk.seges.acris.generator.shared.domain.api.PersistentDataProvider;
import sk.seges.acris.generator.shared.service.IGeneratorService;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.service.IWebSettingsService;

/**
 * @author Peter Simun (simun@seges.sk)
 */
public class GeneratorService implements IGeneratorService {

	private static final long serialVersionUID = 6944837756691206504L;

	private HtmlProcessorFactory htmlProcessorFactory;
	private IWebSettingsService webSettingsService;

	private static Log log = LogFactory.getLog(GeneratorService.class);

	private DataPersister dataPersister;
	private ContentDataProvider contentDataProvider;
	private NodeParserFactory parserFactory;

	private String indexFileName;
	private ThreadPoolExecutor threadPool;
	private Map<String, String> entriesUrlCache = new LinkedHashMap<String, String>(5, .75F, true) {

		private static final long serialVersionUID = -3755811610562830079L;

		protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
			return size() > 50;
		}
	};
	
	public GeneratorService(DataPersister dataPersister, String indexFileName, ContentDataProvider contentDataProvider, 
			IWebSettingsService webSettingsService, HtmlProcessorFactory htmlProcessorFactory, NodeParserFactory parserFactory) {
		this.dataPersister = dataPersister;
		this.indexFileName = indexFileName;
		this.parserFactory = parserFactory;
		this.htmlProcessorFactory = htmlProcessorFactory;
		this.contentDataProvider = contentDataProvider;
		this.webSettingsService = webSettingsService;
		this.threadPool = new ThreadPoolExecutor(5, 20, 20, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}

	public GeneratorToken getDefaultGeneratorToken(String language, String webId) {

		GeneratorToken result = new GeneratorToken();
				
		result.setWebId(webId);
		result.setLanguage(language);
		result.setDefaultToken(true);
		
		ContentData<?> content = contentDataProvider.getContent(result);
		if (content != null) {
			result.setNiceUrl(content.getNiceUrl());
		} else {
			result.setNiceUrl("");
		}
		
		WebSettingsData webSettings = webSettingsService.getWebSettings(result.getWebId());
		if (webSettings != null) {
			result.setWebId(webSettings.getTopLevelDomain());
		} else {
			result.setWebId("");
		}
		
		return result;
	}

	public boolean saveContent(GeneratorToken token, String contentText) {
		return true;
	}

	public ArrayList<String> getAvailableNiceurls(String lang, String webId) {
		List<String> availableNiceurls = contentDataProvider.getAvailableNiceurls(lang, webId);

		ArrayList<String> result = new ArrayList<String>();
		
		for (String niceUrl: availableNiceurls) {
			result.add(niceUrl);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Available tokens for webId: " + webId + " and language " + lang);
			for (String niceUrl : result) {
				log.debug(niceUrl);
			}
		}

		return result;
	}

	public Tuple<String, String> readHtmlBodyFromFile(String filename) {
		String content = readTextFromFile(filename);
		return new Tuple<String, String>(new HTMLNodeSplitter(parserFactory).getHeaderText(content), 
										 new HTMLNodeSplitter(parserFactory).getBody(content));
	}

	public synchronized String readTextFromFile(String filename) {

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

	private synchronized String readTextFromURL(URL url) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuffer content = new StringBuffer();

		String line;

		while ((line = br.readLine()) != null) {
			content.append(line);
		}

		br.close();

		return content.toString();
	}

	@Override
	public void writeOfflineContentHtml(final String entryPointFileName, final String header, final String contentWrapper, 
			final String htmlContent, final GeneratorToken token, final String currentServerURL) {

		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				String content = htmlContent;
				
				if (content != null) {
					content = content.replaceAll("<br></br>", "<br/>");
				}
		
				if (log.isDebugEnabled()) {
					synchronized (this) { 
						log.debug("Generating offline content for niceurl: " + token.getNiceUrl() + ", language: " + token.getLanguage() + " and webId: "
								+ token.getWebId());
						log.debug("			entryPointFileName: " + entryPointFileName);
						log.debug("			header: " + header);
						log.debug("			contentWrapper: " + contentWrapper);
						log.debug("			content: " + content);
						log.debug("			currentServerURL: " + currentServerURL);
					}
				}
		
				String headerContent;
				
				if (entriesUrlCache.get(entryPointFileName) == null) {
					headerContent = readTextFromFile(entryPointFileName);
					entriesUrlCache.put(entryPointFileName, headerContent);
				} else {
					headerContent = entriesUrlCache.get(entryPointFileName);
				}
				
				HTMLNodeSplitter htmlNodeSplitter = new HTMLNodeSplitter(parserFactory);
				String doctype = htmlNodeSplitter.readDoctype(headerContent);
		
				if (log.isDebugEnabled()) {
					log.debug("			headerContent: " + headerContent);
				}
				
				String entryPoint = htmlNodeSplitter.joinHeaders(headerContent, header);
				entryPoint = htmlNodeSplitter.replaceBody(entryPoint, contentWrapper);
				content = (doctype == null ? "" : ("<" + doctype + ">")) + htmlNodeSplitter.replaceRootContent(entryPoint, content);
		
				String result = content;
		
				HtmlPostProcessor htmlPostProcessor = htmlProcessorFactory.create(webSettingsService.getWebSettings(token.getWebId()));
				
				result = htmlPostProcessor.getProcessedContent(content, token, false);
				
				if (result == null) {
					log.error("Unable to process HTML nodes for nice-url " + token.getNiceUrl());
				} else {
					
					writeTextToFile(result, false, token);

					if (token.isDefaultToken()) {
						entryPoint = new HTMLNodeSplitter(parserFactory).joinHeaders(headerContent, header);
						entryPoint = new HTMLNodeSplitter(parserFactory).replaceBody(entryPoint, contentWrapper);
						content = (doctype == null ? "" : ("<" + doctype + ">")) + htmlNodeSplitter.replaceRootContent(entryPoint, content);
			
						result = htmlPostProcessor.getProcessedContent(content, token, true);
						if (result != null) {
							writeTextToFile(result, true, token);
						} else {
							log.error("Unable to process HTML nodes for default nice-url " + token.getNiceUrl());
						}
					}
				}
			}
		});
	}

	/*
	 * Method is used to encode name of the file 
	 */
	protected String encodeFilePath(String path) {
		 try {
			path = URLEncoder.encode(path, Charset.defaultCharset().toString());
			path = path.replaceAll("%2F","/");
			path = path.replaceAll("%2f","/");
		} catch (UnsupportedEncodingException e) {
			log.info("Unable to encode file path " + path);
		}  

		return path;
	}
	

	protected PersistentDataProvider createPersistentDataProvider(GeneratorToken token, boolean indexFile, String content) {
		PersistentDataProvider dataProvider = new TokenPersistentDataProvider();
		dataProvider.setContent(content);
		dataProvider.setWebId(token.getWebId());
		if (indexFile) {
			dataProvider.setId(indexFileName);
		} else {
			dataProvider.setId(token.getNiceUrl() + File.separator + "index.html");
		}
		return dataProvider;
	}

	protected void writeTextToFile(String content, boolean indexFile, GeneratorToken token) {
		if (log.isDebugEnabled()) {
			log.debug("Writing offline content for nice-url " + token.getNiceUrl() + " [ " + token.getLanguage() + " ] for " + token.getWebId());
		}

		synchronized (dataPersister) {
			dataPersister.writeTextToFile(createPersistentDataProvider(token, indexFile, content));
		}
	}
}