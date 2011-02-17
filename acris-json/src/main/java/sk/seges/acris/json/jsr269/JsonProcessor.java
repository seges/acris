package sk.seges.acris.json.jsr269;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.acris.json.client.data.IJsonObject;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({ JsonProcessor.CONFIG_FILE_LOCATION })
public class JsonProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/json.properties";
	public static final String OUTPUT_SUFFIX = "Jsonizer";

	@Override
	protected Type[] getImports() {
		return new Type[] {
			NamedType.THIS	
		};
	}
	
	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { JsonObject.class };
		case OUTPUT_INTERFACES:
			return new Type[] { TypedClassBuilder.get(IJsonObject.class, NamedType.THIS) };
		}
		return super.getConfigurationTypes(type, typeElement);
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	public static NamedType getOutputClass(MutableType inputClass) {
		if (inputClass instanceof HasTypeParameters) {
			inputClass = ((HasTypeParameters)inputClass).stripTypeParameters();
		}
		return inputClass.addClassSufix(OUTPUT_SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] { getOutputClass(mutableType) };
	}
}