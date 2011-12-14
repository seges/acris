package sk.seges.corpis.pap.dao.configuration;

import sk.seges.corpis.MockEntity;
import sk.seges.corpis.appscaffold.shared.annotation.Definition;
import sk.seges.corpis.core.pap.dao.HibernateDataDaoProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.corpis.shared.model.mock.jpa.JpaMockEntity;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = HibernateDataDaoProcessor.class)
public class MockHibernateDataDaoConfiguration {

	@Type
	@Definition(MockEntity.class)
	@DelegateDataAccessObject(@DataAccessObject(provider = Provider.HIBERNATE))
	JpaMockEntity entity;
}
