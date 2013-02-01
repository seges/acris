package sk.seges.acris.security.user_management;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
interface UserRolePermission extends IDomainObject<String>, Serializable {

	String permission();

	List<String> userPermissions();

}