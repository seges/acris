package sk.seges.acris.site.client.json.params;

import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;

public class WebParamsJSO extends BaseJSONModel implements WebParams {

	private static final long serialVersionUID = -728318142595408871L;

	public WebParamsJSO(JSONModel fromJson) {
		super(fromJson);
	}

	@Override
	public Boolean isPublishOnSaveEnabled() {
		return data.getBoolean(PUBLISH_ON_SAVE_ENABLED);
	}

	@Override
	public void setPublishOnSaveEnabled(boolean publishOnSaveEnabled) {
		data.set(PUBLISH_ON_SAVE_ENABLED, publishOnSaveEnabled);
	}

	@Override
	public Boolean isOfflineAutodetectMode() {
		return data.getBoolean(OFFLINE_AUTODETECT_MODE);
	}

	@Override
	public void setOfflineAutodetectMode(boolean mode) {
		data.set(OFFLINE_AUTODETECT_MODE, mode);
	}

	@Override
	public Boolean isProductCategorySingleSelect() {
		return data.getBoolean(PRODUCT_CATEGORY_SINGLE_SELECT);
	}

	@Override
	public void setProductCategorySingleSelect(boolean productCategorySingleSelect) {
		data.set(PRODUCT_CATEGORY_SINGLE_SELECT, productCategorySingleSelect);
	}

	@Override
	public String[] getOfflinePostProcessorInactive() {
		return data.getStringArray(OFFLINE_POST_PROCESSOR_INACTIVE);
	}

	@Override
	public void setOfflinePostProcessorInactive(String[] processors) {
		data.set(OFFLINE_POST_PROCESSOR_INACTIVE, processors);
	}

	@Override
	public String[] getOfflineIndexProcessorInactive() {
		return data.getStringArray(OFFLINE_INDEX_PROCESSOR_INACTIVE);
	}

	@Override
	public void setOfflineIndexProcessorInactive(String[] processors) {
		data.set(OFFLINE_INDEX_PROCESSOR_INACTIVE, processors);
	}
}
