package sk.seges.acris.binding.rebind.bean;

import java.beans.IntrospectionException;

import sk.seges.acris.core.rebind.RebindUtils;

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

		int lastDotIndex = property.lastIndexOf(".");
		
		if (lastDotIndex == -1) {
			throw new IntrospectionException("Incorrect property description " + property + ". Property should consits from listBeanReferenceProperty and listBeanProperty separated by dot. (e.g. program.name)");
		}
		
		listBeanProperty = property.substring(0, lastDotIndex);
		listBeanReferenceProperty = property.substring(lastDotIndex + 1); 
	}
	
	public JClassType resolveBeanPropertyClassType(JClassType classType) throws IntrospectionException {		
		return resolveClassType(classType, listBeanProperty);
	}
	
	public JClassType resolveBeanReferencePropertyClassType(JClassType classType) throws IntrospectionException {
		return resolveClassType(classType, listBeanProperty + "." + listBeanReferenceProperty);
	}
	
	private JClassType resolveClassType(JClassType classType, String property) throws IntrospectionException {
		JClassType listBeanClassType = RebindUtils.getDeclaredFieldClassType(classType, property);
		
		if (listBeanClassType == null) {
			throw new IntrospectionException("Cannot find class type for property " + property + " in bean " + classType.getQualifiedSourceName());
		}	
		
		return listBeanClassType;
	}

	public String getBeanProperty() {
		return listBeanProperty;
	}
	
	public String getBeanPropertyVariableName() {
		return getBeanProperty().replaceAll("\\.", "_");
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