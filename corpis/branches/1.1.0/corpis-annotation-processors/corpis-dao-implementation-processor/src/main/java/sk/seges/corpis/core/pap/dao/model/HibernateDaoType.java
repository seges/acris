package sk.seges.corpis.core.pap.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public class HibernateDaoType extends DelegateMutableDeclaredType {

	private final MutableDeclaredType mutableDomainType;
	private final MutableProcessingEnvironment processingEnv;

	private boolean interfaceClassInitialized = false;
	private TypeElement interfaceClass;

	private static final String DAO_API_CLASS_SUFFIX = "Dao";
	private static final String DAO_API_CLASS_PREFIX = "Hibernate";

	public HibernateDaoType(MutableDeclaredType mutableDomainType, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.mutableDomainType = mutableDomainType;
		
		setKind(MutableTypeKind.CLASS);
		
		DaoApiType interfaceType = getDaoInterface();

		if (interfaceType != null && getDataInterface() != null) {
			
			Set<MutableDeclaredType> interfaces = new HashSet<MutableDeclaredType>();
		
			MutableDeclaredType mutableDataInterfaceType = processingEnv.getTypeUtils().toMutableType((DeclaredType)getDataInterface().asType());

			if (interfaceType.getTypeVariables().size() == 0) {
				interfaces.add(interfaceType);
			} else {
				MutableTypeVariable[] typeParameters = new MutableTypeVariable[interfaceType.getTypeVariables().size()];
				int i = 0;
								
				for (MutableTypeVariable typeParameterElement: interfaceType.getTypeVariables()) {
					if (processingEnv.getTypeUtils().implementsType(typeParameterElement, mutableDataInterfaceType)) {
						typeParameters[i++] = processingEnv.getTypeUtils().getTypeVariable(null, mutableDataInterfaceType);
					} else {
						typeParameters[i++] = typeParameterElement;
					}
				}
	
				interfaces.add(interfaceType.clone().setTypeVariables(typeParameters));
			}
			
			setInterfaces(interfaces);
			setSuperClass(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(AbstractHibernateCRUD.class), new MutableDeclaredType[] {mutableDataInterfaceType}));
		} else {
			setSuperClass(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(AbstractHibernateCRUD.class), new MutableDeclaredType[] {mutableDomainType}));
		};
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		MutableDeclaredType result = mutableDomainType.clone();

		PackageValidator packageValidator = getPackageValidatorProvider().get(result);
		packageValidator.moveTo(LocationType.SERVER).moveTo(LayerType.DAO);
		
		if (packageValidator.isValid()) {
			packageValidator.moveTo(ImplementationType.HIBERNATE);
		} else {
			packageValidator.setType(LayerType.DAO.getName() + "." + ImplementationType.HIBERNATE.getName());
		}

		return result.changePackage(packageValidator.toString())
										  .addClassPrefix(DAO_API_CLASS_PREFIX)
										  .addClassSufix(DAO_API_CLASS_SUFFIX)
										  .addTypeVariable(processingEnv.getTypeUtils().getTypeVariable("T", result));
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}
	
	private boolean isDomainInterface(TypeElement typeElement) {
		
		DataAccessObject dataAccessObjectAnnotation = typeElement.getAnnotation(DataAccessObject.class);
		if (dataAccessObjectAnnotation == null) {
			return false;
		} 
		
		return dataAccessObjectAnnotation.provider().equals(Provider.INTERFACE);
	}

	private TypeElement getDataInterface(TypeElement typeElement) {

		if (isDomainInterface(typeElement)) {
			return typeElement;
		}
		
		if (typeElement.getInterfaces() != null) {
			for (TypeMirror typeMirror: typeElement.getInterfaces()) {
				Element interfaceElement = processingEnv.getTypeUtils().asElement(typeMirror);
				if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
					TypeElement interfaceTypeElement = (TypeElement)interfaceElement;
					TypeElement interfaceClassElement = getDataInterface(interfaceTypeElement);
					
					if (interfaceClassElement != null) {
						return interfaceClassElement;
					}
				}
			}
		}
		
		TypeMirror superClass = typeElement.getSuperclass();
		if (superClass == null) {
			return null;
		}
		Element superClassElement = processingEnv.getTypeUtils().asElement(superClass);
		if (superClassElement == null) {
			return null;
		}
		if (superClassElement.getKind().equals(ElementKind.CLASS)) {
			typeElement = (TypeElement)superClassElement;
			
			TypeElement interfaceClassElement = getDataInterface(typeElement);
			
			if (interfaceClassElement != null) {
				return interfaceClassElement;
			}
		}
		
		return null;
	}
	
	private TypeElement getDataInterface() {
		if (!interfaceClassInitialized) {
			interfaceClass = getDataInterface((TypeElement)processingEnv.getTypeUtils().fromMutableType(mutableDomainType));
		}
		return interfaceClass;
	}
	
	public DaoApiType getDaoInterface() {
		
		if (getDataInterface() == null) {
			return null;
		}
		return new DaoApiType(processingEnv.getTypeUtils().toMutableType((DeclaredType) getDataInterface().asType()), processingEnv);
	}
}