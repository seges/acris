package sk.seges.acris.binding.client.processor;

public interface IBeanPropertyConverter {

	String getConvertedPropertyValue(String originalPropertyName);

	String getConvertedPropertyName(String originalPropertyName);
	
	boolean handleMethods();
	
	boolean handleFields();
	
	boolean supportsHierarchy();
}
