package sk.seges.acris.mvp.server.service.core.spring;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.mvp.server.service.core.AbstractCRUDService;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

public abstract class AbstractSpringCRUDService<T extends IDomainObject<?>> extends AbstractCRUDService<T> {

	@Override
	@Transactional
	public T persist(T entity) {
		return super.persist(entity);
	}

	@Override
	@Transactional
	public T merge(T entity) {
		return super.merge(entity);
	}

	@Override
	@Transactional
	public void remove(T entity) {
		super.remove(entity);
	}

	@Override
	@Transactional
	public PagedResult<List<T>> findAll(Page requestedPage) {
		return super.findAll(requestedPage);
	}

	@Override
	@Transactional
	public T findEntity(T entity) {
		return super.findEntity(entity);
	}	

}