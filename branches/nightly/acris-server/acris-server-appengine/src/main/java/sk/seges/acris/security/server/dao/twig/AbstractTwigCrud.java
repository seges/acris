package sk.seges.acris.security.server.dao.twig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;
import sk.seges.sesam.dao.SortInfo;
import sk.seges.sesam.domain.IMutableDomainObject;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.ObjectDatastore;


public abstract class AbstractTwigCrud<T extends IMutableDomainObject<Long>> implements ICrudDAO<T> {

	private ObjectDatastore datastore;
	private Class<? extends T> clazz;
	
	protected AbstractTwigCrud(ObjectDatastore datastore, Class<? extends T> clazz) {
		this.datastore = datastore;
		this.clazz = clazz;
	}

	public T findUnique(Page requestedPage) {
		//reseting the page to unlimited page
		requestedPage.setStartIndex(0);
		requestedPage.setPageSize(Page.ALL_RESULTS);

		List<T> result =  findAll(requestedPage).getResult();
		if (result == null || result.size() == 0) {
			throw new RuntimeException("Unable to obtain unique result. No results found!");
		}
		
		if (result.size() > 1) {
			throw new RuntimeException("Unable to obtain unique result. More results found!");
		}
		
		return result.get(0);
	}
	
	@Override
	public PagedResult<List<T>> findAll(Page requestedPage) {
		RootFindCommand<? extends T> findCommand = datastore.find().type(clazz);
		applyPageSize(findCommand, requestedPage);
		applySortCriteria(findCommand, requestedPage);
		applyProjections(findCommand, requestedPage);
		applyFilters(findCommand, requestedPage);

		int totalCount = findCommand.countResultsNow();
		
		Iterator<? extends T> result = findCommand.returnResultsNow();
		
		List<T> resultList = new ArrayList<T>();
		
		while (result.hasNext()) {
			resultList.add(result.next());
		}
		
		return new PagedResult<List<T>>(requestedPage, resultList, totalCount);
	}

	private RootFindCommand<? extends T> applyProjections(RootFindCommand<? extends T> findCommand, Page page) {
		//TODO
		return findCommand;
	}
	
	private RootFindCommand<? extends T> applyFilters(RootFindCommand<? extends T> findCommand, Page page) {
		Criterion criterion = page.getFilterable();
		
		if (criterion instanceof SimpleExpression) {
			SimpleExpression<?> expression = (SimpleExpression<?>)criterion;
			findCommand.addFilter(expression.getProperty(), convertOperatorToAppengine(expression.getOperation()), expression.getValue());

		}
		return findCommand;
	}
	
	private FilterOperator convertOperatorToAppengine(String operator) {
		if (operator.equals(Filter.NE)) return FilterOperator.NOT_EQUAL;
		if (operator.equals(Filter.EQ)) return FilterOperator.EQUAL;
		if (operator.equals(Filter.LE)) return FilterOperator.LESS_THAN_OR_EQUAL;
		if (operator.equals(Filter.LT)) return FilterOperator.LESS_THAN;
		if (operator.equals(Filter.GE)) return FilterOperator.GREATER_THAN_OR_EQUAL;
		if (operator.equals(Filter.GT)) return FilterOperator.GREATER_THAN;

		//TODO
		//Filter.ILIKE, Filter.BETWEEN, Filter.LIKE are not currently supported
		
		throw new RuntimeException("Unsupported " + (operator) + " criterion operator!");
	}
	
	private RootFindCommand<? extends T> applySortCriteria(RootFindCommand<? extends T> findCommand, Page page) {
		if (page.getSortables() == null) {
			return findCommand;
		}
		
		for (SortInfo sortInfo : page.getSortables()) {
			findCommand.addSort(sortInfo.getColumn(), sortInfo.isAscending() ? SortDirection.ASCENDING : SortDirection.DESCENDING);
		}
		
		return findCommand;
	}
	
	private RootFindCommand<? extends T> applyPageSize(RootFindCommand<? extends T> findCommand, Page page) {
		if (page.getPageSize() != Page.ALL_RESULTS) {
			findCommand.startFrom(page.getStartIndex());
			findCommand.maximumResults(page.getPageSize());
		}
		return findCommand;
	}
	
	@Override
	public T findEntity(T entity) {
		return datastore.load(clazz, entity.getId());
	}

	@Override
	public T persist(T entity) {
		Key key = datastore.store(entity);
		entity.setId(key.getId());
		return entity;
//		Key key = datastore.store()
//		  .instance(entity)
//		  .ensureUniqueKey()
//		  .returnKeyNow();
//		entity.setId(key.getId());
//		return entity;
	}

	@Override
	public T merge(T entity) {
		datastore.associate(entity);
		datastore.update(entity);
		return entity;
	}

	@Override
	public void remove(T entity) {
		datastore.associate(entity);
		datastore.delete(entity);
	}

}
