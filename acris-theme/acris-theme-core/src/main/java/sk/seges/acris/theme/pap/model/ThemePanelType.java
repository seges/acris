package sk.seges.acris.theme.pap.model;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ThemePanelType extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Panel";
	
	private final ThemeConfigurationType configurationType;
	
	ThemePanelType(ThemeConfigurationType configurationType, MutableProcessingEnvironment processingEnv) {
		this.configurationType = configurationType;

		setKind(MutableTypeKind.CLASS);
		
		TypeElement componentTypeElement = configurationType.getWidgetType();
		if (processingEnv.getTypeUtils().isSubtype(configurationType.asElement().asType(), componentTypeElement.asType())) {
			setSuperClass(processingEnv.getTypeUtils().toMutableType((DeclaredType) configurationType.asElement().asType()));
		} else {
			setSuperClass(processingEnv.getTypeUtils().toMutableType((DeclaredType) componentTypeElement.asType()));
		}
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return configurationType.clone().addClassSufix(SUFFIX);
	}
}