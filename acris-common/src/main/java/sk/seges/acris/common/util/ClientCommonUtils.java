package sk.seges.acris.common.util;

public class ClientCommonUtils {

	public static native String getWebId() /*-{
		return $wnd.webId;
	}-*/;
}
