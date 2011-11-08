package sk.seges.acris.site.client.json.params;

public interface WebParams extends ContentParameters {

	public static final String OFFLINE_POST_PROCESSOR_INACTIVE = "offlinePostProcessorInactive";
	public static final String OFFLINE_INDEX_PROCESSOR_INACTIVE = "offlineIndexProcessorInactive";
	public static final String OFFLINE_AUTODETECT_MODE = "offlineAutodetectMode";
	public static final String PUBLISH_ON_SAVE_ENABLED = "publishOnSaveEnabled";

	String[] getOfflinePostProcessorInactive();

	void setOfflinePostProcessorInactive(String[] processors);

	String[] getOfflineIndexProcessorInactive();

	void setOfflineIndexProcessorInactive(String[] processors);

	boolean isOfflineAutodetectMode();

	void setOfflineAutodetectMode(boolean mode);

	boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);
}
