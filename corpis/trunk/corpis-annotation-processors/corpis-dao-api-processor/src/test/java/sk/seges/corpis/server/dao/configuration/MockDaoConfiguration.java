package sk.seges.corpis.server.dao.configuration;

import sk.seges.corpis.core.pap.dao.DaoApiProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.corpis.shared.model.PlainMockEntityData;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = DaoApiProcessor.class)
public class MockDaoConfiguration {

	@Type
	@DelegateDataAccessObject(@DataAccessObject(provider = Provider.INTERFACE))
	PlainMockEntityData entity;

}