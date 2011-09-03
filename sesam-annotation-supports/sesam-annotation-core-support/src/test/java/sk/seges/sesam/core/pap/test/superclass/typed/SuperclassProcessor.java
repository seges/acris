package sk.seges.sesam.core.pap.test.superclass.typed;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SuperclassProcessor extends AbstractConfigurableProcessor {

	public static NamedType getOutputClass(ImmutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		return inputClass.addClassSufix("Generated");
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(SuperClassMarker.class.getName());
		return hashSet;
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
				mutableType.addClassSufix("Generated")
		};
	} 
	
	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] {
					typeParametersSupport.applyVariableTypeParameters(nameTypesUtils.toImmutableType(typeElement), (DeclaredType)typeElement.asType())
			};
		}
		return super.getOutputDefinition(type, typeElement);
	}
}