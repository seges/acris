/**
 * 
 */
package sk.seges.sesam.dao;

/**
 * @author eldzi
 */
public class NotExpression implements Criterion {
	private static final long serialVersionUID = -6011585536168355602L;

	private Criterion criterion;
	
	public NotExpression() {}
	
	public NotExpression(Criterion criterion) {
		this.criterion = criterion;
	}
	
	/* (non-Javadoc)
	 * @see sk.seges.sesam.dao.Criterion#getOperation()
	 */
	@Override
	public String getOperation() {
		return "not";
	}

	public Criterion getCriterion() {
		return criterion;
	}
	
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}
}
