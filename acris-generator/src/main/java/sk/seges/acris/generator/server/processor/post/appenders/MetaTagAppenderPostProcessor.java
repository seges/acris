package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class MetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

	public MetaTagAppenderPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	@Override
	public boolean process(Node node) {
		if (webSettings.getMetaData() == null) {
			return true;
		}

		for (MetaData metaData : webSettings.getMetaData()) {
			MetaTag metaTag = NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(metaData.getType().getName()));
			if (metaTag == null) {
				appendMetaTag((HeadTag) node, metaData.getType().getName(), metaData.getContent());
			}
		}

		return true;
	}
}