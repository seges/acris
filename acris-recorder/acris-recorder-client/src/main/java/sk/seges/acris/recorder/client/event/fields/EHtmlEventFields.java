package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.HtmlEvent;

public enum EHtmlEventFields implements IEventFields {

	EVENT_ACTION_TYPE(0, 3, HtmlEvent.TYPE_INT_ATTRIBUTE),
	EMPTY(EVENT_ACTION_TYPE, 26, 0),
	EVENT_TYPE(EMPTY, 2, 3),
	EVENT_LENGTH(EVENT_TYPE, 1, 0);
	
	private FieldDefinition fieldDefinition;
	private int value;

	private EHtmlEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	public int getValue() {
		return value;
	}

	private EHtmlEventFields(EHtmlEventFields keyboardEventFields, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EHtmlEventFields(EHtmlEventFields keyboardEventFields, int length, int value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		this.value = value;
	}

	public FieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}
}