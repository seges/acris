package sk.seges.acris.scaffold.model.domain;

public interface CustomerModel<K> {
	K id();
	
	String firstName();
	String surname();
	
	RoleModel role();
}
