package sk.seges.corpis.appscaffold.jpamodel.pap;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@DomainInterface
public interface CustomerModel<T> {
	T id();
	
	String firstName();
	String surname();
	
	RoleModel<T> role();
//	List<ThemeModel> themes();
}
