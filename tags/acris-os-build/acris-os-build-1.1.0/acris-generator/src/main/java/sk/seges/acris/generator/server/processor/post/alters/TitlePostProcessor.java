package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.TitleTag;

import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.site.shared.service.IWebSettingsService;

/**
 * Post processor modifies title in the header regarding the content
 * 
 * @author psimun
 * 
 */
public class TitlePostProcessor extends AbstractContentInfoPostProcessor {

	public TitlePostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean process(Node node) {
		NodesUtils.setTitle((TitleTag)node, contentInfoProvider.getContentTitle(generatorToken));
		return true;
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof TitleTag);
	}
}