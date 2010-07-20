package sk.seges.acris.json.client.facebook;

import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.JsonizerBuilder;
import sk.seges.acris.json.client.facebook.model.Company;
import sk.seges.acris.json.client.facebook.request.FacebookRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;


public class ShowFacebookData implements EntryPoint {

	private static final String FACEBOOK_URL = "https://graph.facebook.com/google";
	
	@Override
	public void onModuleLoad() {
		
		new FacebookRequest().doRequest(FACEBOOK_URL, new AsyncCallback<JSONObject>() {
			
			@Override
			public void onSuccess(JSONObject arg0) {
				JsonizerBuilder jsonizerBuilder = new JsonizerBuilder();
				IJsonizer jsonnizer = jsonizerBuilder.create();
				showCompany(jsonnizer.fromJson(arg0, Company.class));
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Unable to obtain data from facebook", arg0);
				Window.alert("Unable to obtain data from facebook (reason: " + arg0.toString() + ")");
			}
		});
	}
	
	public void showCompany(Company company) {
		RootPanel.get().add(new CompanyPage(company));
	}
}