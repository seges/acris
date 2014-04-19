package sk.seges.acris.recorder.server.dao;

import sk.seges.acris.recorder.RecordingBlob;
import sk.seges.acris.recorder.RecordingLog;
import sk.seges.acris.recorder.RecordingSession;
import sk.seges.corpis.core.pap.dao.DataDaoApiProcessor;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.annotation.configuration.Type;

@ProcessorConfiguration(processor = DataDaoApiProcessor.class)
public class DaoConfiguration {

	@Type
	@DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.INTERFACE))
	RecordingLog recordingLog;

	@Type
	@DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.INTERFACE))
	RecordingSession recordingSession;

    @Type
    @DelegateDataAccessObject(@DataAccessObject(provider = DataAccessObject.Provider.INTERFACE))
    RecordingBlob recordingBlob;
}
