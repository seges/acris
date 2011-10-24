package sk.seges.acris.mvp.shared.model.api;

import sk.seges.sesam.domain.IMutableDomainObject;


public interface GroupData extends IMutableDomainObject<Long> {

	String getName();
	
	void setName(String name);
	
	String getDescription();
	
	void setDescription(String description);
}