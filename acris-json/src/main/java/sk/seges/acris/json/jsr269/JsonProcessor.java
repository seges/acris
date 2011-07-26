package sk.seges.acris.json.jsr269;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.json.client.data.IJsonObject;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JsonProcessor extends AbstractConfigurableProcessor {

	public static final String OUTPUT_SUFFIX = "Jsonizer";

	@Override
	protected Type[] getImports() {
		return new Type[] {
			NamedType.THIS	
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new JsonProcessorConfiguration();
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_INTERFACES:
				return new Type[] { TypedClassBuilder.get(IJsonObject.class, NamedType.THIS) };
			}
		return super.getOutputDefinition(type, typeElement);
	}

	public static NamedType getOutputClass(ImmutableType inputClass) {
		if (inputClass instanceof HasTypeParameters) {
			inputClass = ((HasTypeParameters)inputClass).stripTypeParameters();
		}
		return inputClass.addClassSufix(OUTPUT_SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { getOutputClass(mutableType) };
	}
}