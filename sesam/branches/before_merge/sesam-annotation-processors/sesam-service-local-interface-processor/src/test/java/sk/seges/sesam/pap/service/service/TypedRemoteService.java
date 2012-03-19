package sk.seges.sesam.pap.service.service;

import java.io.Serializable;
import java.util.List;

import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

@RemoteServiceDefinition
public interface TypedRemoteService<T extends Comparable<Serializable>> {

	T find();
	List<T> findAll();
	List<List<T>> findMatrix();
	
	List<? extends Serializable> findEmAll();

	<S extends Serializable> S findSomething();
	
	void put(T t);
	void putAll(T...t);
}