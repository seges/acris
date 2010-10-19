package sk.seges.acris.domain.shared.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

/**
 * Interface defines that object is language aware and holds the language settings.
 * 
 * @author psimun
 */
@BeanWrapper
public interface HasLanguage {

	String getLanguage();

	void setLanguage(String language);
}