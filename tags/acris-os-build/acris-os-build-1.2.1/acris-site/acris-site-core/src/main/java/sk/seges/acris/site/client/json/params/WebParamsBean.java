package sk.seges.acris.site.client.json.params;

public class WebParamsBean implements WebParams {

	private static final long serialVersionUID = -728318142595408871L;

	private boolean publishOnSaveEnabled = false;
	private boolean offlineAutodetectMode = false;

	private String[] offlinePostProcessorInactive;
	private String[] offlineIndexProcessorInactive;

	@Override
	public boolean isPublishOnSaveEnabled() {
		return publishOnSaveEnabled;
	}

	@Override
	public void setPublishOnSaveEnabled(boolean publishOnSaveEnabled) {
		this.publishOnSaveEnabled = publishOnSaveEnabled;
	}

	@Override
	public boolean isOfflineAutodetectMode() {
		return offlineAutodetectMode;
	}

	@Override
	public void setOfflineAutodetectMode(boolean mode) {
		this.offlineAutodetectMode = mode;
	}

	@Override
	public String[] getOfflinePostProcessorInactive() {
		return this.offlinePostProcessorInactive;
	}

	@Override
	public void setOfflinePostProcessorInactive(String[] processors) {
		this.offlinePostProcessorInactive = processors;
	}

	@Override
	public String[] getOfflineIndexProcessorInactive() {
		return this.offlineIndexProcessorInactive;
	}

	@Override
	public void setOfflineIndexProcessorInactive(String[] processors) {
		this.offlineIndexProcessorInactive = processors;
	}
}
