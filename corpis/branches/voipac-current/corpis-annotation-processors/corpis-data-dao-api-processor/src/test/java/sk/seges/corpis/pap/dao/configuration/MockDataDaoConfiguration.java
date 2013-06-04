package sk.seges.corpis.pap.dao.configuration;

import sk.seges.corpis.core.pap.dao.DataDaoApiProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.corpis.pap.dao.mock.MockEntity;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = DataDaoApiProcessor.class)
public class MockDataDaoConfiguration {
	
	@Type
	@DelegateDataAccessObject(@DataAccessObject(provider = Provider.INTERFACE))
	MockEntity<?> entity;
}