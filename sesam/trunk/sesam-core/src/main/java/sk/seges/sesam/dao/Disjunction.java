/**
 * 
 */
package sk.seges.sesam.dao;

/**
 * @author ladislav.gazo
 */
public class Disjunction extends Junction {
	private static final long serialVersionUID = -6937397351955413400L;
	
	protected Disjunction() {	}
	
	public Junction or(Criterion criterion) {
		return add(criterion);
	}

	@Override
	public String getOperation() {
		return "disjunction";
	}
}
