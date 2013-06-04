package sk.seges.acris.widget.client.celltable.column;

public interface DynamicColumDefinition {

	public static final String FIELD = "field";
	public static final String LABEL = "label";
	public static final String TYPE = "type";
	
	String getField();
	void setField(String field);
	
	String getLabel();
	void setLabel(String label);
	
	String getType();
	void setType(String type);

}
