package sk.seges.corpis.appscaffold.shared.annotation;

public @interface DomainInterfaceSpec {
	PersistenceType[] generateDao();
}