package sk.seges.acris.recorder.server.domain.jpa;

import sk.seges.acris.recorder.server.model.base.RecordingBlobBase;
import sk.seges.acris.recorder.server.model.base.RecordingBlobPkBase;
import sk.seges.acris.recorder.server.model.data.RecordingBlobPkData;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by PeterSimun on 13.4.2014.
 */
@Entity
@Table(name = "recording_blob")
public class JpaRecordingBlob extends RecordingBlobBase {

    @Override
    @EmbeddedId
    public JpaRecordingBlobPk getId() {
        return (JpaRecordingBlobPk) super.getId();
    }

    public void setId(JpaRecordingBlobPk id) {
        super.setId(id);
    }

    @Override
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public Blob getContent() {
        return super.getContent();
    }
}