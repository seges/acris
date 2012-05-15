package sk.seges.corpis.core.pap.dao.model;

import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.core.pap.dao.accessor.DefinitionAccessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class HibernateDataDaoType extends HibernateDaoType {

	public HibernateDataDaoType(MutableDeclaredType mutableDomainType, MutableProcessingEnvironment processingEnv) {
		super(mutableDomainType, processingEnv);
	}
	
	@Override
	public AbstractDaoApiType getDaoInterface() {
		DefinitionType definition = new DefinitionAccessor(mutableDomainType, processingEnv).getDefinition();
		
		if (definition != null) {
			return definition.getDaoApiType();
		}
		
		return super.getDaoInterface();
	}
	
	@Override
	protected DomainDataInterfaceType getDataInterface() {
		DefinitionType definition = new DefinitionAccessor(mutableDomainType, processingEnv).getDefinition();
		
		if (definition != null) {
			return definition.getDataInterfaceType();
		}
		
		return super.getDataInterface();
	}
}
