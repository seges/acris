package sk.seges.sesam.pap.converter.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.processor.ConfigurableAnnotationProcessor;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ParametersFilter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.AbstractConverterProvider;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ConverterProviderType extends DelegateMutableDeclaredType {

	protected final MutableDeclaredType mutableType;
	protected final MutableProcessingEnvironment processingEnv;
	
	public static final String CONVERTER_PROVIDER_SUFFIX = "ConverterProvider";

	public ConverterProviderType(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv) {
		this.mutableType = mutableType;
		this.processingEnv = processingEnv;

		setKind(MutableTypeKind.CLASS);
		setSuperClass(processingEnv.getTypeUtils().toMutableType(AbstractConverterProvider.class));
	}
	
	protected void setModifiers() {
		addModifier(Modifier.PUBLIC);
	}
	
	public ParameterElement[] getConverterParameters(ConverterConstructorParametersResolver parametersResolver) {
		
		MutableDeclaredType converterProviderType = processingEnv.getTypeUtils().toMutableType(ConverterProvider.class);

		ParameterElement[] generatedParameters = ParametersFilter.NOT_PROPAGATED.filterParameters(parametersResolver.getConstructorAditionalParameters());

		List<ParameterElement> filteredParameters = new ArrayList<ParameterElement>();
		for (ParameterElement generatedParameter: generatedParameters) {
			if (!generatedParameter.getType().equals(converterProviderType)) {
				filteredParameters.add(generatedParameter);
			}
		}
		
		return filteredParameters.toArray(new ParameterElement[] {});
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		
		DefaultPackageValidator packageValidator = 
			new DefaultPackageValidatorProvider().get(mutableType.getPackageName());
		
		String projectName = processingEnv.getOptions().get(ConfigurableAnnotationProcessor.PROJECT_NAME_OPTION);
		
		int colonIndex = projectName.indexOf(":");
		
		if (colonIndex != -1) {
			projectName = projectName.substring(0, colonIndex);
		}
		
		String[] projectNameParts = projectName.split("-");
		
		projectName = "";
		
		for (String projectNamePart: projectNameParts) {
			projectName += MethodHelper.toMethod(projectNamePart);
		}
		
		MutableDeclaredType result = mutableType.clone();
		result.setSimpleName(projectName + CONVERTER_PROVIDER_SUFFIX);
		
		if (packageValidator.isValid()) {
			result.changePackage(packageValidator.getGroup() + "." + packageValidator.getArtifact() + "." + packageValidator.getLocationType().getName());
		}
		
		return result;
	}
}
