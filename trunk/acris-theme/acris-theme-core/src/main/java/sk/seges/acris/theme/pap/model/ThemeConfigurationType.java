package sk.seges.acris.theme.pap.model;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;

public class ThemeConfigurationType extends DelegateMutableDeclaredType {

	private final TypeElement configurationTypeElement;
	private final MutableDeclaredType configurationType;
	private final MutableProcessingEnvironment processingEnv;
	
	public ThemeConfigurationType(TypeElement configurationTypeElement, MutableProcessingEnvironment processingEnv) {
		this.configurationTypeElement = configurationTypeElement;
		this.configurationType = processingEnv.getTypeUtils().toMutableType((DeclaredType)configurationTypeElement.asType());
		this.processingEnv = processingEnv;
	}
	
	public ThemeComponentType getThemeComponent() {
		return new ThemeComponentType(this, processingEnv);
	}
	
	public TypeElement asElement() {
		return configurationTypeElement;
	}
	
	public ThemePanelType getThemePanel() {
		return new ThemePanelType(this, processingEnv);
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return configurationType;
	}

	public TypeElement getWidgetType() {
		ThemeSupport themeSupportAnnotation = configurationTypeElement.getAnnotation(ThemeSupport.class);
		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(themeSupportAnnotation, new AnnotationClassProperty<ThemeSupport>() {

			@Override
			public Class<?> getClassProperty(ThemeSupport annotation) {
				return annotation.widgetClass();
			}
		});
	}
	
}
