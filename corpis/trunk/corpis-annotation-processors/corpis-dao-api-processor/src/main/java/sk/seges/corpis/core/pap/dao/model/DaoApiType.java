package sk.seges.corpis.core.pap.dao.model;

import java.util.HashSet;
import java.util.Set;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.dao.ICrudDAO;

public class DaoApiType extends DelegateMutableDeclaredType {

	static final String DAO_API_CLASS_SUFFIX = "Dao";
	static final String DAO_API_CLASS_PREFIX = "I";

	private final MutableDeclaredType mutableDataType;
	private final MutableProcessingEnvironment processingEnv;
	
	public DaoApiType(MutableDeclaredType mutableDataType, MutableProcessingEnvironment processingEnv) {
		this.mutableDataType = mutableDataType;
		this.processingEnv = processingEnv;
		setKind(MutableTypeKind.INTERFACE);
		
		Set<MutableDeclaredType> interfaces = new HashSet<MutableDeclaredType>();
		interfaces.add(processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(ICrudDAO.class), new MutableDeclaredType[] {mutableDataType}));
		setInterfaces(interfaces);
	}

	public static MutableDeclaredType toOutput(MutableDeclaredType inputType, MutableProcessingEnvironment processingEnv) {
		MutableDeclaredType result = inputType.clone();
		
		PackageValidator packageValidator = getPackageValidatorProvider().get(result);
		packageValidator.moveTo(LocationType.SERVER).moveTo(LayerType.DAO);
		
		if (packageValidator.isValid()) {
			packageValidator.moveTo(ImplementationType.API);
		} else {
			packageValidator.setType(LayerType.DAO.getName() + "." + ImplementationType.API.getName());
		}

		return result.changePackage(packageValidator.toString())
										  .addClassPrefix(DAO_API_CLASS_PREFIX)
										  .addClassSufix(DAO_API_CLASS_SUFFIX)
										  .addTypeVariable(processingEnv.getTypeUtils().getTypeVariable("T", inputType));
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		return toOutput(mutableDataType, processingEnv);
	}

	protected static PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

}