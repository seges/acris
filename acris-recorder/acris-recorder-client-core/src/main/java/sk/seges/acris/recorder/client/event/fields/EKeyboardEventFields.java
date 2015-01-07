package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.KeyboardEvent;

public enum EKeyboardEventFields implements IEventFields {
	EVENT_ACTION_TYPE(0, 2, KeyboardEvent.TYPE_INT_ATTRIBUTE),
	EVENT_CTRL_KEY(EVENT_ACTION_TYPE, 1, KeyboardEvent.CTRL_KEY_INT_ATTRIBUTE),
	EVENT_ALT_KEY(EVENT_CTRL_KEY, 1, KeyboardEvent.ALT_KEY_INT_ATTRIBUTE),
	EVENT_SHIFT_KEY(EVENT_ALT_KEY, 1, KeyboardEvent.SHIFT_KEY_INT_ATTRIBUTE),
	EVENT_META_KEY(EVENT_SHIFT_KEY, 1, KeyboardEvent.META_KEY_INT_ATTRIBUTE),
	EMPTY_1(EVENT_META_KEY, 1),
    EVENT_SCROLL_TYPE(EMPTY_1, 1, KeyboardEvent.SCROLL_TYPE),
    EMPTY_2(EVENT_SCROLL_TYPE, 1, 0),
    EVENT_SCROLL_OFFSET(EMPTY_2, 11, KeyboardEvent.SCROLL_OFFSET),
    EMPTY_3(EVENT_SCROLL_OFFSET, 1),
	EVENT_KEYCODE(EMPTY_3, 8, KeyboardEvent.KEY_CODE_ATTRIBUTE),
	EVENT_TYPE(EVENT_KEYCODE, 2, 2),
	EVENT_LENGTH(EVENT_TYPE, 1, 0);
	
	private FieldDefinition fieldDefinition;
	private long value;

	private EKeyboardEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length, String field) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
        fieldDefinition.setLength(length);
        fieldDefinition.setField(field);
    }

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length) {
		this(keyboardEventFields, length, Long.valueOf(new String(new char[length]).replace("\0", "1"), 2));
	}

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length, long value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
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