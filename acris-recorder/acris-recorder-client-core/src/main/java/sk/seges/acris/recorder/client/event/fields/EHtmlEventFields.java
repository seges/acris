package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.HtmlEvent;

public enum EHtmlEventFields implements IEventFields {

	EVENT_ACTION_TYPE(0, 3, HtmlEvent.TYPE_INT_ATTRIBUTE),
	EMPTY(EVENT_ACTION_TYPE, 26),
	EVENT_TYPE(EMPTY, 2, 3),
	EVENT_LENGTH(EVENT_TYPE, 1, 0);
	
	private FieldDefinition fieldDefinition;
	private long value;

	private EHtmlEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EHtmlEventFields(EHtmlEventFields htmlEventFields, int length) {
		this(htmlEventFields, length, Long.valueOf(new String(new char[length]).replace("\0", "1"), 2));
	}

	private EHtmlEventFields(EHtmlEventFields htmlEventFields, int length, long value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(htmlEventFields.getFieldDefinition().getPosition() + htmlEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public FieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}
}