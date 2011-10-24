package sk.seges.acris.widget.client.util;

public class WidgetUtils {

	public static native int getPageScrollX()/*-{
		var scrOfX = 0;
		if( typeof( $wnd.pageYOffset ) == 'number' ) {
		    scrOfX = $wnd.pageXOffset;
		} else if( $doc.body && $doc.body.scrollLeft ) {
		    scrOfX = $doc.body.scrollLeft;
		} else if( $doc.documentElement && $doc.documentElement.scrollLeft ) {
		    scrOfX = $doc.documentElement.scrollLeft;
		}
		return scrOfX;	
	}-*/;

	public static native int getPageScrollY()/*-{
		var scrOfY = 0;
		if( typeof( $wnd.pageYOffset ) == 'number' ) {
			scrOfY = $wnd.pageYOffset;
		} else if( $doc.body && $doc.body.scrollTop ) {
			scrOfY = $doc.body.scrollTop;
		} else if( $doc.documentElement && $doc.documentElement.scrollTop ) {
			scrOfY = $doc.documentElement.scrollTop;
		}
		return scrOfY;
	}-*/;
}
