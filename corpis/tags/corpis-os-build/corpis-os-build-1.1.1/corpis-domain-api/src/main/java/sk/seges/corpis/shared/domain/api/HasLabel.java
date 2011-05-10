package sk.seges.corpis.shared.domain.api;


/**
 * Represents that object has label which is, in the most cases, displayed in the user interface to the user
 * 
 * @author psimun
 */
//@BeanWrapper
public interface HasLabel {

	String getLabel();

	void setLabel(String label);

}