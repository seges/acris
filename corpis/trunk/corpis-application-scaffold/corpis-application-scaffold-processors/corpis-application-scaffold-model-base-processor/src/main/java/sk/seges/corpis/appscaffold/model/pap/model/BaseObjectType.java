package sk.seges.corpis.appscaffold.model.pap.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.model.pap.accessor.ReadOnlyAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.core.pap.utils.MethodHelper;

public class BaseObjectType extends AbstractDataType {

	private static final String SUFFIX = "Base";

	public BaseObjectType(MutableDeclaredType dataDefinition, MutableProcessingEnvironment processingEnv) {
		super(dataDefinition, processingEnv);

		TypeElement dataElement = dataDefinition.asElement();
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(dataElement.getEnclosedElements());
		
		boolean isAbstract = false;
		
		for (ExecutableElement method: methods) {
			boolean readOnlyProperty = new ReadOnlyAccessor(method, processingEnv).isReadonly();

			if (readOnlyProperty) {
				isAbstract = true;
				break;
			}
		}
				
		List<MutableTypeMirror> interfaces = new ArrayList<MutableTypeMirror>();
		
		DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType(dataDefinition, processingEnv);
		
		interfaces.add(domainDataInterfaceType);
		changePackage(dataDefinition.getPackageName() + "." + LocationType.SERVER.getName() + "." + LayerType.MODEL.getName() + "." + ImplementationType.BASE.getName());
		
		setInterfaces(interfaces);

		List<MutableDeclaredType> baseObjects = domainDataInterfaceType.getBaseObjects();
		
		if (baseObjects.size() == 1) {
			//TODO identify cycle references
			setSuperClass(new BaseObjectType(baseObjects.get(0), processingEnv));
		}
		
		setKind(MutableTypeKind.CLASS);
		
		setDataTypeVariables();
		
		setModifiers((TypeElement) dataDefinition.asElement());
		if (isAbstract) {
			addModifier(Modifier.ABSTRACT);
		}
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return domainDataType.clone().addClassSufix(SUFFIX);
	}

	private void setModifiers(TypeElement dataElement) {
		if (isAbstract(dataElement)) {
			addModifier(Modifier.ABSTRACT);
		}
	}
	
	private boolean isAbstract(TypeElement dataElement) {
		DomainDataInterfaceType domainDataInterfaceType = new DomainDataInterfaceType((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(dataElement.asType()), processingEnv);
		
		for (MutableTypeMirror interfaceType: domainDataInterfaceType.getInterfaces()) {
			if (!(interfaceType instanceof AbstractDataType)) {
				if (hasAbstractMethod( (TypeElement)((DeclaredType) processingEnv.getTypeUtils().fromMutableType(interfaceType)).asElement())) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean hasSameParameters(ExecutableElement method1, ExecutableElement method2) {
		if (method1.getTypeParameters().size() == method2.getTypeParameters().size()) {

			for (int i = 0; i < method1.getTypeParameters().size(); i++) {
				TypeParameterElement method1Parameter = method1.getTypeParameters().get(i);
				TypeParameterElement method2Parameter = method2.getTypeParameters().get(i);
				
				if (!method1Parameter.asType().equals(method2Parameter.asType())) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	private boolean isObjectMethod(ExecutableElement method) {
		TypeElement objectElement = processingEnv.getElementUtils().getTypeElement(Object.class.getCanonicalName());
		
		List<ExecutableElement> objectMethods = ElementFilter.methodsIn(objectElement.getEnclosedElements());
		
		for (ExecutableElement objectMethod: objectMethods) {
			if (objectMethod.getSimpleName().toString().equals(method.getSimpleName().toString()) && 
					objectMethod.getReturnType().equals(method.getReturnType()) && hasSameParameters(objectMethod, method)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean hasAbstractMethod(TypeElement processingElement) {
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());

		ElementSorter.sort(methods);

		for (ExecutableElement method: methods) {

			if (!MethodHelper.isGetterMethod(method) && !MethodHelper.isSetterMethod(method) && !isObjectMethod(method)) {
				return true;
			}
		}

		for (TypeMirror interfaceType: processingElement.getInterfaces()) {
			TypeElement interfaceTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
			
			if (hasAbstractMethod(interfaceTypeElement)) {
				return true;
			}
		}
		
		return false;
	}
}