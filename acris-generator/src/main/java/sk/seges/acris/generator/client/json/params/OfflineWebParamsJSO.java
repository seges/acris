package sk.seges.acris.generator.client.json.params;

import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class OfflineWebParamsJSO extends BaseJSONModel implements OfflineClientWebParams {

	private static final long serialVersionUID = -728318142595408871L;

    private OfflineWebParamsJSO() {}

    public OfflineWebParamsJSO(JSONModel fromJson) {
		super(fromJson);
	}

    @Override
	public Boolean isPublishOnSaveEnabled() {
		return data.getBoolean(OfflineParameterType.PUBLISH_ON_SAVE_ENABLED.getKey());
	}

	@Override
	public void setPublishOnSaveEnabled(boolean publishOnSaveEnabled) {
		data.set(OfflineParameterType.PUBLISH_ON_SAVE_ENABLED.getKey(), publishOnSaveEnabled);
	}

	@Override
	public Boolean supportsAutodetectMode() {
		return data.getBoolean(OfflineParameterType.AUTODETECT_MODE.getKey());
	}

    @Override
    public OfflineMode getOfflineMode() {
        String offlineMode = data.get(OfflineParameterType.OFFLINE_MODE.getKey());
        if (offlineMode == null) {
            return null;
        }
        return OfflineMode.valueOf(offlineMode);
    }

    @Override
    public void setOfflineMode(OfflineMode offlineMode) {
        data.set(OfflineParameterType.OFFLINE_MODE.getKey(), offlineMode.toString());
    }
}