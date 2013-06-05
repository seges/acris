package sk.seges.acris.security.shared.user_management.domain.api;

public enum OpenIDProvider {
	GOOGLE("https://www.google.com/accounts/o8/id"), YAHOO("http://me.yahoo.com"), AOL("https://www.aol.com"), 
	SEZNAM("http://www.seznam.cz"), MYOPENID("https://www.myopenid.com"), SCALEDO("http://scaledo.com/openid");
	
	private String identifier;
	
	private OpenIDProvider(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
