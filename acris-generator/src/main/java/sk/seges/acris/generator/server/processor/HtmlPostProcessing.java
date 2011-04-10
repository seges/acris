package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.factory.api.ParserFactory;
import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;
import sk.seges.acris.generator.server.processor.model.api.DefaultGeneratorEnvironment;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class HtmlPostProcessing {

	private static final Logger log = Logger.getLogger(HtmlPostProcessing.class);
	
	private Collection<AbstractElementPostProcessor> postProcessors;
	private WebSettingsData webSettings;
	private ContentDataProvider contentMetaDataProvider;
	private PostProcessorActivator postProcessorActivator;
	private ParserFactory parserFactory;
	
	public HtmlPostProcessing(Collection<AbstractElementPostProcessor> postProcessors, PostProcessorActivator postProcessorActivator, 
			ContentDataProvider contentMetaDataProvider, WebSettingsData webSettings, ParserFactory parserFactory) {
		this.postProcessors = postProcessors;
		this.parserFactory = parserFactory;
		this.contentMetaDataProvider = contentMetaDataProvider;
		this.webSettings = webSettings;
		this.postProcessorActivator = postProcessorActivator;
	}

	public String getProcessedContent(final String content, GeneratorToken token) {
		if (postProcessors == null || postProcessors.size() == 0) {
			throw new RuntimeException("No HTML postprocessor registered.");
		}

		List<Node> rootNodes = new ArrayList<Node>();

		Parser parser = parserFactory.createParser(content);
		
		PrototypicalNodeFactory prototypicalNodeFactory = (PrototypicalNodeFactory)parser.getNodeFactory();
		prototypicalNodeFactory.registerTag(new StyleLinkTag());
		
		try {
			NodeIterator nodeIterator = parser.elements();
			return processNodes(nodeIterator, rootNodes, token);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	protected GeneratorEnvironment getGeneratorEnvironment(GeneratorToken generatorToken) {
		ContentData<?> content = contentMetaDataProvider.getContent(generatorToken);
		return new DefaultGeneratorEnvironment(webSettings, generatorToken, content);
	}
	
	private String processNodes(NodeIterator nodeIterator, List<Node> rootNodes, GeneratorToken token) throws ParserException {
		
//		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
//			elementPostProcessor.setGeneratorToken(token);
//		}
		
		GeneratorEnvironment generatorEnvironment = getGeneratorEnvironment(token);
		
		while (nodeIterator.hasMoreNodes()) {
			Node node = nodeIterator.nextNode();

			if (node != null) {
				if (log.isDebugEnabled()) {
					log.debug("Processing node " + node.toString());
				}
			}

			rootNodes.add(node);

			for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
				if (postProcessorActivator.isActive(elementPostProcessor) && elementPostProcessor.supports(node, generatorEnvironment)) {
					elementPostProcessor.process(node, generatorEnvironment);
				}
			}

			processNodes(node.getChildren(), generatorEnvironment);
		}
		
		if (rootNodes != null && rootNodes.size() > 0) {
			return getHtml(rootNodes);
		}
		
		return null;
	}

	private void processNodes(NodeList nodeList, GeneratorEnvironment generatorEnvironment) throws ParserException {
		if (nodeList == null) {
			return;
		}

		int size = nodeList.size();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.elementAt(i);

			for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
				if (postProcessorActivator.isActive(elementPostProcessor) && elementPostProcessor.supports(node, generatorEnvironment)) {
					elementPostProcessor.process(node, generatorEnvironment);
				}
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