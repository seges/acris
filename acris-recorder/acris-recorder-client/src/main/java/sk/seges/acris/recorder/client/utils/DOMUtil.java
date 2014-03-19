package sk.seges.acris.recorder.client.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class DOMUtil {
	private static Map<String, Integer> indexes = new HashMap<String, Integer>();
	
	private static String[] excludedProperties = new String[] {"STYLE", "CLASS"};
	
	public static String elementTagToString(Element el) {
		String elementString = el.getString().replace(el.getInnerHTML(), "");
		elementString = elementString.toUpperCase();

		String[] properties = elementString.split("=");
		
		String result = "";

		for (String property: properties) {
			if (result.length() == 0) {
				result = property;
			} else {
				if (!property.startsWith("\"")) {
					int space = property.indexOf(" ");
					int bracket = property.indexOf(">");

					int pos = space;
					if (space != -1 && bracket != -1) {
						pos = Math.min(space, bracket);
					} else if (bracket != -1) {
						pos = bracket;
					}
					
					result += "=\"" + property.substring(0, pos	) + "\"" + property.substring(pos);
				} else {
					result += "=" + property;
				}
			}
		}

		int enclosingTagIndex = result.indexOf("</");
		
		if (enclosingTagIndex >= 0) {
			result = result.substring(0, enclosingTagIndex).trim();
		}

		for (String excludedProperty : excludedProperties) {
			result = result.replaceAll(excludedProperty + "=\"[a-zA-Z0-9 \\-;_\\.:,<>|'{}=\\+]*\"", "");
		}
		
		result = result.replaceAll("<", "");
		result = result.replaceAll(">", "");

		result = result.replaceAll("[=\\\"\\[\\- ;_\\.:,<>|'{}=\\+]","");

		result = result.replaceAll(" ", "");

//		int length = 0;
		
//		while (length != result.length()) {
//			length = result.length();
//			result = result.replaceAll("  ", " ");
//		}
		
		return result;
	}
	
	public static void generateId(Element el, EventListener listener) {
		
		if (listener instanceof Widget) {
//			Element parentElement = ((Widget)listener).getElement();
			Element parentElement = el.getParentElement();

			String parentID = DOM.getElementProperty((com.google.gwt.user.client.Element)parentElement.cast(), "id");

			if (parentID == null) {
				parentID = "";
			}

//			Window.alert(elementTagToString(el));
			
			parentID = parentID + "_" + elementTagToString(el);

			int index = 1;
			
			if (indexes.containsKey(parentID)) {
				index = indexes.get(parentID)+1;
			}

			indexes.put(parentID, index);
			
			String ID = parentID + "_" + index;

			//Window.alert(ID + "__" + hashCode(ID));
			
			DOM.setElementProperty((com.google.gwt.user.client.Element)el.cast(), "id", ID);
		}
	}
	
	public static int hashCode(String text) {
		int h = 0;
	    int off = 0;
	    char val[] = new char[text.length()];
	    
	    for (int i = 0; i < text.length(); i++) {
	    	val[i] = text.charAt(i);
	    }
	    
	    int len = text.length();
	
        for (int i = 0; i < len; i++) {
            h = 31*h + val[off++];
            h = h & 0xFFFFFFFF;
        }

        return h;
	}

	public static void generateId(Element el) {

//		String currentID = DOM.getElementProperty((com.google.gwt.user.client.Element)el.cast(), "id");
//		if (currentID != null && currentID.length() > 0) {
//			return;
//		}
		
//		DOM.setElementProperty((com.google.gwt.user.client.Element)el.cast(), "id", "1");

//		JavaScriptObject callStack = setCallStack0(StackTrace.getCallStackFunctions());
//		final String[] functionNames = StackTrace.getCallStackFunctionNames(callStack);
//		
//		
//		StackTraceItem[] stackTrace = StackTrace.buildStackTraceElements(functionNames);
//
//		String fullIDString = "";
//		
//		for (StackTraceItem ste : stackTrace) {
////			if (ste.getClassName().contains("sk.seges")) {
////				GWT.log(ste.toString(), null);
//				if (fullIDString.length() != 0) {
//					fullIDString += "_";
//				}
//				fullIDString += ste.getClassName().substring(ste.getClassName().lastIndexOf(".") + 1) + "." + ste.getMethodName();
////			}
//		}
//
//		Window.alert(StackTrace.asString(stackTrace));
//
//		int index = 1;
//		
//		if (indexes.containsKey(fullIDString)) {
//			index = indexes.get(fullIDString)+1;
//		}
//
//		indexes.put(fullIDString, index);
//		
//		Integer ID = fullIDString.hashCode() + index;
//
//		GWT.log(fullIDString + "---" + fullIDString.hashCode() + "---" + ID, null);
//
////		setID(el, ID.toString());
//		DOM.setElementProperty((com.google.gwt.user.client.Element)el.cast(), "id", ID.toString());
//		Window.alert(fullIDString);
	}

	public native static JavaScriptObject setCallStack0(final JavaScriptObject array)/*-{
	 array.shift(); // remove the StackTrace.getCallStackFunctions() frame
	 array.shift(); // remove the Throwable constructor frame
	 return array;
	 }-*/;

	public static native void setID(Element el, String id) /*-{
		el.id = id;
	}-*/;
}