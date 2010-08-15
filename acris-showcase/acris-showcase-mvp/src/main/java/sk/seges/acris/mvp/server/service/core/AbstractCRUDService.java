package sk.seges.acris.mvp.server.service.core;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public T persist(T entity) {
		return ensureDao().persist(entity);
	}

	@Override
	@Transactional
	public T merge(T entity) {
		return ensureDao().merge(entity);
	}

	@Override
	@Transactional
	public void remove(T entity) {
		ensureDao().remove(entity);
	}

	@Override
	@Transactional
	public PagedResult<List<T>> findAll(Page requestedPage) {
		return ensureDao().findAll(requestedPage);
	}

	@Override
	@Transactional
	public T findEntity(T entity) {
		return ensureDao().findEntity(entity);
	}	
}