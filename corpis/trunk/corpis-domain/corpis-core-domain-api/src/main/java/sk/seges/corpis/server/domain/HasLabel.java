package sk.seges.corpis.server.domain;


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