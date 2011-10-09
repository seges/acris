package sk.seges.acris.json.jsr269;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.acris.json.jsr269.configurer.JsonProcessorConfiguration;
import sk.seges.acris.json.jsr269.model.JsonizerType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JsonProcessor extends MutableAnnotationProcessor {


	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new JsonProcessorConfiguration();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new JsonizerType(context.getMutableType(), processingEnv)
		};
	}

	@Override
	protected void processElement(ProcessorContext context) {}
}