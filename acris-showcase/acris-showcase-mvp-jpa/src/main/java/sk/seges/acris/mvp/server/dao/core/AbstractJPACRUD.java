package sk.seges.acris.mvp.server.dao.core;

import java.util.ArrayList;
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
		List<T> results = entityManager.createQuery("select FROM " + clazz.getName()).getResultList();
		List<T> copiedResults = new ArrayList<T>();
		for (T t: results) {
			copiedResults.add(t);
		}
		PagedResult<List<T>> pagedResult = new PagedResult<List<T>>();
		pagedResult.setResult(copiedResults);
		pagedResult.setPage(requestedPage);
		return pagedResult;
	}
}