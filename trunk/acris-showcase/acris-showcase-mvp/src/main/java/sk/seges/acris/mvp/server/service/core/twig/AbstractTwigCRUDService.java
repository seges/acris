package sk.seges.acris.mvp.server.service.core.twig;

import java.util.List;

import sk.seges.acris.mvp.server.service.core.AbstractCRUDService;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.domain.IDomainObject;

import com.google.appengine.api.datastore.Transaction;
import com.vercer.engine.persist.ObjectDatastore;

public abstract class AbstractTwigCRUDService<T extends IDomainObject<?>> extends AbstractCRUDService<T> {

	private ObjectDatastore datastore;

	protected AbstractTwigCRUDService(ObjectDatastore datastore) {
		this.datastore = datastore;
	}

	@Override
	public T persist(T entity) {
		Transaction tx = datastore.beginTransaction();

		try {
			T t = super.persist(entity);
			tx.commit();
			return t;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public T merge(T entity) {
		Transaction tx = datastore.beginTransaction();

		try {
			T t = super.merge(entity);
			tx.commit();
			return t;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public void remove(T entity) {
		Transaction tx = datastore.beginTransaction();

		try {
			super.remove(entity);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public PagedResult<List<T>> findAll(Page requestedPage) {
		Transaction tx = datastore.beginTransaction();

		try {
			PagedResult<List<T>> t = super.findAll(requestedPage);
			tx.commit();
			return t;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@Override
	public T findEntity(T entity) {
		Transaction tx = datastore.beginTransaction();

		try {
			T t = super.findEntity(entity);
			tx.commit();
			return t;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}
}