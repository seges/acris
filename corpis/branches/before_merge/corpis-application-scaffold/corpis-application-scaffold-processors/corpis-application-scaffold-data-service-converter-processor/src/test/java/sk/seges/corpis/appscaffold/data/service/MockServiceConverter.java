package sk.seges.corpis.appscaffold.data.service;

import java.util.List;

import javax.annotation.Generated;
import javax.persistence.EntityManager;

import sk.seges.corpis.appscaffold.data.model.configuration.PagedResultConverter;
import sk.seges.corpis.appscaffold.data.model.converter.MockEntityDTOConverter;
import sk.seges.corpis.appscaffold.data.model.dto.MockEntityDTO;
import sk.seges.corpis.appscaffold.data.model.entity.MockEntity;
import sk.seges.corpis.appscaffold.data.service.MockLocalService;
import sk.seges.corpis.appscaffold.data.service.MockRemoteService;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationTarget;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.common.CollectionConverter;

@LocalServiceConverter(remoteServices = {MockRemoteService.class})
@Generated(value = "sk.seges.corpis.appscaffold.model.pap.DataServiceConverterProcessor")
public class MockServiceConverter implements MockRemoteService {

	private MockLocalService mockLocalServiceService;
	private EntityManager entityManager;


	public MockServiceConverter(MockLocalService mockLocalServiceService, EntityManager entityManager) {

		this.mockLocalServiceService = mockLocalServiceService;
		this.entityManager = entityManager;
	}

	@Override
	public MockEntityDTO find(){
		return (MockEntityDTO)getMockEntityDTOConverter(new TransactionPropagationModel[] {new TransactionPropagationModel(new String[] {}, new PropagationTarget[] {PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS}, PropagationType.ISOLATE)}).toDto((MockEntity)mockLocalServiceService.find());
	}

	@Override
	public PagedResult<List<MockEntityDTO>> findAll(){
		return (PagedResult<List<MockEntityDTO>>)((PagedResultConverter)getPagedResultConverter(new TransactionPropagationModel[] {new TransactionPropagationModel(new String[] {}, new PropagationTarget[] {PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS}, PropagationType.ISOLATE)}, ((CollectionConverter)getCollectionConverter(getMockEntityDTOConverter(new TransactionPropagationModel[] {new TransactionPropagationModel(new String[] {}, new PropagationTarget[] {PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS}, PropagationType.ISOLATE)}))))).toDto((PagedResult<List<MockEntity>>)mockLocalServiceService.findAll());
	}

	private<DTO, DOMAIN> CollectionConverter<? extends DTO, ? extends DOMAIN> getCollectionConverter(DtoConverter<DTO, DOMAIN> converter) {
		return new CollectionConverter<DTO, DOMAIN>(converter);
	}

	private<DTO_T, DOMAIN_T> PagedResultConverter<? extends DTO_T, ? extends DOMAIN_T> getPagedResultConverter(TransactionPropagationModel[] transactionPropagations, DtoConverter<DTO_T, DOMAIN_T> converter0) {
		return new PagedResultConverter<DTO_T, DOMAIN_T>(entityManager, transactionPropagations, converter0);
	}

	private MockEntityDTOConverter getMockEntityDTOConverter(TransactionPropagationModel[] transactionPropagations) {
		return new MockEntityDTOConverter(entityManager, transactionPropagations);
	}

}