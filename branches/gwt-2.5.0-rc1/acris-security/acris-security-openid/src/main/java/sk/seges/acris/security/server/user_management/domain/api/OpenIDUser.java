package sk.seges.acris.security.server.user_management.domain.api;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@BeanWrapper 
@MetaModel
@DomainInterface
@BaseObject
public interface OpenIDUser extends IMutableDomainObject<String> {

	UserData user();

	String email();

	OpenIDProvider provider();

}