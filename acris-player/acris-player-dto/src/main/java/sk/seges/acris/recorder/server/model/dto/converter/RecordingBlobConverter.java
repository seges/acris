package sk.seges.acris.recorder.server.model.dto.converter;

import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingBlob;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingBlobPk;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingBlobPkData;
import sk.seges.acris.server.domain.converter.BlobConverter;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by PeterSimun on 14.4.2014.
 */
public class RecordingBlobConverter extends BasicCachedConverter<String, RecordingBlobData> {

    private final BlobConverter blobConverter;
    private final EntityManager entityManager;

    public RecordingBlobConverter(EntityManager entityManager) {
        this.blobConverter = new BlobConverter(entityManager);
        this.entityManager = entityManager;
    }

    public RecordingBlobData createDomainInstance(Serializable dtoId) {
        if (dtoId != null && dtoId instanceof RecordingBlobPkData) {
            JpaRecordingBlob _result = entityManager.find(JpaRecordingBlob.class, dtoId);
            if (_result != null) {
                return _result;
            }
        }

        JpaRecordingBlob _result = new JpaRecordingBlob();
        if (dtoId instanceof RecordingBlobPkData) {
            _result.setId((RecordingBlobPkData)dtoId);
        }
        return _result;
    }

    @Override
    public String createDtoInstance(Serializable id) {
        return null;
    }

    @Override
    public String convertToDto(String result, RecordingBlobData recordingBlobData) {
        return blobConverter.convertToDto(result, recordingBlobData.getContent());
    }

    @Override
    public String toDto(RecordingBlobData recordingBlobData) {
        return blobConverter.toDto(recordingBlobData.getContent());
    }

    @Override
    public RecordingBlobData convertFromDto(RecordingBlobData result, String s) {
        if (result == null) {
            result = createDomainInstance(null);
        }
        result.setContent(blobConverter.fromDto(s));
        return result;
    }

    @Override
    public RecordingBlobData fromDto(String s) {
        return convertFromDto(null, s);
    }

    @Override
    public boolean equals(Object domain, Object dto) {
        return false;
    }
}