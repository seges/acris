package sk.seges.corpis.appscaffold.jpamodel.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
public interface CustomerModel {
	Object id();
	
	String firstName();
	String surname();
	
	RoleModel role();
//	List<ThemeModel> themes();
}
