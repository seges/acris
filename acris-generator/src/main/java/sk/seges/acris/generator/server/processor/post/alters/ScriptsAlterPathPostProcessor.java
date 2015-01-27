package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.ScriptTag;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.generator.server.processor.utils.ScriptUtils;

public class ScriptsAlterPathPostProcessor extends AbstractPathAlterPostProcessor {

	private static final String JS_TEXT_TYPE = "text/javascript";
	private static final String JS_LANGUAGE = "javascript";

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {

		if (node instanceof ScriptTag) {
			ScriptTag scriptTag = (ScriptTag) node;
			return (isPathRelative(getPath(node)) && (
					compareIgnoreCaseNullSafe(scriptTag.getLanguage(), JS_LANGUAGE) ||
					compareIgnoreCaseNullSafe(scriptTag.getType(), JS_TEXT_TYPE)));
		}

		return false;
	}

	@Override
	protected String getPath(Node node) {
		return ScriptUtils.getPath((ScriptTag) node);
	}

	@Override
	protected void setPath(Node node, String path) {
		ScriptUtils.setPath((ScriptTag) node, path);
	}
}