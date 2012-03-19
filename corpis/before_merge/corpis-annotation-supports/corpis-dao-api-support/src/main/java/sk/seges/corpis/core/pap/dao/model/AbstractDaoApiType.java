package sk.seges.corpis.core.pap.dao.model;

import java.util.List;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public abstract class AbstractDaoApiType extends DelegateMutableDeclaredType {

	private static final String DAO_API_CLASS_SUFFIX = "DaoBase";
	protected final MutableDeclaredType mutableDeclaredType;
	protected final MutableProcessingEnvironment processingEnv;

	protected abstract MutableDeclaredType getDataType(MutableDeclaredType inputType);
	protected abstract List<MutableDeclaredType> getTypeInterfaces();
	
	public AbstractDaoApiType(MutableDeclaredType mutableDeclaredType, MutableProcessingEnvironment processingEnv) {
		this.mutableDeclaredType = mutableDeclaredType;
		this.processingEnv = processingEnv;
		setKind(MutableTypeKind.INTERFACE);
		setInterfaces(getTypeInterfaces());
	}

	public AbstractDaoApiType(TypeElement typeElement, MutableProcessingEnvironment processingEnv) {
		this.mutableDeclaredType = null;
		this.processingEnv = processingEnv;
		setDelegate(processingEnv.getTypeUtils().toMutableType(typeElement));
	}
	
	private MutableDeclaredType toOutput(MutableDeclaredType inputType,	MutableProcessingEnvironment processingEnv) {
		MutableDeclaredType result = inputType.clone();

		PackageValidator packageValidator = getPackageValidatorProvider().get(result);

		if (packageValidator.isValid()) {
			packageValidator.moveTo(LocationType.SERVER).moveTo(LayerType.DAO).moveTo(ImplementationType.API);
		} else {
			packageValidator.setType(LocationType.SERVER.getName() + "." + LayerType.DAO.getName() + "." + ImplementationType.API.getName());
		}
		
		return result.changePackage(packageValidator.toString())
							.addClassSufix(DAO_API_CLASS_SUFFIX)
							.setTypeVariables(processingEnv.getTypeUtils().getTypeVariable("T", getDataType(inputType)));
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return toOutput(mutableDeclaredType, processingEnv);
	}

	protected static PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}
}
