package sk.seges.acris.rpc.event.fields;

public interface IEventFields {
	
	boolean isStringMapper();

	FieldDefinition getFieldDefinition();

	int getValue();
}
