package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@BeanWrapper
public interface HasOpenIDIdentifier extends IMutableDomainObject<String> {

	UserData getUser();

	void setUser(UserData user);

	String getEmail();

	void setEmail(String email);
 
	OpenIDProvider getProvider();

	void setProvider(OpenIDProvider provider);
}
