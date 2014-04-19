package sk.seges.acris.recorder.server.model.dto.configuration;

import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.sesam.pap.model.annotation.*;

@TransferObjectMapping(domainClass = JpaRecordingSession.class)
@Mapping(Mapping.MappingType.AUTOMATIC)
@GenerateHashcode(generate = true)
@GenerateEquals(generate = true)
public class RecordingSessionDTOConfiguration {

    @TransferObjectMapping(configuration = RecordingSessionDTOConfiguration.class, domainClass = RecordingSessionData.class)
    public interface RecordingSessionDataConfiguration {}
}