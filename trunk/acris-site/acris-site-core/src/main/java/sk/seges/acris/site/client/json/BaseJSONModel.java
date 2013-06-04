/**
 * 
 */
package sk.seges.acris.site.client.json;

import java.io.Serializable;

import com.google.gwt.json.client.JSONObject;

/**
 * @author eldzi
 */
public class BaseJSONModel implements Serializable {
	
	private static final long serialVersionUID = 2142705677258230672L;
	
	protected JSONModel data;

	public BaseJSONModel() {
		data = JSONModel.create();
	}
	
    public BaseJSONModel(JSONModel data) {
    	if (data == null) {
    		data = JSONModel.create();
    	}
        this.data = data;
    }
    
    public String getJSONString() {
    	return new JSONObject(data).toString();
    }
    
    @Deprecated
    @Override
    public String toString() {
    	return getJSONString();
    }
}
