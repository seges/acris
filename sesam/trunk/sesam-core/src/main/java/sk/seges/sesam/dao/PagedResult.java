/**
 * 
 */
package sk.seges.sesam.dao;

import java.io.Serializable;

/**
 * Class representing paged results fetched from a data source using a service
 * call. Page used for criteria definition is part of this class as well as
 * returning results. Usually results are in form of a collection.
 * 
 * @author ladislav.gazo
 */
public class PagedResult<T> implements Serializable {
	private static final long serialVersionUID = 7913070730470043908L;

	/** Page definition for returned results. */
	private Page page;
	/** Object of returned results. Usually it is a collection. */
	private T result;
	/**
	 * Total number of results in the moment that can be provided by a data
	 * source.
	 */
	private int totalResultCount;

	/**
	 * Default constructor only because of serializable purposes. Use all
	 * appropriate setters to correctly initialize a paged result.
	 */
	public PagedResult() {
	}

	/**
	 * Constructor simplifying work with paged results with minimum required
	 * parameters provided.
	 * 
	 * @param page
	 *            Page defining criteria for returned results.
	 * @param result
	 *            Results meeting the page criteria.
	 * @param totalResultCount
	 *            Total number of results in the moment that can be provided by
	 *            a data source.
	 */
	public PagedResult(Page page, T result, int totalResultCount) {
		this.page = page;
		this.result = result;
		this.totalResultCount = totalResultCount;
	}

	public Page getPage() {
		return page;
	}

	public T getResult() {
		return result;
	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setResult(T result) {
		this.result = result;
	}

	/**
	 * Set maximum available number of results in data source.
	 * 
	 * @param resultCount
	 *            Total number of results in the moment that can be provided by
	 *            a data source.
	 */
	public void setTotalResultCount(int resultCount) {
		this.totalResultCount = resultCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + totalResultCount;
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PagedResult<T> other = (PagedResult<T>) obj;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (totalResultCount != other.totalResultCount)
			return false;
		return true;
	}
}
