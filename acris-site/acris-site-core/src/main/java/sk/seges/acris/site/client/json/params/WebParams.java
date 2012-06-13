package sk.seges.acris.site.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface WebParams extends ContentParameters {

	public static final String OFFLINE_POST_PROCESSOR_INACTIVE = "offlinePostProcessorInactive";
	public static final String OFFLINE_INDEX_PROCESSOR_INACTIVE = "offlineIndexProcessorInactive";
	public static final String OFFLINE_AUTODETECT_MODE = "offlineAutodetectMode";
	public static final String PUBLISH_ON_SAVE_ENABLED = "publishOnSaveEnabled";
	public static final String PRODUCT_CATEGORY_SINGLE_SELECT = "productCategorySingleSelect";

	String[] getOfflinePostProcessorInactive();

	void setOfflinePostProcessorInactive(String[] processors);

	String[] getOfflineIndexProcessorInactive();

	void setOfflineIndexProcessorInactive(String[] processors);

	Boolean isOfflineAutodetectMode();

	void setOfflineAutodetectMode(boolean mode);

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

	Boolean isProductCategorySingleSelect();

	void setProductCategorySingleSelect(boolean productCategorySingleSelect);
}
