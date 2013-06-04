package sk.seges.acris.pap.service.model;

import java.io.Serializable;
import java.util.List;

import sk.seges.sesam.security.shared.model.api.ClientSecuredEntity;
import sk.seges.sesam.shared.domain.api.HasId;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mock")
public interface TypedService<T extends Comparable<Serializable>> extends RemoteService {

	T find();
	List<T> findAll();
	List<? extends Serializable> findEmAll();

	<S extends Serializable> S findSomething();
	<Z extends HasId<?>> List<ClientSecuredEntity<Z>> fetchAclSecurityData(List<Z> dtos);

	void put(T t);
	void putAll(T...t);
}
