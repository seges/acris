package sk.seges.acris.recorder.client;

import sk.seges.acris.recorder.client.ui.RecorderUI;

import com.google.gwt.core.client.EntryPoint;

public class CursorShowcase implements EntryPoint {
	
//	private void prepareConversationUI() {
//        String userServiceURL = GWT.getModuleBaseURL() + "acris-service/userservice";
//        IUserServiceAsync userService = GWT.create(IUserService.class);
//        ServiceDefTarget userServiceEndPoint = (ServiceDefTarget)userService;
//        userServiceEndPoint.setServiceEntryPoint(userServiceURL);
//
//        userService.createAnonymousSession(new NotifyAsyncCallback<String>() {
//			
//			public void onSuccess(String sessionId) {
//		        ISessionProvider sessionProvider = GWT.create(ISessionProvider.class);
//		    	sessionProvider.setSessionId(sessionId);
//		        
//		    	ConversationMainPanelFactory theConversationMainPanelFactory = ConversationMainPanelFactory.getInstance();
//		        final GWTConversationMainPanel theConversationMainPanel = theConversationMainPanelFactory.getConversationMainPanelUIImplentation();
//
//		        new ConversationControl(theConversationMainPanel);
//		        RootPanel.get().add(theConversationMainPanel);
//			}
//			
//			public void onFailure(Throwable caught) {
//				GWT.log("Unable to create anonymous session", caught);
//			}
//		});
//	}
	
	public void onModuleLoad() {
		new RecorderUI().show();

/*		RootPanel.get().add(cursor);
		container.add(cursor);
		cursor.move(10, 700);
		cursor.move(50, 10);
		cursor.move(500, 350);
		cursor.move("button");
		tooltip.move(300, 10);
		cursor.clickAndWait("button");
		cursor.move(500, 350);*/
	}
}