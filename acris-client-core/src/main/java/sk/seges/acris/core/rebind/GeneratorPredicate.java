package sk.seges.acris.core.rebind;

/**
 * rule for generator
 * e.g.:
 *  <generate-with class="SomeGenerator">
 *		<when-type-assignable class="IGenerate"/>
 *	</generate-with>
 *	ePredict = EPredicts.ASSIGNABLE
 *	name = "class"
 *	value = "IGenerate"
 *
 */
public class GeneratorPredicate {

	public enum EPredicts {
		ASSIGNABLE, TYPEIS, PROPERTY
	}
	
	public static String CLASS = "class";
	
	private EPredicts ePredict;
	
	/**
	 * property name, or this.CLASS
	 */
	private String name;
	
	/**
	 * value of property, or name of class or interface
	 */
	private String value;

	public EPredicts getEPredict() {
		return ePredict;
	}

	public void setEPredict(EPredicts ePredicts) {
		this.ePredict = ePredicts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}