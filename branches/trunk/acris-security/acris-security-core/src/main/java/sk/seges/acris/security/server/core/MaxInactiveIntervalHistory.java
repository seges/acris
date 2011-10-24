package sk.seges.acris.security.server.core;

public class MaxInactiveIntervalHistory {
	
	private int actual;
	private int previous;
	private int old;
	private String sessionId;
	
	public MaxInactiveIntervalHistory(String sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public void putActual(Integer actual) {
		old = previous;
		previous = this.actual;
		this.actual = actual;
		if (old == 0) {
			old = actual;
		}
		if (previous == 0) {
			previous = actual;
		}
	}
	
	public void revert() {
		actual = previous;
		previous = old;
	}

	public int getPrevious() {
		return previous;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MaxInactiveIntervalHistory [actual=" + actual + ", previous=" + previous + ", old=" + old
				+ ", sessionId=" + sessionId + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MaxInactiveIntervalHistory other = (MaxInactiveIntervalHistory) obj;
		if (sessionId == null) {
			if (other.sessionId != null) {
				return false;
			}
		} else if (!sessionId.equals(other.sessionId)) {
			return false;
		}
		return true;
	}
	
	
}
