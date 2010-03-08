package sk.seges.acris.generator.server.rewriterules;

import sk.seges.acris.generator.server.processor.IContentInfoProvider;

public interface INiceUrlGenerator {

	boolean generate(String webId, String niceUrl, String lang);

	void clearRewriteFile(String lang);

	void setContentInfoProvider(IContentInfoProvider contentInfoProvider);
}
