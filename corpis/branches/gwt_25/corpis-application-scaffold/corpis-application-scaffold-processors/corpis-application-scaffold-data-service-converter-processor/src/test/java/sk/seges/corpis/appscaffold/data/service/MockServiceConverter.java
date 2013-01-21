package sk.seges.corpis.appscaffold.data.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.EntityManager;

import sk.seges.corpis.appscaffold.data.model.configuration.PagedResultConverter;
import sk.seges.corpis.appscaffold.data.model.converter.MockEntityDTOConverter;
import sk.seges.corpis.appscaffold.data.model.dto.MockEntityDTO;
import sk.seges.corpis.appscaffold.data.model.entity.MockEntity;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationTarget;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.AbstractConverterPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.common.CollectionConverter;
import sk.seges.sesam.shared.model.converter.provider.AbstractConverterProvider;

@LocalServiceConverter(remoteServices = {MockRemoteService.class})
@Generated(value = "sk.seges.corpis.appscaffold.model.pap.DataServiceConverterProcessor")
public class MockServiceConverter implements MockRemoteService {

	private MockLocalService mockLocalServiceService;
	private EntityManager entityManager;
	private ConvertedInstanceCache cache = new MapConvertedInstanceCache();
	private ConverterProviderContext context = new MockConverterProviderContext();
	
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

	class MockConverterProviderContext extends ConverterProviderContext {
		public MockConverterProviderContext() {
			registerConverterProvider(new MockConverterProvider(this));
		}
	}
	
	class MockConverterProvider extends AbstractConverterProvider {

		private ConverterProviderContext converterProviderContext;
		
		public MockConverterProvider(ConverterProviderContext converterProviderContext) {
			this.converterProviderContext = converterProviderContext;
		}
		
		@Override
		public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(Class<DOMAIN> domainClass) {
			if (domainClass.isAssignableFrom(Collection.class)) {
				return (DtoConverter<DTO, DOMAIN>) new CollectionConverter<DTO, DOMAIN>(context);
			}
			
			if (domainClass.isAssignableFrom(PagedResult.class)) {
				return (DtoConverter<DTO, DOMAIN>) new PagedResultConverter<DTO, DOMAIN>(cache, context);
			}

			return null;
		}

		@Override
		public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(Class<DTO> dtoClass) {
			if (dtoClass.isAssignableFrom(Collection.class)) {
				return (DtoConverter<DTO, DOMAIN>) new CollectionConverter<DTO, DOMAIN>(converterProviderContext);
			}
			
			if (dtoClass.getClass().isAssignableFrom(PagedResult.class)) {
				return (DtoConverter<DTO, DOMAIN>) new PagedResultConverter<DTO, DOMAIN>(cache, converterProviderContext);
			}

			return null;
		}
	}
	
	private<DTO_T, DOMAIN_T> PagedResultConverter<? extends DTO_T, ? extends DOMAIN_T> getPagedResultConverter(TransactionPropagationModel[] transactionPropagations) {
		PagedResultConverter<DTO_T, DOMAIN_T> pagedResultConverter = new PagedResultConverter<DTO_T, DOMAIN_T>(cache, context);
		pagedResultConverter.setTransactionPropagations(transactionPropagations);
		pagedResultConverter.setEntityManager(entityManager);
		return pagedResultConverter;
	}

	private MockEntityDTOConverter getMockEntityDTOConverter(TransactionPropagationModel[] transactionPropagations) {
		return new MockEntityDTOConverter(entityManager, transactionPropagations);
	}

}