package sk.seges.sesam.model.metadata.annotation.strategy;

import sk.seges.sesam.model.metadata.annotation.strategy.api.ModelPropertyConverter;


public class DBPropertyConverter implements ModelPropertyConverter {

	private final String DB_PREFIX = "DB_";

	public DBPropertyConverter() {	
	}
	
	@Override
	public String getConvertedPropertyValue(String originalPropertyName) {
		return convertInPOJOWay(originalPropertyName).toLowerCase();
	}

	@Override
	public String getConvertedPropertyName(String originalPropertyName) {
		return DB_PREFIX + convertInPOJOWay(originalPropertyName);
	}

	private String convertInPOJOWay(String name) {
		String result = "";

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				result += "_";
			}
			result += c;
		}

		return result.toUpperCase();
	}
	
	@Override
	public boolean handleMethods() {
		return true;
	}

	@Override
	public boolean handleFields() {
		return true;
	}

	@Override
	public boolean supportsHierarchy() {
		return true;
	}
}