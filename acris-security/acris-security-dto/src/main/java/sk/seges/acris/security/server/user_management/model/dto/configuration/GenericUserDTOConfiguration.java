package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.core.shared.model.IDataTransferObject;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.pap.model.annotation.*;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@TransferObjectMapping(domainClass = HibernateGenericUser.class)
@Mapping(MappingType.EXPLICIT)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface GenericUserDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = UserData.class, configuration = GenericUserDTOConfiguration.class)
	public interface UserDataConfiguration {}

	void id();
	void userAuthorities();

	@NotNull
	@Size(min = 3, message = "{user_wrong_length}")
	void username();

	@NotNull
	@Size(min = 3, message = "{customer_wrong_password}")
	void password();
	void webId();
	void roles();
	void email();
	void name();
	void surname();
	void contact();
	
}