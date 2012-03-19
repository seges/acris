package sk.seges.corpis.core.pap.dao.accessor;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.core.pap.dao.model.DaoApiType;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.accessor.SingleAnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class DataAccessObjectAccessor extends SingleAnnotationAccessor<DataAccessObject> {

	public DataAccessObjectAccessor(MutableDeclaredType mutableType, MutableProcessingEnvironment processingEnv) {
		super(mutableType, DataAccessObject.class, processingEnv);
	}

	public Provider getProvider() {
		if (!isValid()) {
			return null;
		}
		
		return annotation.provider();
	}
	
	public DaoApiType getDaoBase() {
		
		if (!isValid()) {
			return null;
		}

		Class<?> daoBaseClass = NullCheck.checkNull(annotation.daoBase());
		
		if (daoBaseClass == null) {
//			return new DomainDataInterfaceType(mutableType, processingEnv);
			return null;
		}
		
//		DomainDataInterfaceType dataInterfaceType = getDataType();
//		if (dataInterfaceType == null) {
//			return null;
//		}
		return new DaoApiType(processingEnv.getElementUtils().getTypeElement(daoBaseClass.getCanonicalName()), processingEnv);
	}
	
	
	public DomainDataInterfaceType getDataType() {
		if (!isValid()) {
			return null;
		}
		
		Class<?> dataClass = NullCheck.checkNull(annotation.data());
		
		if (dataClass == null) {
//			return new DomainDataInterfaceType(mutableType, processingEnv);
			return null;
		}
		
		return new DomainDataInterfaceType(processingEnv.getElementUtils().getTypeElement(dataClass.getCanonicalName()), processingEnv);
	}
}