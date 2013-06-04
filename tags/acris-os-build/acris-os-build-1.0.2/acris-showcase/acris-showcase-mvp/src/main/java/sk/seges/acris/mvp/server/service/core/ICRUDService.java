package sk.seges.acris.mvp.server.service.core;

import java.util.List;

import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

public interface ICRUDService<T extends IDomainObject<?>> {

	T persist(T entity);

	T merge(T entity);

	void remove(T entity);

	PagedResult<List<T>> findAll(Page requestedPage);

	T findEntity(T entity);
}