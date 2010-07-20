package sk.seges.acris.json.client.request;

import java.util.HashMap;


public class JSONRequest {

	protected static HashMap<Integer, Object> callbacks = new HashMap<Integer, Object>();

	protected static int curIndex = 0;

	public native void setup(JSONRequestHandler obj, String callback) /*-{
		window[callback] = function(data) {
		  obj.@sk.seges.acris.json.client.request.JSONRequestHandler::onRequestComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
		}
	}-*/;

	public String reserveCallback() {
		while (true) {
			if (!callbacks.containsKey(new Integer(curIndex))) {
				callbacks.put(new Integer(curIndex), null);
				return "__gwt_callback" + curIndex++;
			}
		}
	}

	public void get(String url, JSONRequestHandler handler) {
		String callbackName = reserveCallback();
		setup(handler, callbackName);
		addScript(callbackName, url + "&callback=" + callbackName);
	}

	public native void addScript(String uniqueId, String url) /*-{
		var elem = document.createElement("script");
		elem.setAttribute("language", "JavaScript");
		elem.setAttribute("src", url);
		document.getElementsByTagName("body")[0].appendChild(elem);
	}-*/;
}