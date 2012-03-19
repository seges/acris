package sk.seges.sesam.domain;

public interface IMutableDomainObject<T> extends IDomainObject<T> {

	void setId(T t);
}