package sk.seges.acris.widget.client.i18n;

import java.util.MissingResourceException;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public class DynamicTranslator {

    private ConstantsWithLookup[] messages;
    
    public DynamicTranslator(ConstantsWithLookup ... constantsWithLookups) {
        this.messages = constantsWithLookups;
    }
    
    public String translate(String key) {
        String result = null;
        for(ConstantsWithLookup constants : messages) {
            try {
                result = constants.getString(key);
                if(null != result && !result.isEmpty()) {
                    break;
                }
            } catch(MissingResourceException e) {
                result = key;
            }
        }
        if(result == null || result.isEmpty()) {
            result = key;
        }
        return result;
    }
    
    /**
	* enums from beantable come as strings,
	* so they are converted here to strings
	*/
    public String translate(Enum<?> key) {
    	String newKey = null;
    	if(key instanceof Enum<?>)
    		newKey = ((Enum<?>)key).name();
    	return (newKey==null) ? null : translate(newKey);
    }
}
