package sk.seges.acris.generator.server.spring.configuration.appender;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ScriptsAlterPathPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.AcrisExternalScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.JavascriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.AcrisExternalScriptAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.*;

@Import({WebSettingsServiceConfiguration.class, OfflineModeConfiguration.class,
        WebSettingsConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class ScriptPathPostprocessorAlterTestConfiguration {

    @Bean
    public AbstractElementPostProcessor externalScriptAppenderPostProcessor() {
        return new AcrisExternalScriptAppenderPostProcessor();
    }
    @Bean
    public AbstractElementPostProcessor externalScriptAnnihilatorPostProcessor() {
        return new AcrisExternalScriptAnnihilatorPostProcessor();
    }

    @Bean
    public AbstractElementPostProcessor  javascriptAnnihilatorPostProcessor() {
        return new JavascriptAnnihilatorPostProcessor();
    }

    @Bean
    public AbstractElementPostProcessor niceURLLinkPostProcessor() {
        return new NiceURLLinkAlterPostProcessor();
    }

    @Bean
    public AbstractElementPostProcessor scriptsPathPostProcessor() {
        return new ScriptsAlterPathPostProcessor();
    }
}