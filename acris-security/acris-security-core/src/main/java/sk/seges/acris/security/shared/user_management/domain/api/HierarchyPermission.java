package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@BeanWrapper
public interface HierarchyPermission extends Serializable, IDomainObject<Integer> {
 
	//FIXME
	String getWebId();

	//FIXME should be on another interface
	void setWebId(String webId);

	String getPermission();

	void setPermission(String permission);

	List<HierarchyPermission> getChildPermissions();

	void setChildPermissions(List<HierarchyPermission> childPermissions);

	void addChildPermissions(HierarchyPermission... permissions);

	Integer getLevel();

	void setLevel(Integer level);

	Boolean getHasChildren();

	void setHasChildren(Boolean hasChildren);

	HierarchyPermission getParent();

	void setParent(HierarchyPermission parent);
}