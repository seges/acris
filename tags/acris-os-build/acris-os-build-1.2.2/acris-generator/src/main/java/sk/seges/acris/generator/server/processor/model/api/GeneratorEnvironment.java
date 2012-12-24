package sk.seges.acris.generator.server.processor.model.api;

import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;

public interface GeneratorEnvironment {

	GeneratorToken getGeneratorToken();
	GeneratorToken getDefaultToken();
	
	ContentData getContent();
	
	WebSettingsData getWebSettings();
		
	boolean isIndexFile();
}
