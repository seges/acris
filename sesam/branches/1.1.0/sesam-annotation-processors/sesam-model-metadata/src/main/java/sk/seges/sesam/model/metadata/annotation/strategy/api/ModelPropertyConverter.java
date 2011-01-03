package sk.seges.sesam.model.metadata.annotation.strategy.api;

public interface ModelPropertyConverter {

	String getConvertedPropertyValue(String originalPropertyName);

	String getConvertedPropertyName(String originalPropertyName);
	
	boolean handleMethods();
	
	boolean handleFields();
	
	boolean supportsHierarchy();
}
