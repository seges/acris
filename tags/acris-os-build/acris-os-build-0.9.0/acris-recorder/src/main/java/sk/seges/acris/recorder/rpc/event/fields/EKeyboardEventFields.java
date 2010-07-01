package sk.seges.acris.recorder.rpc.event.fields;

import sk.seges.acris.recorder.rpc.event.KeyboardEvent;



public enum EKeyboardEventFields implements IEventFields {
	EVENT_ACTION_TYPE(0, 2, KeyboardEvent.TYPE_INT_ATTRIBUTE),
	EVENT_CTRL_KEY(EVENT_ACTION_TYPE, 1, KeyboardEvent.CTRL_KEY_INT_ATTRIBUTE),
	EVENT_ALT_KEY(EVENT_CTRL_KEY, 1, KeyboardEvent.ALT_KEY_INT_ATTRIBUTE),
	EVENT_SHIFT_KEY(EVENT_ALT_KEY, 1, KeyboardEvent.SHIFT_KEY_INT_ATTRIBUTE),
	EVENT_META_KEY(EVENT_SHIFT_KEY, 1, KeyboardEvent.META_KEY_INT_ATTRIBUTE),
	EVENT_KEYCODE(EVENT_META_KEY, 8, KeyboardEvent.KEY_CODE_ATTRIBUTE),
	EVENT_CHARCODE(EVENT_KEYCODE, 8, KeyboardEvent.CHAR_CODE_ATTRIBUTE),
	EVENT_TARGET(EVENT_CHARCODE, 7, KeyboardEvent.TARGET_ATTRIBUTE, true),
	EVENT_TYPE(EVENT_TARGET, 2, 2),
	EVENT_LENGTH(EVENT_TYPE, 1, 0);
	
	private FieldDefinition fieldDefinition;
	private int value;
	private boolean stringMapper = false;;
	
	private EKeyboardEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EKeyboardEventFields(int position, int length, String field, boolean stringMapper) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
		this.stringMapper = stringMapper;
	}

	private EKeyboardEventFields(int position, int length, int value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length, String field, boolean stringMapper) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
		this.stringMapper = stringMapper;
	}

	public boolean isStringMapper() {
		return stringMapper;
	}

	private EKeyboardEventFields(EKeyboardEventFields keyboardEventFields, int length, int value) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(keyboardEventFields.getFieldDefinition().getPosition() + keyboardEventFields.getFieldDefinition().getLength());
		fieldDefinition.setLength(length);
		this.value = value;
	}

	public FieldDefinition getFieldDefinition() {
		return fieldDefinition;
	}
}
