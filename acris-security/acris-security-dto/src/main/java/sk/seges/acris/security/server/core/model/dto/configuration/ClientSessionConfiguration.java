package sk.seges.acris.security.server.core.model.dto.configuration;

import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.session.ClientSessionDTO;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.server.model.converter.common.MapConverter;
import sk.seges.sesam.shared.model.api.PropertyHolder;

import java.util.Map;

@TransferObjectMapping(domainClass = ClientSession.class, dtoClass = ClientSessionDTO.class, converter = ClientSessionConverter.class)
@Mapping(Mapping.MappingType.EXPLICIT)
public interface ClientSessionConfiguration {

	@TransferObjectMapping(converter = SessionMapConverter.class)
	Map<String, PropertyHolder> session();

	void user();
}