package sk.seges.sesam.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ladislav.gazo
 */
public abstract class Junction implements Criterion {
	private static final long serialVersionUID = 6351399300707045961L;
	
	private List<Criterion> junctions = new ArrayList<Criterion>();
	
	protected Junction() {}
	
	public Junction add(Criterion criterion) {
		junctions.add(criterion);
		return this;
	}
	
	public List<Criterion> getJunctions() {
		return junctions;
	}
}
