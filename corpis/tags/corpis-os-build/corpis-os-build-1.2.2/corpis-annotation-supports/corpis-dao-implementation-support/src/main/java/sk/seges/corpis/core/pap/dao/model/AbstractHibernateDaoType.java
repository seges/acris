package sk.seges.corpis.core.pap.dao.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.core.pap.dao.accessor.DataAccessObjectAccessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.api.MutableWildcardType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public abstract class AbstractHibernateDaoType extends DelegateMutableDeclaredType {

	protected final MutableDeclaredType mutableDomainType;
	protected final MutableProcessingEnvironment processingEnv;

	private boolean interfaceClassInitialized = false;
	private MutableDeclaredType interfaceClass;

	private static final String DAO_API_CLASS_SUFFIX = "Dao";
	private static final String DAO_API_CLASS_PREFIX = "Hibernate";
	
	protected abstract AbstractDaoApiType getDaoInterface();

	public AbstractHibernateDaoType(MutableDeclaredType mutableDomainType, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.mutableDomainType = mutableDomainType;

		setKind(MutableTypeKind.CLASS);

		AbstractDaoApiType interfaceType = getDaoInterface();
		MutableDeclaredType mutableDataInterface = getDataInterface();
		if (interfaceType != null && mutableDataInterface != null) {

			List<MutableDeclaredType> interfaces = new ArrayList<MutableDeclaredType>();

			if (interfaceType.getTypeVariables().size() == 0) {
				interfaces.add(interfaceType);
			} else {
				MutableTypeVariable[] typeParameters = new MutableTypeVariable[interfaceType.getTypeVariables().size()];
				int i = 0;

				for (MutableTypeVariable typeParameterElement : interfaceType.getTypeVariables()) {
					if (processingEnv.getTypeUtils().implementsType(typeParameterElement, mutableDataInterface)) {
						typeParameters[i++] = processingEnv	.getTypeUtils()
								.getTypeVariable(null, mutableDataInterface);
					} else {
						typeParameters[i++] = typeParameterElement;
					}
				}

				interfaces.add(interfaceType.clone().setTypeVariables(typeParameters)
						.renameTypeParameter(RenameActionType.REPLACE, null));
			}
			setInterfaces(interfaces);
			
			if (mutableDataInterface.getTypeVariables().size() > 0) {
				List<MutableWildcardType> wildcardType = new ArrayList<MutableWildcardType>();
				for (int i = 0; i < mutableDataInterface.getTypeVariables().size(); i++) {
					wildcardType.add(processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null));
				}
				mutableDataInterface.setTypeVariables(wildcardType.toArray(new MutableTypeVariable[] {}));
			}
				
			setSuperClass(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(AbstractHibernateCRUD.class),
					new MutableDeclaredType[] { mutableDataInterface }));
		} else {
			setSuperClass(processingEnv.getTypeUtils().getDeclaredType(
					(MutableDeclaredType) processingEnv.getTypeUtils()
							.toMutableType(AbstractHibernateCRUD.class),
					new MutableDeclaredType[] { mutableDomainType }));
		}
		;
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
				.addClassSufix(DAO_API_CLASS_SUFFIX);
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private boolean isDomainInterface(TypeElement typeElement) {

		DataAccessObject dataAccessObjectAnnotation = typeElement.getAnnotation(DataAccessObject.class);
		if (dataAccessObjectAnnotation == null) {
			return false;
		}

		return dataAccessObjectAnnotation.provider().equals(Provider.HIBERNATE);
	}

	private TypeElement getDataInterface(TypeElement typeElement) {

		if (isDomainInterface(typeElement)) {
			return typeElement;
		}

		if (typeElement.getInterfaces() != null) {
			for (TypeMirror typeMirror : typeElement.getInterfaces()) {
				Element interfaceElement = processingEnv.getTypeUtils()
						.asElement(typeMirror);
				if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
					TypeElement interfaceTypeElement = (TypeElement) interfaceElement;
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
			typeElement = (TypeElement) superClassElement;

			TypeElement interfaceClassElement = getDataInterface(typeElement);

			if (interfaceClassElement != null) {
				return interfaceClassElement;
			}
		}

		return null;
	}

	protected MutableDeclaredType toMutable(TypeElement typeElement) {
		return (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(typeElement.asType());
	}
	
	protected TypeElement fromMutable(MutableDeclaredType mutableDeclaredType) {
		return (TypeElement) ((DeclaredType) processingEnv.getTypeUtils().fromMutableType(mutableDeclaredType)).asElement();
	}
	
	protected MutableDeclaredType getDataInterface() {
		if (!interfaceClassInitialized) {
			interfaceClass = new DataAccessObjectAccessor(mutableDomainType, processingEnv).getDataType();

			if (interfaceClass == null) {
				TypeElement dataInterface = getDataInterface(fromMutable(mutableDomainType));
				
				if (dataInterface != null) {
					interfaceClass = new DomainDataInterfaceType(dataInterface, processingEnv);
				}
			}
			
			if (interfaceClass == null) {
				interfaceClass = mutableDomainType;
			}
			
			interfaceClassInitialized = true;
		}
		return interfaceClass;
	}
}