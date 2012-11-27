package sk.seges.acris.recorder.rpc.event.fields;

import sk.seges.acris.recorder.rpc.event.HtmlEvent;


public enum EHtmlEventFields implements IEventFields {
	EVENT_ACTION_TYPE(0, 3, HtmlEvent.TYPE_INT_ATTRIBUTE),
	EVENT_TARGET(EVENT_ACTION_TYPE, 20, HtmlEvent.TARGET_ATTRIBUTE, true),
	EMPTY(EVENT_TARGET, 6, 0),
	EVENT_TYPE(EMPTY, 2, 3),
	EVENT_LENGTH(EVENT_TYPE, 1, 0);
	
	private FieldDefinition fieldDefinition;
	private int value;
	private boolean stringMapper = false;;
	
	private EHtmlEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EHtmlEventFields(int position, int length, int value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		this.value = value;
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

	private EHtmlEventFields(EHtmlEventFields keyboardEventFields, int length, String field, boolean stringMapper) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
		this.stringMapper = stringMapper;
	}

	public boolean isStringMapper() {
		return stringMapper;
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
