package sk.seges.acris.generator.server.processor;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

@Component
public class HTMLPostProcessing {

	@Autowired
	private ApplicationContext applicationContext;

	private Collection<AbstractElementPostProcessor> postProcessors;

	private NodeIterator nodeIterator;
	private Node rootNode;

	public HTMLPostProcessing() {
	}

	public boolean setProcessorContent(final String content, String webId, String lang) {
		if (postProcessors == null || postProcessors.size() == 0) {
			throw new RuntimeException("No HTML postprocessor registered.");
		}

		rootNode = null;

		Lexer lexer = new Lexer(content);
		Parser parser = new Parser(lexer);
		try {
			nodeIterator = parser.elements();
			return processNodes(webId, lang);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void registerPostProcessors() {
		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext
				.getBeansOfType(AbstractElementPostProcessor.class);
		postProcessors = abstractPostProcessors.values();
	}

	private boolean processNodes(String webId, String lang) throws ParserException {
		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			elementPostProcessor.setPostProcessorPageId(webId, lang);
		}
		
		
		while (nodeIterator.hasMoreNodes()) {
			Node node = nodeIterator.nextNode();

			if (rootNode == null) {
				rootNode = node;
			}

			for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
				if (elementPostProcessor.supports(node)) {
					elementPostProcessor.process(node);
				}
			}

			processNodes(node.getChildren());
		}
		
		return (rootNode != null);
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

		if (rootNode != null) {

			while (rootNode.getParent() != null) {
				rootNode = rootNode.getParent();
			}

			return rootNode.toHtml();
		}

		return null;
	}
}