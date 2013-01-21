package sk.seges.corpis.appscaffold.data.model.configuration;

import java.io.Serializable;

import javax.persistence.EntityManager;

import sk.seges.corpis.service.annotation.TransactionPropagationModel;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

public class PagedResultConverter<DTO_T, DOMAIN_T> extends BasicCachedConverter<PagedResult<DTO_T>, PagedResult<DOMAIN_T>> {

	public PagedResultConverter(ConvertedInstanceCache arg0, ConverterProviderContext context) {
		super(arg0);
	}

	public void setTransactionPropagations(TransactionPropagationModel[] transactionPropagations) {}
	public void setEntityManager(EntityManager entityManager) {};
	
	public PagedResultConverter(ConverterProviderContext converterProviderContext) {
		this(new sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache(), converterProviderContext);
	}

	@Override
	public PagedResult<DTO_T> convertToDto(PagedResult<DTO_T> result, PagedResult<DOMAIN_T> domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult<DTO_T> toDto(PagedResult<DOMAIN_T> domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult<DOMAIN_T> convertFromDto(PagedResult<DOMAIN_T> result, PagedResult<DTO_T> dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult<DOMAIN_T> fromDto(PagedResult<DTO_T> dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(PagedResult<DOMAIN_T> domain, PagedResult<DTO_T> dto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public PagedResult<DTO_T> createDtoInstance(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagedResult<DOMAIN_T> createDomainInstance(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}
}
