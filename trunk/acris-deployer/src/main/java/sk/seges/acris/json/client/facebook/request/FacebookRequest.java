package sk.seges.acris.json.client.facebook.request;

import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.JsonizerBuilder;
import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.acris.json.client.request.JSONRequest;
import sk.seges.acris.json.client.request.JSONRequestHandler;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FacebookRequest extends JSONRequest {

	@JsonObject
	public static class JSONError extends Throwable {

		private static final long serialVersionUID = 2095950996148006801L;

		@Field
		private String type;

		@Field
		private String message;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		@Override
		public String toString() {
			return message;
		}
	}

	public void doRequest(String url, final AsyncCallback<JSONObject> callback) {
		get(url, new JSONRequestHandler() {

			@Override
			public void onRequestComplete(JSONObject json) {
				JSONValue errorValue = json.get("error");
				if (errorValue != null) {
					JsonizerBuilder jsonizerBuilder = new JsonizerBuilder();
					
					IJsonizer jsonnizer = jsonizerBuilder.create();
					JSONError error = jsonnizer.fromJson(errorValue, JSONError.class);

					callback.onFailure(error);
				} else {
					callback.onSuccess(json);
				}
			}
		});
	}
}