package sk.seges.acris.site.shared.domain.api;


public enum MetaDataType {
	AUTHOR("Author", MetaContentValueType.SINGLE_TEXT_VALUE),
	COPYRIGHT("Copyright", MetaContentValueType.SINGLE_TEXT_VALUE),
	EXPIRES("Expires", MetaContentValueType.DATE_VALUE),
	ROBOTS("Robots", MetaContentValueType.MULTI_LIST_VALUE, ContentType.RobotsContentType.values()),
	GENERATOR("Generator", MetaContentValueType.SINGLE_TEXT_VALUE),
	REFRESH("Refresh", MetaContentValueType.SINGLE_TEXT_VALUE),
	GOOGLEBOT("Googlebot", MetaContentValueType.SINGLE_LIST_VALUE, ContentType.GoogleBotType.values()),
	GOOGLE_WEBMASTER("google-site-verification", MetaContentValueType.SINGLE_TEXT_VALUE);
	
	private MetaContentValueType contentValueType;
	private ContentType[] contentTypes;
	private String name;
	
	MetaDataType(String name, MetaContentValueType contentValueType) {
		this.name = name;
		this.contentValueType = contentValueType;
	}

	MetaDataType(String name, MetaContentValueType contentValueType, ContentType... contentTypes) {
		this(name, contentValueType);
		this.contentTypes = contentTypes;
	}

	public MetaContentValueType getContentValueType() {
		return contentValueType;
	}
	
	public ContentType[] getContentTypes() {
		return contentTypes;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}