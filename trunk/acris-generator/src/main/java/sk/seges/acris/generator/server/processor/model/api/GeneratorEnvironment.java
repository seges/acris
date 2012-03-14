package sk.seges.acris.generator.server.processor.model.api;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public interface GeneratorEnvironment {

	GeneratorToken getGeneratorToken();
	
	ContentData getContent();
	
	WebSettingsData getWebSettings();
		
	boolean isIndexFile();
}
