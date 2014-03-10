package sk.seges.acris.security.server.core.model.dto.configuration;

import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.session.ClientSessionDTO;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.corpis.pap.converter.hibernate.TransactionalConverter;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.server.model.converter.common.MapConverter;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.api.InstantiableDtoConverter;
import sk.seges.sesam.utils.CastUtils;

import javax.annotation.Generated;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
@TransferObjectMapping(dtoClass = ClientSessionDTO.class,
		domainClassName = "sk.seges.acris.security.server.session.ClientSession", 
		configurationClassName = "sk.seges.acris.security.server.core.model.dto.configuration.ClientSessionConfiguration", 
		generateConverter = false, generateDto = false, 
		converterClassName = "sk.seges.acris.security.server.core.model.dto.configuration.ClientSessionConverter")
@Generated(value = "sk.seges.corpis.pap.model.hibernate.HibernateTransferObjectDataConverterProcessor")
public class ClientSessionConverter extends TransactionalConverter<ClientSessionDTO, ClientSession> {
	 
	protected final EntityManager entityManager;
	 
	protected final ConverterProviderContext converterProviderContext;
	 
	public ClientSessionConverter(EntityManager entityManager, ConverterProviderContext converterProviderContext) {
		super();
		this.entityManager = entityManager;
		this.converterProviderContext = converterProviderContext;
	}
	 
	public boolean equals(Object _domainArg, Object _dtoArg) {
		if (_domainArg == null) {
			return (_dtoArg == null);
		}
	
		if (_dtoArg == null) {
			return false;
		}
	
		if (!(_domainArg instanceof ClientSession)) {
			return false;
		}
	
		ClientSession _domain = (ClientSession)_domainArg;
	
		if (!(_dtoArg instanceof ClientSessionDTO)) {
			return false;
		}
	
		ClientSessionDTO _dto = (ClientSessionDTO)_dtoArg;
	
		if (_domain.getSession() == null) {
			if (_dto.getSession() != null)
				return false;
		} else if (!_domain.getSession().equals(_dto.getSession()))
			return false;
		if (_domain.getUser() == null) {
			if (_dto.getUser() != null)
				return false;
		} else if (!_domain.getUser().equals(_dto.getUser()))
			return false;
		return true;
	}
	
	public ClientSessionDTO createDtoInstance(Serializable id) {
		ClientSessionDTO _result = new ClientSessionDTO();
		return _result;
	}
	
	public ClientSessionDTO toDto(ClientSession _domain) {
	
		if (_domain  == null) {
			return null;
		}
	
		ClientSessionDTO _result = createDtoInstance(null);
		return convertToDto(_result, _domain);
	}
	
	public ClientSessionDTO convertToDto(ClientSessionDTO _result, ClientSession _domain) {
	
		if (_domain  == null) {
			return null;
		}

		SessionMapConverter converterSession = new SessionMapConverter(converterProviderContext);
		if (converterSession != null) {
			_result.setSession(converterSession.toDto(CastUtils.cast((Map<String, Object>)_domain.getSession(), Map.class), java.util.HashMap.class));
		}
		InstantiableDtoConverter<GenericUserDTO, UserData> converterUser = ((InstantiableDtoConverter<GenericUserDTO, UserData>)(DtoConverter)converterProviderContext.getConverterForDomain(_domain.getUser()));
		if (converterUser != null) {
			_result.setUser(converterUser.toDto(CastUtils.cast((UserData)_domain.getUser(), UserData.class)));
		}
		return _result;
	}
	
	public ClientSession createDomainInstance(Serializable id) {
		if (id != null) {
			ClientSession _result = (ClientSession)entityManager.find(ClientSession.class, id);
			if (_result != null) {
				return _result;
			}
		}
	
		 return new ClientSession();
	}
	
	public ClientSession fromDto(ClientSessionDTO _dto) {
	
		if (_dto == null) {
			return null;
		}
	
		ClientSession _result = createDomainInstance(null);
	
		return convertFromDto(_result, _dto);
	}
	
	public ClientSession convertFromDto(ClientSession _result, ClientSessionDTO _dto) {
	
		if (_dto  == null) {
			return null;
		}

		SessionMapConverter converterSession = new SessionMapConverter(converterProviderContext);
		if (converterSession != null) {
			_result.setSession((Map<String, Object>)converterSession.fromDto((Map<String, PropertyHolder>)_dto.getSession()));
		}
		InstantiableDtoConverter<GenericUserDTO, UserData> converterUser = ((InstantiableDtoConverter<GenericUserDTO, UserData>)(DtoConverter)converterProviderContext.getConverterForDto(_dto.getUser()));
		if (converterUser != null) {
			_result.setUser((UserData)converterUser.fromDto((GenericUserDTO)_dto.getUser()));
		}
		return _result;
	}
}
