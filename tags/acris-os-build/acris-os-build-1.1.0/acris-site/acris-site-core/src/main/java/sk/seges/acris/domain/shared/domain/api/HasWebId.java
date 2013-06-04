package sk.seges.acris.domain.shared.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;

@BeanWrapper
public interface HasWebId {

	String getWebId();
	
	void setWebId(String webId);
}