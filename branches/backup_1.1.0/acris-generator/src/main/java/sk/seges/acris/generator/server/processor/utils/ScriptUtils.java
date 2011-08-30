package sk.seges.acris.generator.server.processor.utils;

import org.htmlparser.tags.ScriptTag;

public class ScriptUtils {

	private static final String SRC = "SRC";

	public static String getPath(ScriptTag scriptTag) {
		String path = scriptTag.getAttribute(SRC);
		if (path != null && path.length() > 0) {
			return path;
		}
		return scriptTag.getAttribute(SRC.toLowerCase());
	}

	public static void setPath(ScriptTag scriptTag, String path) {
		scriptTag.setAttribute(SRC, path);
	}
}
