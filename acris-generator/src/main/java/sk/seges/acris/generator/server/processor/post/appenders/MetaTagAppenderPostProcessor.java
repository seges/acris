package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;
import sk.seges.acris.site.server.model.data.MetaDataData;

public class MetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getWebSettings().getMetaData() == null) {
			return true;
		}

		for (MetaDataData metaData : generatorEnvironment.getWebSettings().getMetaData()) {
			MetaTag metaTag = NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(metaData.getType().getName()));
			if (metaTag == null) {
				appendMetaTag((HeadTag) node, metaData.getType().getName(), metaData.getContent());
			}
		}

		return true;
	}
}
