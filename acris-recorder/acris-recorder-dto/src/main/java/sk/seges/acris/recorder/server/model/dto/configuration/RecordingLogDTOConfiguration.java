package sk.seges.acris.recorder.server.model.dto.configuration;

import sk.seges.acris.recorder.RecordingLog;
import sk.seges.acris.recorder.RecordingSession;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingLog;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.security.server.user_management.model.dto.configuration.GenericUserDTOConfiguration;
import sk.seges.sesam.pap.model.annotation.*;

import java.util.Date;

@TransferObjectMapping(domainClass = JpaRecordingLog.class)
@Mapping(Mapping.MappingType.EXPLICIT)
@GenerateHashcode(generate = true)
@GenerateEquals(generate = true)
public interface RecordingLogDTOConfiguration {

    void id();

    @Field(RecordingLogData.SESSION + "." + RecordingSession.ID)
    void sessionId();

    void eventTime();
    void event();

}