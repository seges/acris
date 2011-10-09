package sk.seges.acris.json.client.facebook;

import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.JsonizerBuilder;
import sk.seges.acris.json.client.facebook.model.Company;
import sk.seges.acris.json.client.facebook.request.FacebookRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


public class ShowFacebookData implements EntryPoint {

	private static final String FACEBOOK_URL = "https://graph.facebook.com/facebook";
	private static final String GOOGLE_URL = "https://graph.facebook.com/google";
	
	@Override
	public void onModuleLoad() {
	
		Button buttonF = new Button("Facebook");
		RootPanel.get().add(buttonF);
		Button buttonG = new Button("Google");
		RootPanel.get().add(buttonG);
		
		RootPanel.get().add(new HTML("<br/><b style='font-size: 14px'>Keep in mind, that all data are fetched from Facebook using JSON and are displayed using GWT UIBinder on the browser!!! No iFrames just REST like Facebook webservices!<br/><br/>Choose your button to get the JSON working.</b>"));
		
		buttonF.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
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
		});

		buttonG.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new FacebookRequest().doRequest(GOOGLE_URL, new AsyncCallback<JSONObject>() {
					
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
		});

	}
	
	private CompanyPage companyPage;
	
	public void showCompany(Company company) {
		if (companyPage == null) {
			companyPage = new CompanyPage(company);
			RootPanel.get().add(companyPage);
		} else {
			companyPage.setCompany(company);
		}
	}
}