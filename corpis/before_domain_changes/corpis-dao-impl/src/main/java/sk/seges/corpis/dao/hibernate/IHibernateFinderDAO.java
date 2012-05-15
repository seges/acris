package sk.seges.corpis.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.sesam.dao.Page;

public interface IHibernateFinderDAO<T> {

	List<T> findByCriteria(DetachedCriteria criteria, Page page);

	List<T> findByCriteria(DetachedCriteria criteria, Page page, Set<String> existingAliases);

	Integer getCountByCriteria(DetachedCriteria criteria);

	Integer getCountByCriteria(DetachedCriteria criteria, Page requestedPage, Set<String> existingAliases);
}
