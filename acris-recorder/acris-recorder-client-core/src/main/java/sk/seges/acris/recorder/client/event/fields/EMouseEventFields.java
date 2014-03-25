package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.MouseEvent;

public enum EMouseEventFields implements IEventFields {

	EVENT_ACTION_TYPE(0, 3, MouseEvent.TYPE_INT_ATTRIBUTE),
	EVENT_CLIENT_X(EVENT_ACTION_TYPE, 14, MouseEvent.CLIENT_X_ATTRIBUTE),
	EVENT_CLIENT_Y(EVENT_CLIENT_X, 13, MouseEvent.CLIENT_Y_ATTRIBUTE),
	EVENT_TYPE(EVENT_CLIENT_Y, 1, 0),
	EVENT_LENGTH(EVENT_TYPE, 1, 1),
	EVENT_CTRL_KEY(EVENT_LENGTH, 1, MouseEvent.CTRL_KEY_INT_ATTRIBUTE),
	EVENT_ALT_KEY(EVENT_CTRL_KEY, 1, MouseEvent.ALT_KEY_INT_ATTRIBUTE),
	EVENT_SHIFT_KEY(EVENT_ALT_KEY, 1, MouseEvent.SHIFT_KEY_INT_ATTRIBUTE),
	EVENT_META_KEY(EVENT_SHIFT_KEY, 1, MouseEvent.META_KEY_INT_ATTRIBUTE),
	EVENT_RELATIVE(EVENT_META_KEY, 1, MouseEvent.RELATIVE_INT_ATTRIBUTE),
	EVENT_SCREEN_X(EVENT_RELATIVE, 14, MouseEvent.SCREEN_X_ATTRIBUTE),
	EVENT_SCREEN_Y(EVENT_SCREEN_X, 13, MouseEvent.SCREEN_Y_ATTRIBUTE);
	
	private FieldDefinition fieldDefinition;
	private int value;

	private EMouseEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	public int getValue() {
		return value;
	}

	private EMouseEventFields(EMouseEventFields keyboardEventFields, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EMouseEventFields(EMouseEventFields keyboardEventFields, int length, int value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		this.value = value;
	}

	public FieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}
}