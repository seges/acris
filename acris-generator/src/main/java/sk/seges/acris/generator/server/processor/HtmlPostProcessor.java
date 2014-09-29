package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import sk.seges.acris.generator.server.processor.factory.CustomPrototypicalNodeFactory;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;
import sk.seges.acris.generator.server.processor.model.api.DefaultGeneratorEnvironment;
import sk.seges.acris.generator.server.processor.model.api.DefaultNodesContext;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.PostProcessorKind.Kind;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public class HtmlPostProcessor {

	private static final Logger log = Logger.getLogger(HtmlPostProcessor.class);
	
	private Collection<AbstractElementPostProcessor> postProcessors;

	private WebSettingsData webSettings;
	private ContentDataProvider contentMetaDataProvider;
	private PostProcessorActivator postProcessorActivator;
	private NodeParserFactory parserFactory;
	
	public HtmlPostProcessor(Collection<AbstractElementPostProcessor> postProcessors, PostProcessorActivator postProcessorActivator, 
			ContentDataProvider contentMetaDataProvider, WebSettingsData webSettings, NodeParserFactory parserFactory) {
		this.postProcessors = postProcessors;
		this.parserFactory = parserFactory;
		this.contentMetaDataProvider = contentMetaDataProvider;
		this.webSettings = webSettings;
		this.postProcessorActivator = postProcessorActivator;

		assert postProcessors != null;
		assert parserFactory != null;
		assert contentMetaDataProvider != null;
		assert webSettings != null;
		assert postProcessorActivator != null;
	}

	public String getProcessedContent(final String content, GeneratorToken token, GeneratorToken defaultToken, boolean indexFile) {
		if (postProcessors == null || postProcessors.size() == 0) {
			log.warn("No HTML post processor register");
		}

		List<Node> rootNodes = new ArrayList<Node>();

		Parser parser = parserFactory.createParser(content);
		parser.setNodeFactory(new CustomPrototypicalNodeFactory());
		
		try {
			NodeIterator nodeIterator = parser.elements();
			return processNodes(nodeIterator, rootNodes, token, defaultToken, indexFile);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	protected GeneratorEnvironment getGeneratorEnvironment(GeneratorToken generatorToken, GeneratorToken defaultToken, boolean indexFile) {
		ContentData content = contentMetaDataProvider.getContent(generatorToken);
		DefaultGeneratorEnvironment result = new DefaultGeneratorEnvironment(webSettings, generatorToken, defaultToken, content, indexFile);
		result.setNodesContext(new DefaultNodesContext());
		return result;
	}
	
	private String processNodes(NodeIterator nodeIterator, List<Node> rootNodes, GeneratorToken token, GeneratorToken defaultToken, boolean indexFile) throws ParserException {
		
		GeneratorEnvironment generatorEnvironment = getGeneratorEnvironment(token, defaultToken, indexFile);
		
		while (nodeIterator.hasMoreNodes()) {
			Node node = nodeIterator.nextNode();

			if (node == null) {
				continue;
			}

			if (log.isTraceEnabled()) {
				log.trace("Processing node " + node.toString());
			}

			if (node instanceof Html && generatorEnvironment.getNodesContext().getHtmlNode() == null) {
				generatorEnvironment.getNodesContext().setHtmlNode((Html) node);
			}

			rootNodes.add(node);

			executeProcessors(node, generatorEnvironment);

			processNodes(node.getChildren(), generatorEnvironment);
		}
		
		if (rootNodes != null && rootNodes.size() > 0) {
			return getHtml(rootNodes);
		}
		
		return null;
	}

	private void executeProcessors(Node node, GeneratorEnvironment generatorEnvironment) {

		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			if (elementPostProcessor.getKind().equals(Kind.APPENDER)) {
				if (postProcessorActivator.isActive(elementPostProcessor, generatorEnvironment.getGeneratorToken().isDefaultToken()) && elementPostProcessor.supports(node, generatorEnvironment)) {
					elementPostProcessor.process(node, generatorEnvironment);
				}
			}
		}

		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			if (elementPostProcessor.getKind().equals(Kind.ALTER)) {
				if (postProcessorActivator.isActive(elementPostProcessor, generatorEnvironment.getGeneratorToken().isDefaultToken()) && elementPostProcessor.supports(node, generatorEnvironment)) {
					elementPostProcessor.process(node, generatorEnvironment);
				}
			}
		}

		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			if (elementPostProcessor.getKind().equals(Kind.ANNIHILATOR)) {
				if (postProcessorActivator.isActive(elementPostProcessor, generatorEnvironment.getGeneratorToken().isDefaultToken()) && elementPostProcessor.supports(node, generatorEnvironment)) {
					elementPostProcessor.process(node, generatorEnvironment);
				}
			}
		}
	}
	
	private void processNodes(NodeList nodeList, GeneratorEnvironment generatorEnvironment) throws ParserException {
		if (nodeList == null) {
			return;
		}
		
		int currentSize = nodeList.size();

		for (int i = 0; i < currentSize; i++) {
			Node node = nodeList.elementAt(i);

			if (node instanceof HeadTag && generatorEnvironment.getNodesContext().getHeadNode() == null) {
				generatorEnvironment.getNodesContext().setHeadNode((HeadTag) node);
			}

			executeProcessors(node, generatorEnvironment);
			
			if (nodeList.size() < currentSize) {
				//node was removed
				i = i - currentSize + nodeList.size();
				currentSize = nodeList.size();
			}

			processNodes(node.getChildren(), generatorEnvironment);
		}
	}

	private String getHtml(List<Node> rootNodes) {

		String result = "";
		
		for (Node node : rootNodes) {
			result += node.toHtml();
		}

		return result;
	}
}