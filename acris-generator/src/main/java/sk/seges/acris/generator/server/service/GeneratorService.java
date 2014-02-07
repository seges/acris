package sk.seges.acris.generator.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.server.domain.TokenPersistentDataProvider;
import sk.seges.acris.generator.server.domain.api.PersistentDataProvider;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HTMLNodeSplitter;
import sk.seges.acris.generator.server.processor.HtmlPostProcessor;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;
import sk.seges.acris.generator.server.service.persist.api.DataPersister;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.acris.site.server.service.IWebSettingsServiceLocal;
import sk.seges.sesam.dao.*;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.io.*;
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

/**
 * @author Peter Simun (simun@seges.sk)
 */
@LocalService
public class GeneratorService implements IGeneratorServiceLocal {

	private HtmlProcessorFactory htmlProcessorFactory;
	private IWebSettingsServiceLocal webSettingsService;

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
			IWebSettingsServiceLocal webSettingsService, HtmlProcessorFactory htmlProcessorFactory, NodeParserFactory parserFactory) {
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
		
		ContentData content = contentDataProvider.getContent(result);
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

	@Override
	public ArrayList<String> getAvailableNiceurls(Page page) {
		List<String> availableNiceurls = contentDataProvider.getAvailableNiceurls(page);

		ArrayList<String> result = new ArrayList<String>();
		
		for (String niceUrl: availableNiceurls) {
			result.add(niceUrl);
		}
		
		if (log.isDebugEnabled()) {
			Criterion filterable = page.getFilterable();
			log.debug("Available tokens for page: " + page.toString() + ", " + toString(filterable));
			for (String niceUrl : result) {
				log.debug(niceUrl);
			}
		}

		return result;
	}

	private String toString(Criterion criterion) {
		
		if (criterion instanceof Junction) {
			List<Criterion> junctions = ((Junction)criterion).getJunctions();
			String result = "";
			String op = " and ";
			
			if (criterion instanceof Disjunction) {
				op = " or ";
			}

			int i = 0;
			for (Criterion cr: junctions) {
				if (i > 0) {
					result += op;
				}
				result = "( " + toString(cr) + " )";
				i++;
			}
			
			return result;
		}

		if (criterion instanceof SimpleExpression) {
			SimpleExpression<?> expression = ((SimpleExpression<?>)criterion);
			return expression.getProperty() + " " + expression.getOperation() + " " + expression.getValue().toString();
		}

		return criterion.toString();
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

	private void writeContent(String headerContent, String header, String contentWrapper, 
			String content, GeneratorToken token, GeneratorToken defaultToken, boolean indexFile) {
		
		HTMLNodeSplitter htmlNodeSplitter = new HTMLNodeSplitter(parserFactory);
		String doctype = htmlNodeSplitter.readDoctype(headerContent);

		if (log.isTraceEnabled()) {
			log.trace("			headerContent: " + headerContent);
		}
		
		String entryPoint = htmlNodeSplitter.joinHeaders(headerContent, header);
		entryPoint = htmlNodeSplitter.replaceBody(entryPoint, contentWrapper);
		String result = (doctype == null ? "" : ("<" + doctype + ">")) + htmlNodeSplitter.replaceRootContent(entryPoint, content);

		HtmlPostProcessor htmlPostProcessor = htmlProcessorFactory.create(webSettingsService.getWebSettings(token.getWebId()));
		
		result = htmlPostProcessor.getProcessedContent(result, token, defaultToken, indexFile);
		
		if (result == null) {
			log.error("Unable to process HTML nodes for nice-url " + token.getNiceUrl());
		} else {
			writeTextToFile(result, indexFile, token);
		}
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
					log.debug("Generating offline content for niceurl: " + token.getNiceUrl() + ", language: " + token.getLanguage() + " and webId: "
							+ token.getWebId());
				}
				
				if (log.isTraceEnabled()) {
					synchronized (this) { 
						log.trace("			entryPointFileName: " + entryPointFileName);
						log.trace("			header: " + header);
						log.trace("			contentWrapper: " + contentWrapper);
						log.trace("			content: " + content);
						log.trace("			currentServerURL: " + currentServerURL);
					}
				}
		
				String headerContent;
				
				if (entriesUrlCache.get(entryPointFileName) == null) {
					headerContent = readTextFromFile(entryPointFileName);
					entriesUrlCache.put(entryPointFileName, headerContent);
				} else {
					headerContent = entriesUrlCache.get(entryPointFileName);
				}

				GeneratorToken defaultGeneratorToken = getDefaultGeneratorToken(token.getLanguage(), token.getWebId());
				
				writeContent(headerContent, header, contentWrapper, content, token, defaultGeneratorToken, token.isDefaultToken());
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
		dataProvider.setAlias(token.getAlias());
		
		if (indexFile) {
			dataProvider.setId(indexFileName);
		} else {
			//TODO not indexFileName?
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