package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class DescriptionMetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

	public DescriptionMetaTagAppenderPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag);
	}

	@Override
	public boolean process(Node node) {
		if (NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(DescriptionMetaTagPostProcessor.DESCRIPTION_TAG_NAME)) == null) {
			appendMetaTag((HeadTag) node, DescriptionMetaTagPostProcessor.DESCRIPTION_TAG_NAME, contentInfoProvider.getContentDescription(generatorToken));
		}
		return true;
	}
}