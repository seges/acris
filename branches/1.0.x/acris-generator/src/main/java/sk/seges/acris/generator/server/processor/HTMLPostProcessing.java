package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.htmltags.StyleLinkTag;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.AbstractContentInfoPostProcessor;

@Component
public class HTMLPostProcessing {

	@Autowired
	private ApplicationContext applicationContext;

	private Collection<AbstractElementPostProcessor> postProcessors;

	private NodeIterator nodeIterator;
	private List<Node> rootNodes;

	public HTMLPostProcessing() {
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

	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void registerPostProcessors() {
		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext
				.getBeansOfType(AbstractElementPostProcessor.class);
		postProcessors = abstractPostProcessors.values();
	}

	private boolean processNodes(GeneratorToken token, IContentInfoProvider contentInfoProvider) throws ParserException {
		for (AbstractElementPostProcessor elementPostProcessor : postProcessors) {
			elementPostProcessor.setPostProcessorPageId(token);
			if (elementPostProcessor instanceof AbstractContentInfoPostProcessor) {
				((AbstractContentInfoPostProcessor)elementPostProcessor).setContentInfoProvider(contentInfoProvider);
			}
		}
		
		while (nodeIterator.hasMoreNodes()) {
			Node node = nodeIterator.nextNode();

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