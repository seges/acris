package sk.seges.acris.binding.client.metadata;

import java.beans.PropertyDescriptor;

import sk.seges.acris.binding.client.metadata.api.MetaDataDescriptor;

public class PropertyMetaDescriptor<T, F> implements MetaDataDescriptor {

	private static final long serialVersionUID = 4343985701744332337L;

	protected String className;
	protected String fieldName;
	protected String localDataSourceName;
	protected String localDateTimeFormat;
	protected String localStringFormat;

	transient protected Class<?> fieldType;

	transient protected PropertyDescriptor propertyDescriptor;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	public boolean setValue(T object, F value) {
		try {
			getPropertyDescriptor().getWriteMethod().invoke(object, value);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public F getValue(T object) {
		try {
			return (F) getPropertyDescriptor().getReadMethod().invoke(object);
		} catch (Exception e) {
			return null;
		}
	}

	PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	void setPropertyDescriptor(PropertyDescriptor writePropertyDescriptor) {
		this.propertyDescriptor = writePropertyDescriptor;
	}

	public String getLocalDataSourceName() {
		return this.localDataSourceName;
	}

	public void setLocalDataSourceName(String input) {
		this.localDataSourceName = input;
	}

	public String getLocalDateTimeFormat() {
		return localDateTimeFormat;
	}

	public void setLocalDateTimeFormat(String localDateTimeFormat) {
		this.localDateTimeFormat = localDateTimeFormat;
	}

	public void setLocalStringFormat(String inputFormat) {
		this.localStringFormat = inputFormat;
	}

	public String getLocalStringFormat() {
		return this.localStringFormat;
	}
}