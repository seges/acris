package sk.seges.acris.recorder.server.model.dto.converter;

import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

import java.util.List;

/**
 * Created by PeterSimun on 14.4.2014.
 */
@TransferObjectMapping(domainClass = RecordingSessionWithBlobs.class)
@Mapping(Mapping.MappingType.EXPLICIT)
@GenerateHashcode(generate = false)
@GenerateEquals(generate = false)
public interface RecordingSessionWithBlobsDTOConfiguration {

    void recordingSession();

    @TransferObjectMapping(converter = RecordingBlobsListConverter.class)
    List<String> recordingBlobs();

}
