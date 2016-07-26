package sk.seges.acris.security.server.user_management.model.dto.configuration;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.acris.core.shared.model.IDataTransferObject;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.pap.model.annotation.Annotations;
import sk.seges.sesam.pap.model.annotation.Copy;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.PropertyAccessor;
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

	@Copy(annotations = @Annotations(accessor = PropertyAccessor.PROPERTY, packageOf = Size.class))		
	@NotNull
	@Size(message = "{javax_validation_constraints_Size_message}")
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