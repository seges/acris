package sk.seges.acris.recorder.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingLogDao;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingSessionDao;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.server.rest.RecordingResource;
import sk.seges.acris.recorder.server.service.IRecordingRemoteServiceLocal;
import sk.seges.acris.recorder.server.service.RecordingLocalService;
import sk.seges.acris.recorder.server.service.RecordingService;
import sk.seges.acris.recorder.server.service.RecordingServiceConverter;
import sk.seges.acris.recorder.shared.service.IRecordingRemoteService;
import sk.seges.corpis.server.model.converter.provider.AbstractContextualConverterProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RecordingSpringConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao() {
		HibernateJpaRecordingSessionDao hibernateJpaRecordingSessionDao = new HibernateJpaRecordingSessionDao();
		hibernateJpaRecordingSessionDao.setEntityManager(entityManager);
		return hibernateJpaRecordingSessionDao;
	}

	@Bean
	public RecordingLogDaoBase<RecordingLogData> recordingLogDao() {
		HibernateJpaRecordingLogDao hibernateJpaRecordingLogDao = new HibernateJpaRecordingLogDao();
		hibernateJpaRecordingLogDao.setEntityManager(entityManager);
		return hibernateJpaRecordingLogDao;
	}

	@Bean
	public RecordingLocalService recordingLocalService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao) {
		return new RecordingService(recordingSessionDao, recordingLogDao);
	}

	@Bean
	public IRecordingRemoteService recordingRemoteService(IRecordingRemoteServiceLocal iRecordingRemoteServiceLocalService, AbstractContextualConverterProvider converterProviderContext) {
		return new RecordingServiceConverter(iRecordingRemoteServiceLocalService, converterProviderContext, entityManager);
	}

	@Bean
	public RecordingResource recordingResource(RecordingLocalService recordingLocalService) {
		return new RecordingResource(recordingLocalService);
	}
}