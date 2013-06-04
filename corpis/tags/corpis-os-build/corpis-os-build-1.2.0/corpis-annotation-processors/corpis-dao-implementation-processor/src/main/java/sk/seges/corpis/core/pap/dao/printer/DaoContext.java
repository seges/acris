package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.corpis.core.pap.dao.model.HibernateDaoType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class DaoContext {

	private final MutableDeclaredType dataType;
	private final HibernateDaoType daoType;
	private final MutableProcessingEnvironment processingEnv;

	public DaoContext(MutableDeclaredType domainType, HibernateDaoType daoType, MutableProcessingEnvironment processingEnv) {
		this.dataType = domainType;
		this.daoType = daoType;
		this.processingEnv = processingEnv;
	}
	
	public MutableDeclaredType getDomainType() {
		return dataType;
	}

	public HibernateDaoType getDaoType() {
		return daoType;
	}
	
	public MutableProcessingEnvironment getProcessingEnv() {
		return processingEnv;
	}
}