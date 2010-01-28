package sk.seges.acris.generator.server.rewriterules;


public interface INiceUrlGenerator {
	boolean generate(String webId, String niceUrl, String lang);
	void clearRewriteFile(String lang);
}
