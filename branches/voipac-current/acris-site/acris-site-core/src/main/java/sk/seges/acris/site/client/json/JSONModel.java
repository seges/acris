package sk.seges.acris.site.client.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author ladislav.gazo
 */
public abstract class JSONModel extends JavaScriptObject {

    // Overlay types always have protected, zero-arg constructors
    protected JSONModel() {
    }

    /**
     * Create an empty instance.
     * 
     * @return new Object
     */
    public static native JSONModel create() /*-{
        return new Object();
    }-*/;

    /**
     * Convert a JSON encoded string into a JSOModel instance.
     * <p/>
     * Expects a JSON string structured like '{"foo":"bar","number":123}'
     *
     * @return a populated JSOModel object
     */
    public static native JSONModel fromJson(String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;

    /**
     * Convert a JSON encoded string into an array of JSOModel instance.
     * <p/>
     * Expects a JSON string structured like '[{"foo":"bar","number":123}, {...}]'
     *
     * @return a populated JsArray
     */
    public static native JsArray<JSONModel> arrayFromJson(String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;

    public final native boolean hasKey(String key) /*-{
        return this[key] != undefined;
    }-*/;

    public final native JsArrayString keys() /*-{
        var a = new Array();
        for (var p in this) { a.push(p); }
        return a;
    }-*/;

    @Deprecated
    public final Set<String> keySet() {
        JsArrayString array = keys();
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < array.length(); i++) {
            set.add(array.get(i));
        }
        return set;
    }

    public final String get(String key) {
    	if(!hasKey(key)) {
    		return null;
    	}
    	return getJSNI(key);
    }
    
    private final native String getJSNI(String key) /*-{
        return "" + this[key];
    }-*/;

    public final native String get(String key, String defaultValue) /*-{
        return this[key] ? ("" + this[key]) : defaultValue;
    }-*/;

    public final native void set(String key, String value) /*-{
        this[key] = value;
    }-*/;

    public final native void set(String key, int value) /*-{
    	this[key] = value;
	}-*/;
    
    public final native void set(String key, boolean value) /*-{
		this[key] = value;
	}-*/;
    
    public final native void set(String key, JavaScriptObject value) /*-{
		this[key] = value;
	}-*/;
    
    public final void set(String key, String[] array) {
    	JsArrayString jsArray = createStringArray();
    	jsArray.setLength(array.length);
    	for(int i = 0; i < array.length; i++) {
    		jsArray.set(i, array[i]);
    	}
    	set(key, jsArray);
    }
    
    public final Integer getInteger(String key) {
    	if(!hasKey(key)) {
    		return null;
    	}
        return Integer.parseInt(get(key));
    }

    public final Boolean getBoolean(String key) {
    	if(!hasKey(key)) {
    		return null;
    	}
        return Boolean.parseBoolean(get(key));
    }
    
    public final String[] getStringArray(String key) {
    	if(!hasKey(key)) {
    		return null;
    	}
    	JsArrayString jsonArray = getJsStringArray(key);
    	String[] array = new String[jsonArray.length()];
    	for(int i = 0; i < jsonArray.length(); i++) {
    		array[i] = jsonArray.get(i);
    	}
    	return array;
    }

	public final Map<String, String> getStringStringMap(String key) {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject object = new JSONObject(this);
		JSONObject value = (JSONObject) object.get(key);
		Set<String> keys = value.keySet();
		for (String string : keys) {
			JSONString jValue = value.get(string).isString();
			map.put(string, jValue.stringValue());
		}
		return map;
	}
	
	public final void setStringStringMap(String key, Map<String, String> map) {
		throw new RuntimeException("Not yet implemented");
	}
    
	public final Map<String, Integer> getStringIntMap(String key) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		JSONObject object = new JSONObject(this);
		JSONObject value = (JSONObject) object.get(key);
		Set<String> keys = value.keySet();
		for (String string : keys) {
			JSONNumber jValue = value.get(string).isNumber();
			map.put(string, Double.valueOf(jValue.doubleValue()).intValue());
		}
		return map;
	}

	public final void setStringIntMap(String key, Map<String, Integer> map) {
		throw new RuntimeException("Not yet implemented");
	}
	
    public final native JSONModel getObject(String key) /*-{
        return this[key];
    }-*/;

    public final native JsArray<JSONModel> getArray(String key) /*-{
        return this[key] ? this[key] : new Array();
    }-*/;
    
    public final native JsArrayString getJsStringArray(String key) /*-{
    	return this[key] ? this[key] : null;
	}-*/;
    
    private final native JsArrayString createStringArray() /*-{
    	return new Array();
    }-*/;
}
