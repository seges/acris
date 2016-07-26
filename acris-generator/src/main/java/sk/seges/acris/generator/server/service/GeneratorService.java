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
import sk.seges.acris.site.shared.service.IWebSettingsLocalService;
import sk.seges.sesam.dao.*;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
	private IWebSettingsLocalService webSettingsService;

	private static Log log = LogFactory.getLog(GeneratorService.class);

	private final DataPersister dataPersister;
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
			IWebSettingsLocalService webSettingsService, HtmlProcessorFactory htmlProcessorFactory, NodeParserFactory parserFactory) {
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
				result += "( " + toString(cr) + " )";
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

		URL url;

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

        StringBuilder content = new StringBuilder();

		String line;

		while ((line = br.readLine()) != null) {
			content.append(line);
		}

		br.close();

		return content.toString();
	}

	private void writeContent(String headerContent, String header, String contentWrapper, 
			String content, GeneratorToken token, GeneratorToken defaultToken, boolean indexFile, String defaultLocale) {
		
		HTMLNodeSplitter htmlNodeSplitter = new HTMLNodeSplitter(parserFactory);
		String doctype = htmlNodeSplitter.readDoctype(headerContent);

		if (log.isTraceEnabled()) {
			log.trace("			headerContent: " + headerContent);
		}
		
		String entryPoint = htmlNodeSplitter.joinHeaders(headerContent, header);
		entryPoint = htmlNodeSplitter.replaceBody(entryPoint, contentWrapper);
		String result = (doctype == null ? "" : ("<" + doctype + ">")) + htmlNodeSplitter.replaceRootContent(entryPoint, content);

		HtmlPostProcessor htmlPostProcessor = htmlProcessorFactory.create(webSettingsService.getWebSettings(token.getWebId()));
		
		result = htmlPostProcessor.getProcessedContent(result, token, defaultToken, indexFile, defaultLocale);
		
		if (result == null) {
			log.error("Unable to process HTML nodes for nice-url " + token.getNiceUrl());
		} else {
			writeTextToFile(result, indexFile, token);
		}
	}

    private abstract class GeneratorRunnable implements Runnable {
        protected final String entryPointFileName;
        protected final String header;
        protected final String contentWrapper;
        protected final String htmlContent;
        protected final GeneratorToken token;
        protected final String currentServerURL;
        protected final String defaultLocale;

        public GeneratorRunnable(final String entryPointFileName, final String header, final String contentWrapper,
                                 final String htmlContent, final GeneratorToken token, final String currentServerURL, final String defaultLocale) {

            this.entryPointFileName = entryPointFileName;
            this.header = header;
            this.contentWrapper = contentWrapper;
            this.htmlContent = htmlContent;
            this.token = token;
            this.currentServerURL = currentServerURL;
            this.defaultLocale = defaultLocale;
        }
    }

	@Override
	public void writeOfflineContentHtml(final String entryPointFileName, final String header, final String contentWrapper,
			final String htmlContent, final GeneratorToken token, final String currentServerURL, final String defaultLocale) {

		threadPool.execute(new GeneratorRunnable(entryPointFileName, header, contentWrapper, htmlContent, token, currentServerURL, defaultLocale) {
			@Override
			public void run() {
				String content = this.htmlContent;
				
				if (content != null) {
					content = content.replaceAll("<br></br>", "<br/>");
				}

				if (log.isDebugEnabled()) {
					log.debug("Generating offline content for niceurl: " + this.token.getNiceUrl() + ", language: " + this.token.getLanguage() + " and webId: "
							+ this.token.getWebId() + ". Token is " + (token.isDefaultToken() ? "" : "not ") + "default.");
				}
				
				if (log.isTraceEnabled()) {
					synchronized (this) { 
						log.trace("			entryPointFileName: " + this.entryPointFileName);
						log.trace("			header: " + this.header);
						log.trace("			contentWrapper: " + this.contentWrapper);
						log.trace("			content: " + content);
						log.trace("			currentServerURL: " + this.currentServerURL);
					}
				}
		
				String headerContent;
				
				if (entriesUrlCache.get(this.entryPointFileName) == null) {
					headerContent = readTextFromFile(this.entryPointFileName);
					entriesUrlCache.put(this.entryPointFileName, headerContent);
				} else {
					headerContent = entriesUrlCache.get(this.entryPointFileName);
				}

				GeneratorToken defaultGeneratorToken = getDefaultGeneratorToken(this.token.getLanguage(), this.token.getWebId());

				writeContent(headerContent, this.header, this.contentWrapper, content, this.token, defaultGeneratorToken, this.token.isDefaultToken(), this.defaultLocale);
			}
		});
	}

	protected PersistentDataProvider createPersistentDataProvider(GeneratorToken token, boolean indexFile, String content) {
		PersistentDataProvider dataProvider = new TokenPersistentDataProvider();
		dataProvider.setContent(content);
		dataProvider.setWebId(token.getWebId());
		dataProvider.setAlias(token.getAlias());
		
		if (indexFile) {
			dataProvider.setId(indexFileName);
		} else {
			dataProvider.setId(token.getNiceUrl() + File.separator + indexFileName);
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