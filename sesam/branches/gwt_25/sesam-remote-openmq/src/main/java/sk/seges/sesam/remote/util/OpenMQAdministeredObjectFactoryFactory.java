/**
 * 
 */
package sk.seges.sesam.remote.util;

import java.util.Map;
import java.util.Map.Entry;

import com.sun.messaging.AdministeredObject;

/**
 * @author eldzi
 */
public class OpenMQAdministeredObjectFactoryFactory {
    private Class<AdministeredObject> objectType;
    private Map<String, String> additionalProperties;
    
    @SuppressWarnings("unchecked")
    public void setObjectType(String objectType) {
        try {
            this.objectType = (Class<AdministeredObject>)Class.forName(objectType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setObjectType(Class<AdministeredObject> objectType) {
        this.objectType = objectType;
    }
    
    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    
    public Object createInstance() throws Exception {
        AdministeredObject o = objectType.newInstance();
        if(additionalProperties != null) {
            for(Entry<String, String> entry : additionalProperties.entrySet()) {
                o.setProperty(entry.getKey(), entry.getValue());
            }
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        return objectType;
    }

}
