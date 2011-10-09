package sk.seges.sesam.dao;

import java.io.Serializable;

/**
 * Holds information about a column and a type of sorting
 * (ascending/descending). Name of the column usually matches domain
 * object's field.
 * 
 * @author eldzi
 */
public class SortInfo implements Serializable {
	private static final long serialVersionUID = -3674913932641601223L;
	
	private boolean ascending;
	private String column;
	
	public SortInfo() {}

	public SortInfo(boolean ascending, String column) {
		this.ascending = ascending;
		this.column = column;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ascending ? 1231 : 1237);
		result = prime * result
				+ ((column == null) ? 0 : column.hashCode());
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
		SortInfo other = (SortInfo) obj;
		if (ascending != other.ascending)
			return false;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[ col = " + column + ", asc = " + ascending + " ]";
	}
}