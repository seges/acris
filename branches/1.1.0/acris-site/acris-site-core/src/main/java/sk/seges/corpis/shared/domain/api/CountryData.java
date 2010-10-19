package sk.seges.corpis.shared.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.domain.shared.domain.api.HasLabel;
import sk.seges.acris.domain.shared.domain.api.HasLanguage;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * Represents specific country in the world with the necessary information, like domain, language, name, etc.
 * 
 * @author psimun
 */
@BeanWrapper
public interface CountryData extends IMutableDomainObject<Integer>, HasLanguage, HasLabel {

	String getDomain();

	void setDomain(String domain);

	Boolean isEuropeanUnion();

	void setEuropeanUnion(Boolean europeanUnion);

	String getCountry();

	void setCountry(String country);
}