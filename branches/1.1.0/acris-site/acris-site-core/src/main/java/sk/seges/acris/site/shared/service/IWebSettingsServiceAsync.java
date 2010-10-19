package sk.seges.acris.site.shared.service;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IWebSettingsServiceAsync {

	void getWebSettings(String webId, AsyncCallback<WebSettingsData> callback);

	void saveWebSettings(WebSettingsData webSettingsData, AsyncCallback<Void> callback);
}