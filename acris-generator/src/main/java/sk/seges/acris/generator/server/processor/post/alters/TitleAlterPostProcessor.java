package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.TitleTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;

/**
 * Post processor modifies title in the header regarding the content
 * 
 * @author Peter Simun (simun@seges.sk)
 */
public class TitleAlterPostProcessor extends AbstractAlterPostProcessor {

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		NodesUtils.setText((TitleTag) node, generatorEnvironment.getContent().getTitle());
		return true;
	}

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof TitleTag && 
				generatorEnvironment.getContent() != null &&
				generatorEnvironment.getContent().getTitle() != null && 
				generatorEnvironment.getContent().getTitle().length() > 0);
	}
}