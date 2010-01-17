package sk.seges.acris.generator.server.rewriterules;

public interface INiceUrlGenerator {
	boolean generate(String lang, String webId);
	void clearRewriteFile(String lang);
}
