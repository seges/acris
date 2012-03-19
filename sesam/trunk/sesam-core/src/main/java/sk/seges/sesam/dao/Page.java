/**
 * 
 */
package sk.seges.sesam.dao;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * Supporting class for defining paging results retrieved by a service call.
 * 
 * @author ladislav.gazo
 */
public class Page implements Serializable {
	private static final long serialVersionUID = -3246966934600152455L;
	/** If somebody wants to fetch all results available, use this as page size. */
	public static final int ALL_RESULTS = 0;
	public static final Page ALL_RESULTS_PAGE = new Page(0, ALL_RESULTS);

	private int startIndex;
	private int pageSize;

	private List<SortInfo> sortables;
	private Criterion filterable;
	private List<String> projectables;
	private String projectableResult;
	
	/**
	 * Default constructor only because of serializable purposes. Use all
	 * appropriate setters to correctly initialize a page.
	 */
	public Page() {
	}

	/**
	 * Used for initial definition of required results. Minimal set of required
	 * parameters are provided.
	 * 
	 * @param startIndex
	 *            Start index of the first result matching page criteria.
	 * @param pageSize
	 *            Number of results that should be retrieved. Can differ from
	 *            number of results really fetched. This is only criteria.
	 */
	public Page(int startIndex, int pageSize) {
		this.startIndex = startIndex;
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * <b>Required</b> property of a page determining the first result in
	 * result collection.
	 * 
	 * @param startIndex
	 *            Index of a result in a collection of all records available.
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * <b>Required</b> property of a page determining number of results
	 * available after fetching from data source using a service call.
	 * 
	 * @param pageSize
	 *            Number of results that should be fetched.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SortInfo> getSortables() {
		return sortables;
	}
	
	public void setSortables(List<SortInfo> sortables) {
		this.sortables = sortables;
	}
	
	public void addSortable(SortInfo sortable) {
		if(sortables == null) {
			sortables = new LinkedList<SortInfo>();
		}
		sortables.add(sortable);
	}

	public Criterion getFilterable() {
		return filterable;
	}
	
	public Page setFilterable(Criterion filterable) {
		this.filterable = filterable;
		return this;
	}

	public List<String> getProjectables() {
		return projectables;
	}
	
	public void setProjectables(List<String> projectables) {
		this.projectables = projectables;
	}
	
	public void addProjectable(String projectable) {
		if(projectables == null) {
			projectables = new LinkedList<String>();
		}
		projectables.add(projectable);
	}

	/**
	 * Goes in pair with projectables to determine class name of retrieved and
	 * constrained objects.
	 * 
	 * NOTE: it is string because of GWT being not able to accept Class
	 * 
	 * @return A class name representing final results constrained by projectables.
	 */
	public String getProjectableResult() {
		return projectableResult;
	}

	public void setProjectableResult(String projectableResult) {
		this.projectableResult = projectableResult;
	}

	@Override
	public String toString() {
		return startIndex + " [" + pageSize + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pageSize;
		result = prime * result + startIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Page other = (Page) obj;
		if (pageSize != other.pageSize)
			return false;
		if (startIndex != other.startIndex)
			return false;
		return true;
	}
}
