package sk.seges.acris.mvp.server.service.core;

import java.util.List;

import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

public abstract class AbstractCRUDService<T extends IDomainObject<?>> implements ICRUDService<T> {

	private ICrudDAO<T> crudDao;

	protected abstract ICrudDAO<T> getDao();

	protected ICrudDAO<T> ensureDao() {
		if (crudDao == null) {
			crudDao = getDao();
		}

		return crudDao;
	}

	@Override
	public T persist(T entity) {
		return ensureDao().persist(entity);
	}

	@Override
	public T merge(T entity) {
		return ensureDao().merge(entity);
	}

	@Override
	public void remove(T entity) {
		ensureDao().remove(entity);
	}

	@Override
	public PagedResult<List<T>> findAll(Page requestedPage) {
		return ensureDao().findAll(requestedPage);
	}

	@Override
	public T findEntity(T entity) {
		return ensureDao().findEntity(entity);
	}
}