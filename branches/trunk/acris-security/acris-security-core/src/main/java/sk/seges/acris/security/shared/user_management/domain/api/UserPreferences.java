package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

@BeanWrapper
public interface UserPreferences extends Serializable {

	String getLocale();

	void setLocale(String locale);
}
