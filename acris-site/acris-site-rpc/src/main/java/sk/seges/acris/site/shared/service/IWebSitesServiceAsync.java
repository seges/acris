package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.shared.model.dto.WebSitesDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IWebSitesServiceAsync {

	void getWebSites(String webId, AsyncCallback<List<WebSitesDTO>> callback);
	
	void addWebSite(WebSitesDTO webSite, AsyncCallback<Void> callback);
	
	void getPrimaryWebSite(String webId, String locale, AsyncCallback<WebSitesDTO> webSite);
	
	void deleteWebSites(String webId, AsyncCallback<Void> callback);
}
