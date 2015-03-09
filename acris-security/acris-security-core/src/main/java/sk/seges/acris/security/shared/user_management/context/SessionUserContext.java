package sk.seges.acris.security.shared.user_management.context;


public class SessionUserContext extends CommonUserContext {
	private static final long serialVersionUID = -1631599046237774825L;
	
	public SessionUserContext() { }
	
	public SessionUserContext(String webId, String locale) {
		super(webId, locale);
	}
}
