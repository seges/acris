package sk.seges.acris.site.shared.domain.api;

public interface ContentType {
	String name();
	
	public static enum RobotsContentType implements ContentType {
		ALL, NONE, NOINDEX, INDEX, NOFOLLOW, FOLLOW;
	}
	
	public static enum GoogleBotType implements ContentType {
		GOOGLEBOT;
	}
}