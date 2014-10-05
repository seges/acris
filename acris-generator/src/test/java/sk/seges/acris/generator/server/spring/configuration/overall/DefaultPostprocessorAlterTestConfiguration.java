package sk.seges.acris.generator.server.spring.configuration.overall;

import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, 
	OfflineSettingsConfiguration.class})
public class DefaultPostprocessorAlterTestConfiguration {
}