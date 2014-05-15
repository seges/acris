package sk.seges.acris.site.client.json.params;

public class ImageSizeBean implements ImageSize{

	private static final long serialVersionUID = -861239727964682776L;
	private Integer width;
	private Integer height;
	private String type;
	
	@Override
	public Integer getWidth() {
		return width;
	}

	@Override
	public void setWidth(Integer width) {
		this.width = width;		
	}

	@Override
	public Integer getHeight() {
		return height;
	}

	@Override
	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String getType() {		
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	
}
