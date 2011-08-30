package sk.seges.acris.security.shared.user_management.domain.api;

import java.util.List;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@BeanWrapper
public interface UserData<E> extends IMutableDomainObject<E> {

	boolean hasAuthority(String authority);

	List<String> getUserAuthorities();

	void setUserAuthorities(List<String> authorities);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	boolean isEnabled();

	void setEnabled(boolean enabled);
}