package sk.seges.acris.recorder.server.domain.jpa;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;
import sk.seges.acris.recorder.server.model.base.RecordingSessionBase;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recording_session")
@SequenceGenerator(name = "seqRecordingSession", sequenceName = "seq_recording_session", initialValue = 1)
public class JpaRecordingSession extends RecordingSessionBase {

    private int logsCount;

    @Override
    @Formula("(select count(*) from recording_log where recording_log.session_id = id)")
    public int getLogsCount() {
        return logsCount;
    }

    public void setLogsCount(int logsCount) {
        this.logsCount = logsCount;
    }

    @Id
	@Override
	@GeneratedValue(generator = "seqRecordingSession")
	public Long getId() {
		return super.getId();
	}

	@Override
	@OneToMany(targetEntity = JpaRecordingLog.class, mappedBy = "session", cascade = { CascadeType.REMOVE })
	public List<RecordingLogData> getAuditLogs() {
		return super.getAuditLogs();
	}

	@Column
	@Override
	public Date getSessionTime() {
		return super.getSessionTime();
	}

	@Override
	@Column
	public String getLanguage() {
		return super.getLanguage();
	}

	@Override
	@Column(length = 2096)
	public String getSessionInfo() {
		return super.getSessionInfo();
	}

	@Override
	@ManyToOne(targetEntity = HibernateGenericUser.class)
	public UserData getUser() {
		return super.getUser();
	}

	@Override
	@Column
	public String getWebId() {
		return super.getWebId();
	}
}
