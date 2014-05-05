package sk.seges.acris.core.rebind;

import java.util.List;

/**
 * group of rules for GWT generators
 *
 */
public class GenPredicGroup {
	/**
	 * ALL=every rule must be true
	 * ANY=at least one must be true
	 * NONE=every rule must be false
	 * @author MPsenkova
	 *
	 */
	public static enum EOperator {
		ALL, ANY, NONE
	}	
	
	/**
	 * list of groups
	 */
	private List<GenPredicGroup> myGroups = null;
	
	/**
	 * list of rules
	 */
	private List<GeneratorPredicate> generatorPredicts = null;
	
	/**
	 * operator
	 */
	private EOperator operator;

	public List<GeneratorPredicate> getGeneratorPredicts() {
		return generatorPredicts;
	}

	public void setGeneratorPredicts(List<GeneratorPredicate> generatorPredicts) {
		this.generatorPredicts = generatorPredicts;
	}

	public EOperator getOperator() {
		return operator;
	}

	public void setOperator(EOperator operator) {
		this.operator = operator;
	}

	public List<GenPredicGroup> getMyGroups() {
		return myGroups;
	}

	public void setMyGroups(List<GenPredicGroup> myGroups) {
		this.myGroups = myGroups;
	}	
}