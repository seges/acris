package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.corpis.core.pap.dao.model.AbstractHibernateDaoType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class DaoContext {

	private final MutableDeclaredType domainType;
	private final AbstractHibernateDaoType daoType;
	private final MutableProcessingEnvironment processingEnv;

	public DaoContext(MutableDeclaredType domainType, AbstractHibernateDaoType daoType, MutableProcessingEnvironment processingEnv) {
		this.domainType = domainType;
		this.daoType = daoType;
		this.processingEnv = processingEnv;
	}
	
	public MutableDeclaredType getDomainType() {
		return domainType;
	}

	public AbstractHibernateDaoType getDaoType() {
		return daoType;
	}
	
	public MutableProcessingEnvironment getProcessingEnv() {
		return processingEnv;
	}
}