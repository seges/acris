package sk.seges.acris.generator.server.rewriterules;

import sk.seges.acris.generator.server.processor.ContentInfoProvider;

public interface INiceUrlGenerator {

	boolean generate(String webId, String niceUrl, String lang);

	void clearRewriteFile(String lang);

	void setContentInfoProvider(ContentInfoProvider contentInfoProvider);
}
