package sk.seges.acris.generator.server.rewriterules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.ContentInfoProvider;

public abstract class AbstractNiceURLGenerator implements INiceUrlGenerator {

	protected static final String NEW_LINE = System.getProperty("line.separator");

	protected static final Logger log = Logger.getLogger(AbstractNiceURLGenerator.class);

	@Autowired
	protected ContentInfoProvider[] contentInfoProviders;
	
	protected Set<ContentInfoProvider> contentInfoProvidersSet;

	protected String lang;
	
	@Autowired
	@Qualifier("url.redirect.single.file")
	protected Boolean redirectSingleFile;

	@Autowired
	@Qualifier("url.redirect.condition")
	protected Boolean redirectCondition;
	
	@Autowired
	@Qualifier("url.redirect.file.location")
	private String redirectFilePath;

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
	@Qualifier("legacy.url.redirect.single.file")
	private Boolean legacyRedirectSingleFile;

	@Autowired
	@Qualifier("legacy.url.redirect.file.location")
	private String legacyRedirectFilePath;

//	public void setContentProvidersFactory(ListFactoryBean contentProvidersFactory) {
//	    this.contentProvidersFactory = contentProvidersFactory;
//	    try {
//	        this.contentProviders = (List<ContentProvider>) contentProvidersFactory.getObject();    
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	    
//	}

	
	public void setContentProviders(ContentInfoProvider[] contentInfoProviders) {
		this.contentInfoProviders = contentInfoProviders;
	}

	protected abstract String getDefaultRewriteRule();
	protected abstract String getRewriteRule(String fromURL, String toURL);
	protected abstract String getFinalRewriteRule();

	protected String getRedirectRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	protected String getForwardRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	protected String getPermanentRedirectRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	private boolean generate(String lang, String niceUrl, String webId, Writer writer) {
		this.lang = lang;
		
		GeneratorToken content = new GeneratorToken();
		content.setLanguage(lang);
		content.setNiceUrl(niceUrl);
		content.setWebId(webId);

		boolean found = false;
		
	    for(ContentInfoProvider provider : contentInfoProvidersSet) {
        	if (provider.exists(content)) {
        		found = true;
        		break;
        	}
	    }
		
		if (!found) {
			log.error("Content for niceurl '" + niceUrl + "' does not exists.");
			return false;
		}
		
	    String pageName = "http://" + content.getWebId() + "/" +  content.getNiceUrl() + ".html";

	    try {
	        writer.write(getRewriteRule("#" + content.getNiceUrl(), pageName + "#" + content.getNiceUrl()));
	    } catch (IOException e) {
	        log.error("Unable to write rewrite rule for niceurl '" + niceUrl + "'. Target URL is '" + pageName + "#" + content.getNiceUrl() + "'");
	        return false;
	    }
		/**/
		return true;
	}

	protected BufferedWriter openOrCreateFile(String fileName) {
		File outputFile = new File(fileName);

		if (!outputFile.exists()) {
			try {
				if (!outputFile.createNewFile()) {
					log.error("Unable to create new empty file " + fileName);
					return null;
				}
			} catch (IOException ioe) {
				log.error("Exception occured while creating new empty file " + fileName, ioe);
				return null;
			}
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(outputFile, redirectSingleFile);
		} catch (FileNotFoundException e) {
			log.error("Exception occured while creating fileOutputStream for file " + fileName, e);
			return null;
		}

		return new BufferedWriter(new OutputStreamWriter(outputStream));
	}

	public void clearRewriteFile(String lang_country) {
		File file = null;
		
		if (redirectSingleFile) {
			file = new File(redirectFilePath);
			if (file.exists()) {
				file.delete();
			}
		} else {
			file = new File(redirectFilePath + "_" + lang_country);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	public boolean generate(String lang, String webId) {

		if(null == contentInfoProvidersSet) {
	        initializeContentProvidersSet();
	    }
	    
	    this.lang = lang;

		List<String> niceurls = new ArrayList<String>();

		for(ContentInfoProvider provider : contentInfoProvidersSet) {
		    List<String> availableNiceurls = provider.getAvailableNiceurls(webId, lang);

		    for(String niceurl : availableNiceurls) {
		    	niceurls.add(niceurl);
		    }
		}

		if (niceurls == null || niceurls.size() == 0) {
			if (log.isInfoEnabled()) {
				log.info("There are no niceurls available in the database");
			}
			return false;
		}

		BufferedWriter writer = null;
		
		boolean newFile = !(new File(redirectFilePath).exists());
		
		if (redirectSingleFile) {
			writer = openOrCreateFile(redirectFilePath);
		} else {
			writer = openOrCreateFile(redirectFilePath + "_" + lang);
		}
		
		if (writer == null) {
			if (log.isInfoEnabled()) {
				if (redirectSingleFile) {
					log.info("Unable to create buffered writer (from file '" + redirectFilePath  + "') for saving output");
				} else {
					log.info("Unable to create buffered writer (from file '" + redirectFilePath + "_" + lang + "') for saving output");
				}
			}
			return false;
		}

		if (newFile) {
			final String defaultRule = getDefaultRewriteRule(); 
			
			if (defaultRule != null && defaultRule.length() > 0) {
				try {
					writer.write(defaultRule);
				} catch (IOException ioe) {
					log.error("Unable to write default rewrite rule '" + defaultRule + "'");
					return false;
				}
			}
		
			final BaseURLGenerator baseURLGenerator = new BaseURLGenerator(this);
	
			try {
				writer.write(baseURLGenerator.getBaseURLs(webId));
			} catch (IOException e) {
				log.error("Unable to write base rewrite rules");
				return false;
			}
		}
		
		for (final String niceurl : niceurls) {
			if (!generate(lang, niceurl, webId, writer)) {
				if (redirectSingleFile) {
					log.error("Unable to generate and save nice URL for niceurl " + niceurl + ". Output file: "
						+ redirectFilePath + ". Continue with generating nice URL for the next token");
				} else {
					log.error("Unable to generate and save nice URL for niceurl " + niceurl + ". Output file: "
							+ redirectFilePath + "_" + lang + ". Continue with generating nice URL for the next token");
				}
			}
		}

		final LegacyURLGenerator legacyURLGenerator = new LegacyURLGenerator(this, legacyRedirectFilePath, legacyRedirectSingleFile);

		try {
			writer.write(legacyURLGenerator.getLegacyURLs(lang));
		} catch (IOException e) {
			log.error("Unable to write rewrite legacy rules");
			return false;
		}

		//TODO
		final String finalRule = getFinalRewriteRule(); 
		
		if (finalRule != null && finalRule.length() > 0) {
			try {
				writer.write(finalRule);
			} catch (IOException ioe) {
				log.error("Unable to write final rewrite rule '" + finalRule + "'");
				return false;
			}
		}
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			if (redirectSingleFile) {
				log.error("Unable to close output writer for file " + redirectFilePath, e);
			} else {
				log.error("Unable to close output writer for file " + redirectFilePath + "_" + lang, e);
			}
		}
		
		return true;
	}
	
	private void initializeContentProvidersSet() {
	    if(null == contentInfoProviders) {
	        throw new RuntimeException("ContentProviders not set - can't continue");
	    }
        throw new RuntimeException("Do it better");
//        contentInfoProvidersSet = new HashSet<ContentInfoProvider>(contentInfoProviders.length);
//        Set<Class> contentProviderClasses = new HashSet<Class>(contentInfoProviders.length);
//        for(ContentInfoProvider provider : contentInfoProviders) {
//            Class contentClass = provider.getContentClass();
//            if(!contentInfoProviderClasses.contains(contentClass)) {
//                contentInfoProviderClasses.add(contentClass);
//                contentInfoProvidersSet.add(provider);
//            }
//        }
	}

	public Boolean getRedirectCondition() {
		return redirectCondition;
	}

	public void setRedirectCondition(Boolean redirectCondition) {
		this.redirectCondition = redirectCondition;
	}
}