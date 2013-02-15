package sk.seges.sesam.pap.metadata.model;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;

public class MetaModelContext {

	public static final String THIS = "THIS";
	
	private ModelPropertyConverter converter;
	
	private List<String> properties = new LinkedList<String>();
	private List<TypeElement> processingElements = new LinkedList<TypeElement>();

	public String getProperty() {
		if (properties.size() == 0) {
			return null;
		}
		return properties.get(properties.size() - 1);
	}

	public MetaModelContext setProperty(String property) {
		if (property == null) {
			if (properties.size() > 0) {
				properties.remove(properties.size() - 1);
			}
		} else {
			properties.add(property);
		}
		return this;
	}

	public ModelPropertyConverter getConverter() {
		return converter;
	}

//	public void 
	public MetaModelContext setConverter(ModelPropertyConverter converter) {
		this.converter = converter;
		return this;
	}

	public String getFieldName() {
		if (getProperty().equals(THIS)) {
			return THIS;
		}
		return converter.getConvertedPropertyName(getProperty());
	}

	private String toPath(boolean includeLast) {
		String result = "";
		
		for (int i = 0; i < properties.size() - (includeLast ? 0 : 1); i++) {
			if (i > 0) {
				result += ".";
			}
			result += properties.get(i);
		}
		
		return result;
	}
	
	public String getPath() {
		if (getProperty().equals(THIS)) {
			return converter.getConvertedPropertyValue(toPath(false));
		}
		return converter.getConvertedPropertyValue(toPath(true));
	}

	public void setProcessingElement(TypeElement processingElement) {
		if (processingElement == null) {
			if (processingElements.size() > 0) {
				processingElements.remove(processingElements.size() - 1);
			}
		} else {
			processingElements.add(processingElement);
		}
	}

	public boolean containsProcessingElement(TypeElement element) {
		for (int i = 1; i < processingElements.size() - 1; i++) {
			if (processingElements.get(i).equals(element)) {
				return true;
			}
		}

		return false;
	}

	public boolean containsProperty(String property) {
		for (int i = 0; i < properties.size() - 1; i++) {
			if (properties.get(i).equals(property)) {
				return true;
			}
		}

		return false;
	}

	public TypeElement getProcessingElement() {
		if (processingElements.size() == 0) {
			return null;
		}
		return processingElements.get(processingElements.size() - 1);
	}
	
	public MetaModelContext clone() {
		MetaModelContext context = new MetaModelContext();
		
		context.setConverter(converter);
		
		for (String property: properties) {
			context.setProperty(property);
		}
		
		for (TypeElement processingElement: processingElements) {
			context.setProcessingElement(processingElement);
		}
		
		return context;
	}
	
	public boolean isNested() {
		return properties.size() > 0;
	}
}