package sk.seges.acris.mvp.server.dao.core;

import java.util.List;

import sk.seges.corpis.dao.AbstractJPADAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;


public abstract class AbstractJPACRUD<T extends IDomainObject<?>> extends AbstractJPADAO<T> {

	protected AbstractJPACRUD(Class<T> clazz) {
		super(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagedResult<List<T>> findAll(Page requestedPage) {
		List<T> results = entityManager.createQuery("FROM " + clazz.getName()).getResultList();
		PagedResult<List<T>> pagedResult = new PagedResult<List<T>>();
		pagedResult.setResult(results);
		pagedResult.setPage(requestedPage);
		return pagedResult;
	}
}