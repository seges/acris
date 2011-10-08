package sk.seges.corpis.appscaffold.datainterface.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
public interface CustomerModel<T> {
	T id();
	
	String firstName();
	String surname();
	
	RoleModel role();
}
