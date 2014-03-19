package sk.seges.acris.recorder.server.domain.jpa;

import sk.seges.acris.recorder.server.model.base.RecordingLogBase;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recording_log")
@SequenceGenerator(name = "seqRecordingLog", sequenceName = "seq_recording_log", initialValue = 1)
public class JpaRecordingLog extends RecordingLogBase {

	@Id
	@Override
	@GeneratedValue(generator = "seqRecordingLog")
	public String getId() {
		return super.getId();
	}

	@Override
	@Column
	public String getEvent() {
		return super.getEvent();
	}

	@Override
	@Column
	public Date getEventTime() {
		return super.getEventTime();
	}

	@Override
	@OneToMany(targetEntity = JpaRecordingSession.class)
	public RecordingSessionData getSession() {
		return super.getSession();
	}
}
