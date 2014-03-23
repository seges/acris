package sk.seges.acris.recorder.server.model.dto.configuration;

import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingLog;
import sk.seges.acris.security.server.user_management.model.dto.configuration.GenericUserDTOConfiguration;
import sk.seges.sesam.pap.model.annotation.*;

@TransferObjectMapping(domainClass = JpaRecordingLog.class)
@Mapping(Mapping.MappingType.AUTOMATIC)
@GenerateHashcode(generate = true)
@GenerateEquals(generate = true)
public class RecordingLogDTOConfiguration {}