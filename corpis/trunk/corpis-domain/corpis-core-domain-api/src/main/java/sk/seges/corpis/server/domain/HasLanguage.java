package sk.seges.corpis.server.domain;


/**
 * Interface defines that object is language aware and holds the language settings.
 * 
 * @author psimun
 */
//@BeanWrapper
public interface HasLanguage {

	String getLanguage();

	void setLanguage(String language);
}