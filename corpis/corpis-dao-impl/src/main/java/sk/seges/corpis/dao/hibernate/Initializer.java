/**
 * 
 */
package sk.seges.corpis.dao.hibernate;

/**
 * Class used to help in things Hibernate is not able to do automatically.
 * 
 * @author eldzi
 */
public class Initializer {
	private InitializerHelper helper;

	private String sequenceName;
	private Integer initialValue;
	private Integer incrementSize;

	/**
	 * @param helper the helper to set
	 */
	public void setHelper(InitializerHelper helper) {
		this.helper = helper;
	}

	public void createSequence() {
		helper.createSequence(sequenceName, initialValue, incrementSize);
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public void setInitialValue(Integer initialValue) {
		this.initialValue = initialValue;
	}

	public void setIncrementSize(Integer incrementSize) {
		this.incrementSize = incrementSize;
	}
}
