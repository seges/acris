package sk.seges.acris.player.server.spring;

import org.springframework.context.annotation.Bean;
import sk.seges.acris.player.server.service.IPlayerRemoteServiceLocal;
import sk.seges.acris.player.server.service.PlayerService;
import sk.seges.acris.player.server.service.PlayerServiceConverter;
import sk.seges.acris.player.shared.service.IPlayerRemoteService;
import sk.seges.acris.recorder.server.dao.api.RecordingBlobDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingLogDaoBase;
import sk.seges.acris.recorder.server.dao.api.RecordingSessionDaoBase;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingBlobDao;
import sk.seges.acris.recorder.server.domain.jpa.dao.hibernate.HibernateJpaRecordingSessionDao;
import sk.seges.acris.recorder.server.model.data.RecordingBlobData;
import sk.seges.acris.recorder.server.model.data.RecordingLogData;
import sk.seges.acris.recorder.server.model.data.RecordingSessionData;
import sk.seges.acris.recorder.server.rest.PlayerResource;
import sk.seges.corpis.server.model.converter.provider.AbstractContextualConverterProvider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PlayerSpringConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

    @Bean
    public RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao() {
        HibernateJpaRecordingBlobDao hibernateJpaRecordingBlobDao = new HibernateJpaRecordingBlobDao();
        hibernateJpaRecordingBlobDao.setEntityManager(entityManager);
        return hibernateJpaRecordingBlobDao;
    }

    @Bean
	public IPlayerRemoteServiceLocal playerLocalService(RecordingSessionDaoBase<RecordingSessionData> recordingSessionDao, RecordingLogDaoBase<RecordingLogData> recordingLogDao,
                                                        RecordingBlobDaoBase<RecordingBlobData> recordingBlobDao) {
		return new PlayerService(entityManager, recordingSessionDao, recordingLogDao, recordingBlobDao);
	}

	@Bean
	public IPlayerRemoteService playerRemoteService(IPlayerRemoteServiceLocal iPlayerRemoteServiceLocalService, AbstractContextualConverterProvider converterProviderContext) {
		return new PlayerServiceConverter(iPlayerRemoteServiceLocalService, converterProviderContext, entityManager);
	}

    @Bean
    public PlayerResource playerResource(IPlayerRemoteServiceLocal playerLocalService) {
        return new PlayerResource(playerLocalService);
    }


}
