package sk.seges.acris.security.user_management;

import java.util.List;

import sk.seges.acris.core.shared.common.HasDescription;
import sk.seges.acris.core.shared.common.HasEmail;
import sk.seges.acris.core.shared.common.HasName;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface User extends IMutableDomainObject<Long>, HasName, HasDescription, HasWebId, HasEmail {

	List<String> userAuthorities();
	String username();
	String password();
	boolean enabled();
	List<Role> roles();
	String surname();
	String contact();	
	UserPreferences userPreferences();
}