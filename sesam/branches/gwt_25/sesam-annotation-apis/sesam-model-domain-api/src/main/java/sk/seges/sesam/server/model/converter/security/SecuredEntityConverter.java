package sk.seges.sesam.server.model.converter.security;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.security.server.model.acl.AclDataRegistry;
import sk.seges.sesam.security.server.model.acl.AclSecuredEntity;
import sk.seges.sesam.security.shared.model.api.ClientSecuredEntity;
import sk.seges.sesam.shared.domain.api.HasId;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

@TransferObjectMapping(dtoClass = ClientSecuredEntity.class, domainClass = AclSecuredEntity.class)
public class SecuredEntityConverter extends BasicCachedConverter<ClientSecuredEntity<HasId<?>>, AclSecuredEntity<IDomainObject<?>>> {

	private final ConverterProviderContext converterProviderContext;
	private final AclDataRegistry aclDataRegistry;
	
	public SecuredEntityConverter(ConvertedInstanceCache cache, ConverterProviderContext converterProviderContext, AclDataRegistry aclDataRegistry) {
		super(cache);
		this.aclDataRegistry = aclDataRegistry;
		this.converterProviderContext = converterProviderContext;
	}

	@Override
	public ClientSecuredEntity<HasId<?>> convertToDto(ClientSecuredEntity<HasId<?>> result, AclSecuredEntity<IDomainObject<?>> domain) {
		DtoConverter<Object, Object> converterForDomain = converterProviderContext.getConverterForDomain((Object)domain.getEntity());
		Object dto = converterForDomain.toDto(domain.getEntity());
		if (dto instanceof HasId) {
			result.setEntity((HasId<?>) dto);
		}
		result.setPermissionData(domain.getPermissionData());
		
		//ClassConverter.getDtoClassName(converterProviderContext, domain.getEntity().getClass())
		return result;
	}

	@Override
	public ClientSecuredEntity<HasId<?>> toDto(AclSecuredEntity<IDomainObject<?>> domain) {
		return convertToDto(null, domain);
	}

	@Override
	public AclSecuredEntity<IDomainObject<?>> convertFromDto(AclSecuredEntity<IDomainObject<?>> result, ClientSecuredEntity<HasId<?>> dto) {
		DtoConverter<Object, Object> converterForDto = converterProviderContext.getConverterForDto((Object)dto.getEntity());
		Object domain = converterForDto.fromDto(dto.getEntity());
		if (domain instanceof IDomainObject) {
			result.setEntity((IDomainObject<?>) domain);
		}
		result.setPermissionData(dto.getPermissionData());
		result.setAclData(aclDataRegistry.getAclSecurityData(result.getEntity()));

		return result;
	}

	@Override
	public AclSecuredEntity<IDomainObject<?>> fromDto(ClientSecuredEntity<HasId<?>> dto) {
		return convertFromDto(null, dto);
	}

	@Override
	public boolean equals(AclSecuredEntity<IDomainObject<?>> domain, ClientSecuredEntity<HasId<?>> dto) {
		Object dtoId = domain.getEntity().getId();
		if (dtoId == null) {
			return false;
		}
	
		return dtoId.equals(dto.getEntity().getId());
	}
}