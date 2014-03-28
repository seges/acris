package sk.seges.acris.recorder.server.dao.hibernate;

import sk.seges.acris.recorder.RecordingSession;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.corpis.appscaffold.shared.annotation.Definition;
import sk.seges.corpis.core.pap.dao.HibernateDataDaoProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = HibernateDataDaoProcessor.class)
public class HibernateDaoConfiguration {

	@Type
	@Definition(RecordingSession.class)
	@DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.HIBERNATE))
	JpaRecordingSession recordingSession;

}