package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.tags.ScriptTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class ScriptsPathPostProcessor extends AbstractPathPostProcessor {

	private static final String SRC = "SRC";
	private static final String JS_TYPE = "text/javascript";

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {

		if (node instanceof ScriptTag) {
			ScriptTag scriptTag = (ScriptTag) node;
			return (isPathRelative(getPath(node)) && compareIgnoreCaseNullSafe(scriptTag.getType(), JS_TYPE));
		}

		return false;
	}

	@Override
	protected String getPath(Node node) {
		ScriptTag scriptTag = (ScriptTag) node;
		String path = scriptTag.getAttribute(SRC);
		if (path != null && path.length() > 0) {
			return path;
		}
		return scriptTag.getAttribute(SRC.toLowerCase());
	}

	@Override
	protected void setPath(Node node, String path) {
		ScriptTag scriptTag = (ScriptTag) node;
		scriptTag.setAttribute(SRC, path);
	}
}