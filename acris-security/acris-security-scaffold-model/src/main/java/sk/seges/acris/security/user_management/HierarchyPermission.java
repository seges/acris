package sk.seges.acris.security.user_management;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface HierarchyPermission extends Serializable, IMutableDomainObject<Integer>, HasWebId {
 
	String permission();

	List<HierarchyPermission> childPermissions();

	Integer level();

	Boolean hasChildren();

	HierarchyPermission parent();

	String discriminator();
}