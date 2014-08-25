package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.core.shared.model.IDataTransferObject;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateSecurityRole;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = HibernateSecurityRole.class)
@Mapping(MappingType.EXPLICIT)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface SecurityRoleDTOConfiguration extends IDataTransferObject {

	static final String LOGGED_ROLE_NAME = "logged_user_role";

	static final String NONE = "none";
	static final String ALL_USERS = "*";
	static final String GRANT = "USER_ROLE";

	@TransferObjectMapping(domainClass = RoleData.class, configuration = SecurityRoleDTOConfiguration.class)
	public interface RoleDataConfiguration {}

	void id();
	void name();
	void description();
	void selectedAuthorities();
	void webId();

}