package sk.seges.acris.recorder.server.domain.jpa.dao.hibernate;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingLog;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import javax.annotation.Generated;
import java.util.List;

@Generated(value = "sk.seges.corpis.core.pap.dao.HibernateDataDaoProcessor")
public class HibernateJpaRecordingLogDao extends AbstractHibernateCRUD<RecordingLogData> implements RecordingLogDaoBase<RecordingLogData> {
	 
	public HibernateJpaRecordingLogDao() {
		super(JpaRecordingLog.class);
	}
	
	public HibernateJpaRecordingLogDao(Class<? extends JpaRecordingLog> clazz) {
		super(clazz);
	}
	
	@Override
	public JpaRecordingLog getEntityInstance() {
		return new JpaRecordingLog();
	}

	@Override
	@Transactional
	public RecordingLogData persist(RecordingLogData entity) {
		entity.setEvent(entity.getEvent().replace('\0', ' '));
		return super.persist(entity);
	}

	@Override
	@Transactional
	public RecordingLogData merge(RecordingLogData entity) {
		entity.setEvent(entity.getEvent().replace('\0', ' '));
		return super.merge(entity);
	}
}