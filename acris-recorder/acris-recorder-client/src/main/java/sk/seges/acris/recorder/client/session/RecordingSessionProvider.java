package sk.seges.acris.recorder.client.session;

import com.google.gwt.dom.client.Document;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.recorder.shared.params.RecordingSessionDetailParams;
import sk.seges.acris.site.client.json.BaseJSONModel;
import sk.seges.acris.site.client.json.JSONModel;

public class RecordingSessionProvider {

	public static class IpInfoJSO extends BaseJSONModel {

		protected IpInfoJSO() {}

		public IpInfoJSO(String json) {
			super(JSONModel.fromJson(json));
		}

		public String getIp() {
			return data.get("ip");
		}

		public String getHostName() {
			return data.get("hostname");
		}

		public String getLocation() {
			return data.get("loc");
		}

		public String getOrganisation() {
			return data.get("org");
		}

		public  String getCity() {
			return data.get("city");
		}

		public String getRegion() {
			return data.get("region");
		}

		public String getCountry() {
			return data.get("country");
		}

		public String getPhone() {
			return data.get("phone");
		}
	}

	public static void getSession(final AsyncCallback<RecordingSessionDetailParams> callback) {

		final RecordingSessionDetailParamsJSO sessionDetail = new RecordingSessionDetailParamsJSO();

		sessionDetail.setLanguage(getLanguage());
		sessionDetail.setAntialiasingFonts(isFontsAntialiased());
		sessionDetail.setBrowserHeight(getScreenHeight());
		sessionDetail.setBrowserWidth(getScreenWidth());

		sessionDetail.setBrowserName(getBrowserName());
		sessionDetail.setBrowserVersion(getBrowserVersion());

		sessionDetail.setColorDepth(getColorDepth());
		sessionDetail.setHostName(Window.Location.getHostName());
		sessionDetail.setInitialUrl(Window.Location.getHref());
		sessionDetail.setJavaEnabled(Window.Navigator.isJavaEnabled());
		sessionDetail.setOsName(getOsName());
		sessionDetail.setReferrer(Document.get().getReferrer());
		sessionDetail.setUserAgent(Window.Navigator.getUserAgent());

		getIpInfo(new AsyncCallback<IpInfoJSO>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(IpInfoJSO result) {
				sessionDetail.setIpAddress(result.getIp());
				sessionDetail.setCity(result.getCity());
				sessionDetail.setCountry(result.getRegion());
				sessionDetail.setState(result.getCountry());

				callback.onSuccess(sessionDetail);
			}
		});
	}

	private static final native String getOsName() /*-{
		var osName="Unknown OS";
		if (navigator.appVersion.indexOf("Win")!=-1) osName="Windows";
		if (navigator.appVersion.indexOf("Mac")!=-1) osName="MacOS";
		if (navigator.appVersion.indexOf("X11")!=-1) osName="UNIX";
		if (navigator.appVersion.indexOf("Linux")!=-1) osName="Linux";

		return osName;
    }-*/;

	private static final native int getColorDepth() /*-{
        return screen.colorDepth;
    }-*/;

	private static final void getIpInfo(final AsyncCallback<IpInfoJSO> callback) {

		String url = "http://ipinfo.io/json";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						// Process the response in response.getText()
						String jsonResponse = response.getText();
						callback.onSuccess(new IpInfoJSO(jsonResponse));
					} else {
						callback.onFailure(new RuntimeException("Unable to connect to the ipinfo.io - status code: " + response.getStatusCode()));
						// Handle the error.  Can get the status text from response.getStatusText()
					}
				}
			});
		} catch (RequestException e) {
			callback.onFailure(e);
		}
	}

	private static final native String getLanguage() /*-{
        return window.navigator.systemLanguage || window.navigator.language;
    }-*/;

	private static final native String getBrowserName() /*-{
        return navigator.appName;
    }-*/;

	private static final native boolean isFontsAntialiased() /*-{
		// IE has screen.fontSmoothingEnabled - sweet!
		if (typeof(screen.fontSmoothingEnabled) != "undefined") {
			return screen.fontSmoothingEnabled;
		}

		try {
			// Create a 35x35 Canvas block.
			var canvasNode = $doc.createElement("canvas");
			canvasNode.width = "35";
			canvasNode.height = "35"

			// We must put this node into the body, otherwise
			// Safari Windows does not report correctly.
			canvasNode.style.display = "none";
            $doc.body.appendChild(canvasNode);
			var ctx = canvasNode.getContext("2d");

			// draw a black letter "O", 32px Arial.
			ctx.textBaseline = "top";
			ctx.font = "32px Arial";
			ctx.fillStyle = "black";
			ctx.strokeStyle = "black";

			ctx.fillText("O", 0, 0);

			// start at (8,1) and search the canvas from left to right,
			// top to bottom to see if we can find a non-black pixel.  If
			// so we return true.
			for (var j = 8; j <= 32; j++) {
				for (var i = 1; i <= 32; i++) {

					var imageData = ctx.getImageData(i, j, 1, 1).data;
					var alpha = imageData[3];

					if (alpha != 255 && alpha != 0) {
						return true; // font-smoothing must be on.
					}
				}

			}

			// didn't find any non-black pixels - return false.
			return false;
		}
		catch (ex) {
			// Something went wrong (for example, Opera cannot use the
			// canvas fillText() method.  Return null (unknown).
			return null;
		}
    }-*/;

	private static final native int getScreenHeight() /*-{

        if( typeof( $wnd.innerHeight ) == 'number' ) {
            return $wnd.innerHeight;
        }

		if( $doc.documentElement && ( $doc.documentElement.clientHeight ) ) {
            //IE 6+ in 'standards compliant mode'
            return $doc.documentElement.clientHeight;
        }

		if( $doc.body && ( $doc.body.clientHeight ) ) {
            //IE 4 compatible
            return $doc.body.clientHeight;
        }

		return 0;
    }-*/;

	private static final native int getScreenWidth() /*-{

        if( typeof( $wnd.innerWidth ) == 'number' ) {
            return $wnd.innerWidth;
        }

        if( $doc.documentElement && ( $doc.documentElement.clientWidth ) ) {
            //IE 6+ in 'standards compliant mode'
            return $doc.documentElement.clientWidth;
        }

        if( $doc.body && ( $doc.body.clientWidth ) ) {
            //IE 4 compatible
            return $doc.body.clientWidth;
        }

        return 0;
    }-*/;

	private static final native String getBrowserVersion() /*-{
        var nAgt = navigator.userAgent;
		var fullVersion  = ''+parseFloat(navigator.appVersion);
        var verOffset, ix;

		// In Opera, the true version is after "Opera" or after "Version"
        if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
            fullVersion = nAgt.substring(verOffset+6);
            if ((verOffset=nAgt.indexOf("Version"))!=-1)
                fullVersion = nAgt.substring(verOffset+8);
        }

		// In MSIE, the true version is after "MSIE" in userAgent
        else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
            fullVersion = nAgt.substring(verOffset+5);
        }
		// In Chrome, the true version is after "Chrome"
        else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
            fullVersion = nAgt.substring(verOffset+7);
        }
		// In Safari, the true version is after "Safari" or after "Version"
        else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
            fullVersion = nAgt.substring(verOffset+7);
            if ((verOffset=nAgt.indexOf("Version"))!=-1)
                fullVersion = nAgt.substring(verOffset+8);
        }
		// In Firefox, the true version is after "Firefox"
        else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
            fullVersion = nAgt.substring(verOffset+8);
        }
		// In most other browsers, "name/version" is at the end of userAgent
        else if ( (parseInt(nAgt.lastIndexOf(' '))+1) < (verOffset=parseInt(nAgt.lastIndexOf('/'))) ) {
            fullVersion = nAgt.substring(verOffset+1);
        }
		// trim the fullVersion string at semicolon/space if present
        if ((ix = fullVersion.indexOf(";"))!=-1)
            fullVersion = fullVersion.substring(0,ix);
        if ((ix = fullVersion.indexOf(" "))!=-1)
            fullVersion = fullVersion.substring(0,ix);

        var majorVersion = parseInt(''+fullVersion,10);

		if (isNaN(majorVersion)) {
            fullVersion = ''+parseFloat(navigator.appVersion);
        }

		return fullVersion;
    }-*/;
}