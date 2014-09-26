package sk.seges.acris.security.user_management;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

/** Interface for hierarchical permission. */
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
