package sk.seges.acris.binding.rebind.bean;

import java.beans.IntrospectionException;

import sk.seges.acris.binding.rebind.RebindUtils;

import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * 
 * @author fat
 */
public class PropertyResolver {

	private String listBeanReferenceProperty;
	private String listBeanProperty; 

	public PropertyResolver(String property) throws IntrospectionException {
		if (property == null) {
			throw new IntrospectionException("Binding property is null. Unable to resolve property.");
		}

		String[] props = property.split("\\.");

		if (props.length != 2) {
			throw new IntrospectionException("Incorrect property description " + property + ". Property should consits from listBeanReferenceProperty and listBeanProperty separated by dot. (e.g. program.name)");
		}
		
		listBeanProperty = props[0];
		listBeanReferenceProperty = props[1]; 
		
	}
	
	public JClassType resolveBeanPropertyClassType(JClassType classType) throws IntrospectionException {

		JClassType listBeanClassType = RebindUtils.getDeclaredFieldClassType(classType, listBeanProperty);
		
		if (listBeanClassType == null) {
			throw new IntrospectionException("Cannot find class type for property " + listBeanProperty + " in bean " + classType.getQualifiedSourceName());
		}	
		
		return listBeanClassType;
	}

	public String getBeanProperty() {
		return listBeanProperty;
	}
	
	public String getBeanPropertyReference() {
		return listBeanReferenceProperty;
	}
	
	public String getPropertyTuple(JClassType classType) {
		return classType.getSimpleSourceName() + getFirstUpperCase(getBeanProperty()) + getFirstUpperCase(getBeanPropertyReference());
	}

	private String getFirstUpperCase(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
}