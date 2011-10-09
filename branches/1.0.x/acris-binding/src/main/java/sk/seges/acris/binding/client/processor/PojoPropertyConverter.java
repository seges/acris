package sk.seges.acris.binding.client.processor;


public class PojoPropertyConverter implements IBeanPropertyConverter {

	public PojoPropertyConverter() {	
	}
	
	@Override
	public String getConvertedPropertyValue(String originalPropertyName) {
		return originalPropertyName;
	}

	@Override
	public String getConvertedPropertyName(String originalPropertyName) {
		String result = "";

		for (int i = 0; i < originalPropertyName.length(); i++) {
			char c = originalPropertyName.charAt(i);
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