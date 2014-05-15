package sk.seges.acris.site.client.json.params;

import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;

public class ImageSizeJSO extends BaseJSONModel implements ImageSize{

	private static final long serialVersionUID = 5681621155992540168L;
	
	
	public ImageSizeJSO(JSONModel data) {
		super(data);
	}
	
	public JSONModel getJSONModel() {
		return data;
	}
	
	@Override
	public Integer getWidth() {		
		return data.getInteger(WIDTH);
	}

	@Override
	public void setWidth(Integer width) {
		data.set(WIDTH, width);
	}

	@Override
	public Integer getHeight() {		
		return data.getInteger(HEIGHT);
	}

	@Override
	public void setHeight(Integer height) {
		data.set(HEIGHT, height);
	}

	@Override
	public String getType() {
		return data.get(TYPE);
	}

	@Override
	public void setType(String type) {
		data.set(TYPE, type);
	}

}
