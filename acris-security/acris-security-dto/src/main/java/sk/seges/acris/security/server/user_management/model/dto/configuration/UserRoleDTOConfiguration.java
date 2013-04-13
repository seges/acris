package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateSecurityRole;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = HibernateSecurityRole.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface UserRoleDTOConfiguration {}