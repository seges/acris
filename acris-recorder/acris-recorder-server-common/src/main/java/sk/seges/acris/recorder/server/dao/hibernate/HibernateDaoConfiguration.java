package sk.seges.acris.recorder.server.dao.hibernate;

import sk.seges.acris.recorder.RecordingLog;
import sk.seges.acris.recorder.RecordingSession;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingLog;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.corpis.appscaffold.shared.annotation.Definition;
import sk.seges.corpis.core.pap.dao.HibernateDataDaoProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = HibernateDataDaoProcessor.class)
public class HibernateDaoConfiguration {

	@Type
	@Definition(RecordingLog.class)
	@DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.HIBERNATE))
	JpaRecordingLog recordingLog;

	@Type
	@Definition(RecordingSession.class)
	@DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.HIBERNATE))
	JpaRecordingSession recordingSession;

}