package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper
public interface RoleData extends Serializable, IDomainObject<Integer> {

	List<String> getSelectedAuthorities();

	void setSelectedAuthorities(List<String> securityPermissions);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

}