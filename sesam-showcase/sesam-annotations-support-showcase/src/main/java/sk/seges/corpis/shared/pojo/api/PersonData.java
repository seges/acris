package sk.seges.corpis.shared.pojo.api;

import sk.seges.corpis.platform.annotation.TransferObject;

@TransferObject
public interface PersonData {
	
	String getName();
 
	void setName(String name);
} 
