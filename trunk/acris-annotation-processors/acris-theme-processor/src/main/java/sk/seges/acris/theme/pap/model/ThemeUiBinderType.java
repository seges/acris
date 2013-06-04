package sk.seges.acris.theme.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;

public class ThemeUiBinderType extends DelegateMutableDeclaredType {

	private final MutableTypes mutableTypes;
	private final String prefixName;
	
	public ThemeUiBinderType(String name, MutableDeclaredType type, MutableProcessingEnvironment processingEnv) {
		this.mutableTypes = processingEnv.getTypeUtils();
		this.prefixName = name;

		setKind(MutableTypeKind.INTERFACE);
 		setSuperClass(mutableTypes.toMutableType(UiBinder.class).setTypeVariables(mutableTypes.getTypeVariable(null, mutableTypes.toMutableType(Element.class)), mutableTypes.getTypeVariable(null, type)));
 		
 		setModifier();
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return mutableTypes.toMutableType(UiBinder.class).clone().addClassPrefix(prefixName);
	}
	
}
