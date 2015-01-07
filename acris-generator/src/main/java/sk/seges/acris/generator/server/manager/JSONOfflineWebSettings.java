package sk.seges.acris.generator.server.manager;

import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.server.manager.api.ParametersManager;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public class JSONOfflineWebSettings implements OfflineWebSettings {

	private ParametersManager parametersManager;

	public JSONOfflineWebSettings(WebSettingsData webSettings, ParametersManagerFactory parameterManagerFactory) {
		this.parametersManager = parameterManagerFactory.create(webSettings.getParameters());
	}

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        Object offlineModeParameter = parametersManager.getParameterValue(OfflineParameterType.OFFLINE_MODE);

        if (offlineModeParameter == null) {
            return null;
        }
        return OfflineClientWebParams.OfflineMode.valueOf(offlineModeParameter.toString());
    }

	@Override
	public boolean supportsAutodetectMode() {
		Boolean parameterValue = (Boolean) parametersManager.getParameterValue(OfflineParameterType.AUTODETECT_MODE);
		return (parameterValue != null && parameterValue);
	}

	@Override
	public boolean publishOnSaveEnabled() {
		Boolean parameterValue = (Boolean) parametersManager.getParameterValue(OfflineParameterType.PUBLISH_ON_SAVE_ENABLED);
		return (parameterValue != null && parameterValue);
	}
}
