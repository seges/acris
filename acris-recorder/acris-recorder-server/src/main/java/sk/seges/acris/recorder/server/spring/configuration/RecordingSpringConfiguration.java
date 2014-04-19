package sk.seges.acris.recorder.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import sk.seges.acris.recorder.server.dao.api.RecordingBlobDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingBlobDao;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingLogDao;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingSessionDao;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.server.rest.RecordingResource;
import sk.seges.acris.recorder.server.service.IRecordingRemoteServiceLocal;
import sk.seges.acris.recorder.server.service.RecordingLocalService;
import sk.seges.acris.recorder.server.service.RecordingService;
import sk.seges.acris.recorder.server.service.RecordingServiceConverter;
import sk.seges.acris.recorder.server.util.BlobHelper;
import sk.seges.acris.recorder.server.util.hibernate.HibernateBlobHelper;
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
    public RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao() {
        HibernateJpaRecordingBlobDao hibernateJpaRecordingBlobDao = new HibernateJpaRecordingBlobDao();
        hibernateJpaRecordingBlobDao.setEntityManager(entityManager);
        return hibernateJpaRecordingBlobDao;
    }

    @Bean
    public BlobHelper blobHelper() {
        return new HibernateBlobHelper(entityManager);
    }

    @Bean
	public RecordingLocalService recordingLocalService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao,
                                                       RecordingLogDaoBase<RecordingLogData> recordingLogDao,
                                                       RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao,
                                                       BlobHelper blobHelper) {
		return new RecordingService(recordingSessionDao, recordingLogDao, recordingBlobDao, blobHelper);
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