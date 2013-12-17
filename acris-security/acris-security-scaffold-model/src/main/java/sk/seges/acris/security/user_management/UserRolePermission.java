package sk.seges.acris.security.user_management;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface UserRolePermission extends IMutableDomainObject<String>, Serializable {

	String permission();

	List<String> userPermissions();

}