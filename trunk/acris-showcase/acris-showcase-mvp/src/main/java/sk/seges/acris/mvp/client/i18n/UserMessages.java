package sk.seges.acris.mvp.client.i18n;

import com.google.gwt.i18n.client.Messages;


public interface UserMessages extends Messages {

	String failure(String error);
	
	String userName();
	
	String identifier();

	String password();
	
	String newUser();
}