package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = HibernateGenericUser.class)
@Mapping(MappingType.EXPLICIT)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface GenericUserDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = UserData.class, configuration = GenericUserDTOConfiguration.class)
	public interface UserDataConfiguration {}

	void id();
	void userAuthorities();
	void username();
	void password();
	void webId();
	void roles();
	void email();
	void name();
	void surname();
	void contact();
	
}