package sk.seges.acris.recorder.rpc.event.fields;

public interface IEventFields {
	
	boolean isStringMapper();

	FieldDefinition getFieldDefinition();

	int getValue();
}
