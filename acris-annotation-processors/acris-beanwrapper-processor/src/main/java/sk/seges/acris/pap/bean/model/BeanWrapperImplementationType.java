package sk.seges.acris.pap.bean.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class BeanWrapperImplementationType extends DelegateMutableDeclaredType {

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";
	
	private final MutableDeclaredType bean;
	
	public BeanWrapperImplementationType(MutableDeclaredType bean, MutableProcessingEnvironment processingEnv) {
		this.bean = bean;
		
		setKind(MutableTypeKind.CLASS);
		
		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		interfaces.add(new BeanWrapperType(bean.clone().stripTypeParametersTypes(), processingEnv));
		setInterfaces(interfaces);
		
		List<Modifier> modifiers = new ArrayList<Modifier>();
		modifiers.addAll(bean.getModifiers());
		modifiers.remove(Modifier.STATIC);
		setModifier(modifiers.toArray(new Modifier[] {}));
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return bean.clone().addClassSufix(BEAN_WRAPPER_SUFFIX);
	}
}