package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.AbstractContentInfoPostProcessor;

public class HtmlPostProcessing {

	private static final Logger log = Logger.getLogger(HtmlPostProcessing.class);
	
	private Collection<AbstractElementPostProcessor> postProcessors;

	private NodeIterator nodeIterator;
	private List<Node> rootNodes;

	public HtmlPostProcessing(Collection<AbstractElementPostProcessor> postProcessors) {
		this.postProcessors = postProcessors;
	}

	public boolean setProcessorContent(final String content, GeneratorToken token, IContentInfoProvider contentInfoProvider) {
		if (postProcessors == null || postProcessors.size() == 0) {
			throw new RuntimeException("No HTML postprocessor registered.");
		}

		rootNodes = new ArrayList<Node>();

		Lexer lexer = new Lexer(content);
		Parser parser = new Parser(lexer);
		
		PrototypicalNodeFactory prototypicalNodeFactory = (PrototypicalNodeFactory)parser.getNodeFactory();
		prototypicalNodeFactory.registerTag(new StyleLinkTag());
		
		try {
			nodeIterator = parser.elements();
			return processNodes(token, contentInfoProvider);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean processNodes(GeneratorToken token, IContentInfoProvider contentInfoProvider) throws ParserException {
		
		GeneratorToken defaultGeneratorToken = contentInfoProvider.getDefaultContent(token.getWebId(), token.getLanguage());
		
		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			elementPostProcessor.setPostProcessorPageId(token, defaultGeneratorToken);
			if (elementPostProcessor instanceof AbstractContentInfoPostProcessor) {
				((AbstractContentInfoPostProcessor)elementPostProcessor).setContentInfoProvider(contentInfoProvider);
			}
		}
		
		while (nodeIterator.hasMoreNodes()) {
			Node node = nodeIterator.nextNode();

			if (node != null) {
				if (log.isDebugEnabled()) {
					log.debug("Processing node " + node.toString());
				}
			}

			rootNodes.add(node);

			for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
				if (elementPostProcessor.supports(node)) {
					elementPostProcessor.process(node);
				}
			}

			processNodes(node.getChildren());
		}
		
		return (rootNodes != null && rootNodes.size() > 0);
	}

	private void processNodes(NodeList nodeList) throws ParserException {
		if (nodeList == null) {
			return;
		}

		int size = nodeList.size();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.elementAt(i);

			for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
				if (elementPostProcessor.supports(node)) {
					elementPostProcessor.process(node);
				}
			}

			processNodes(node.getChildren());
		}
	}

	public String getHtml() {

		String result = "";
		
		for (Node node : rootNodes) {
			result += node.toHtml();
		}

		return result;
	}
}