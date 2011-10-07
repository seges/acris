package sk.seges.corpis.appscaffold.mapbasedobject.pap.model;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.appscaffold.shared.domain.MapBasedObject;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class MapBasedType extends DelegateMutableDeclaredType {

	private final TypeElement mapBasedElement;
	private final MutableProcessingEnvironment processingEnv;
	
	public MapBasedType(TypeElement mapBasedElement, MutableProcessingEnvironment processingEnv) {
		this.mapBasedElement = mapBasedElement;
		this.processingEnv = processingEnv;

		setSuperClass(processingEnv.getTypeUtils().toMutableType(MapBasedObject.class));

		Set<MutableDeclaredType> interfaces = new HashSet<MutableDeclaredType>();
		for (TypeMirror interfaceElementMirror : mapBasedElement.getInterfaces()) {
			MutableTypeMirror mutableInterface = processingEnv.getTypeUtils().toMutableType(interfaceElementMirror);
			
			if (mutableInterface.getKind().isDeclared()) {
				interfaces.add((MutableDeclaredType)mutableInterface);
			}
		}

		setInterfaces(interfaces);

		setKind(MutableTypeKind.CLASS);
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType mutableType = processingEnv.getTypeUtils().toMutableType((DeclaredType)mapBasedElement.asType());
		mutableType.changePackage(mutableType.getPackageName() + ".shared.domain").addClassSufix("MapBean");
		return mutableType;
	}
}