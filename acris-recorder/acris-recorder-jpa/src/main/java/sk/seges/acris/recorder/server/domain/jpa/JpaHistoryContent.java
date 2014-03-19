package sk.seges.acris.recorder.server.domain.jpa;

import sk.seges.acris.recorder.server.model.base.HistoryContentBase;

import javax.persistence.*;

@Entity
@Table(name = "history_content")
@SequenceGenerator(name = "seqHistoryContent", sequenceName = "seq_history_content", initialValue = 1)
public class JpaHistoryContent extends HistoryContentBase {

	@Override
	@Id
	@GeneratedValue(generator = "seqHistoryContent")
	public Long getId() {
		return super.getId();
	}
}