package sk.seges.corpis.server.domain;


//@BeanWrapper
public interface HasWebId {
	
	public static final String WEB_ID = "webId";
	
	String getWebId();

	void setWebId(String webId);
}