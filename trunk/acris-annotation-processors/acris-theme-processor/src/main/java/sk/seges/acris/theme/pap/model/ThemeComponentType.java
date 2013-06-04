package sk.seges.acris.theme.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

import com.google.gwt.user.client.ui.Widget;

public class ThemeComponentType extends DelegateMutableDeclaredType {

	public static final String SUFFIX = "Component";
	
	private final ThemeConfigurationType configurationType;
	
	ThemeComponentType(ThemeConfigurationType configurationType, MutableProcessingEnvironment processingEnv) {
		this.configurationType = configurationType;
		
		setKind(MutableTypeKind.CLASS);
		setSuperClass(processingEnv.getTypeUtils().toMutableType(Widget.class));
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return configurationType.clone().addClassSufix(SUFFIX);
	}
}