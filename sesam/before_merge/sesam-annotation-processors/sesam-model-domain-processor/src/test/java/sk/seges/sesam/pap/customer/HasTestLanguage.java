package sk.seges.sesam.pap.customer;


/**
 * Interface defines that object is language aware and holds the language settings.
 * 
 * @author psimun
 */
//@BeanWrapper
public interface HasTestLanguage {

	String getLanguage();

	void setLanguage(String language);
}