package sk.seges.acris.site.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface ImageSize extends ContentParameters{

	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String TYPE = "type";
	
	Integer getWidth();
	void setWidth(Integer width);
	
	Integer getHeight();
	void setHeight(Integer height);
	
	String getType();
	void setType(String type);
}
