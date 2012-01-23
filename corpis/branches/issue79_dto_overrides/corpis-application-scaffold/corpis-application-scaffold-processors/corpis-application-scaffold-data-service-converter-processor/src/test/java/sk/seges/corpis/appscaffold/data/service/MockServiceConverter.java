package sk.seges.corpis.appscaffold.data.service;

import java.util.Collection;
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
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.common.CollectionConverter;

@LocalServiceConverter(remoteServices = {MockRemoteService.class})
@Generated(value = "sk.seges.corpis.appscaffold.model.pap.DataServiceConverterProcessor")
public class MockServiceConverter implements MockRemoteService, ConverterProvider {

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
		return (PagedResult<List<MockEntityDTO>>)((PagedResultConverter)getPagedResultConverter(new TransactionPropagationModel[] {new TransactionPropagationModel(new String[] {}, new PropagationTarget[] {PropagationTarget.RETURN_VALUE, PropagationTarget.ARGUMENTS}, PropagationType.ISOLATE)})).toDto((PagedResult<List<MockEntity>>)mockLocalServiceService.findAll());
	}

	private<DTO, DOMAIN> CollectionConverter<? extends DTO, ? extends DOMAIN> getCollectionConverter() {
		return new CollectionConverter<DTO, DOMAIN>(this);
	}

	private<DTO_T, DOMAIN_T> PagedResultConverter<? extends DTO_T, ? extends DOMAIN_T> getPagedResultConverter(TransactionPropagationModel[] transactionPropagations) {
		PagedResultConverter<DTO_T, DOMAIN_T> pagedResultConverter = new PagedResultConverter<DTO_T, DOMAIN_T>(this);
		pagedResultConverter.setTransactionPropagations(transactionPropagations);
		pagedResultConverter.setEntityManager(entityManager);
		return pagedResultConverter;
	}

	private MockEntityDTOConverter getMockEntityDTOConverter(TransactionPropagationModel[] transactionPropagations) {
		return new MockEntityDTOConverter(entityManager, transactionPropagations);
	}

	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain) {
		if (domain.getClass().isAssignableFrom(Collection.class)) {
			return (DtoConverter<DTO, DOMAIN>) new CollectionConverter<DTO, DOMAIN>(this);
		}
		
		if (domain.getClass().isAssignableFrom(PagedResult.class)) {
			return (DtoConverter<DTO, DOMAIN>) new PagedResultConverter<DTO, DOMAIN>(this);
		}

		return null;
	}

	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto) {
		if (dto.getClass().isAssignableFrom(Collection.class)) {
			return (DtoConverter<DTO, DOMAIN>) new CollectionConverter<DTO, DOMAIN>(this);
		}
		
		if (dto.getClass().isAssignableFrom(PagedResult.class)) {
			return (DtoConverter<DTO, DOMAIN>) new PagedResultConverter<DTO, DOMAIN>(this);
		}

		return null;
	}

}