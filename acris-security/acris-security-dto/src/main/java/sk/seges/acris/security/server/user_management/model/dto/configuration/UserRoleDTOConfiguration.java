package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaSecurityRole;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaSecurityRole.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface UserRoleDTOConfiguration {}