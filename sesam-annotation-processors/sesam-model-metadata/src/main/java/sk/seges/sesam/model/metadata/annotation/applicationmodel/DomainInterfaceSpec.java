package sk.seges.sesam.model.metadata.annotation.applicationmodel;

public @interface DomainInterfaceSpec {
	PersistenceType[] generateDao();
}