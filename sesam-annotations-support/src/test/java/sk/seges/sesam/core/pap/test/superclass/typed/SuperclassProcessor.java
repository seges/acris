package sk.seges.sesam.core.pap.test.superclass.typed;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SuperclassProcessor extends AbstractConfigurableProcessor {

	public static NamedType getOutputClass(MutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		return inputClass.addClassSufix("Generated");
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(SuperClassTest.class.getName());
		return hashSet;
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
				mutableType.addClassSufix("Generated")
		};
	} 
	
	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] {
					genericsSupport.applyVariableGenerics(NamedType.THIS, typeElement)
			};
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
}