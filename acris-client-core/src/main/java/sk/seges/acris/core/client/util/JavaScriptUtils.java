package sk.seges.acris.core.client.util;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptUtils {
    public static native JavaScriptObject getURL(String url)/*-{
        return $wnd.open(url,'export',
        'target=_new')
    }-*/;

    public static native JavaScriptObject openFile(String url)/*-{
        return $wnd.open(url)
    }-*/;
    
	public static native String encodeURIComponent(String uri) /*-{
		return encodeURIComponent(uri);
	}-*/;
}
