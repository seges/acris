/**
 * 
 */
package sk.seges.sesam.pap.customer;

import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * Represents specific country in the world with the necessary information, like domain, language, name, etc.
 * 
 * @author psimun
 */
//@BeanWrapper
public interface TestCountryData<K> extends IMutableDomainObject<K>, HasTestLanguage, HasTestLabel {
	public static final String ID = "id";
	public static final String LABEL = "label";
	public static final String COUNTRY = "country";
	public static final String DOMAIN = "domain";
	public static final String EUROPEAN_UNION = "europeanUnion";
	public static final String LANG = "language";
	
	String getLabel();
	void setLabel(String label);

	Boolean getEuropeanUnion();
	void setEuropeanUnion(Boolean europeanUnion);

	String getDomain();
	void setDomain(String domain);

	String getCountry();
	void setCountry(String country);

	String getLanguage();
	void setLanguage(String language);

}
