package sk.seges.corpis.appscaffold.jpamodel.pap.api;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
public interface CustomerModel<T> {
	T id();
	
	String firstName();
	String surname();
	
	RoleModel<T> role();
//	List<ThemeModel> themes();
}
