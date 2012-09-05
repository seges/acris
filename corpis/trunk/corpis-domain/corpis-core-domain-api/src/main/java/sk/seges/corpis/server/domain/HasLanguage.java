package sk.seges.corpis.server.domain;


/**
 * Interface defines that object is language aware and holds the language settings.
 * 
 * @author psimun
 */
//@BeanWrapper
public interface HasLanguage {

	public static final String LANGUAGE = "language";
	
	String getLanguage();

	void setLanguage(String language);
}