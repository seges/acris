package sk.seges.acris.recorder.server.domain.jpa;

import sk.seges.acris.recorder.server.model.base.RecordingBlobBase;
import sk.seges.acris.recorder.server.model.base.RecordingBlobPkBase;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by PeterSimun on 13.4.2014.
 */
@Embeddable
public class JpaRecordingBlobPk extends RecordingBlobPkBase {

    @Override
    @Column(nullable = true)
    public Long getBlobId() {
        return super.getBlobId();
    }

    @Override
    @Column(nullable = true)
    public Long getSessionId() {
        return super.getSessionId();
    }
}
