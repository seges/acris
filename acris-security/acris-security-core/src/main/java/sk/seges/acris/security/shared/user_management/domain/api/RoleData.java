package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@BeanWrapper
public interface RoleData extends Serializable, IDomainObject<Integer> {

	public static final String NONE = "none";
	public static final String ALL_USERS = "*";
	public static final String GRANT = "USER_ROLE";
	public static final String A_NAME = "name";
	public static final String A_SELECTED_AUTHORITIES = "selectedAuthorites";
	
	List<String> getSelectedAuthorities();

	void setSelectedAuthorities(List<String> securityPermissions);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);
	
	String getWebId();
	
	void setWebId(String webId);

}