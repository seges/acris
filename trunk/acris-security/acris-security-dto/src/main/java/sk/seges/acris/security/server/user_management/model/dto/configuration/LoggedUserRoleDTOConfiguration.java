package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.security.server.util.LoggedUserRole;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = LoggedUserRole.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public class LoggedUserRoleDTOConfiguration {}