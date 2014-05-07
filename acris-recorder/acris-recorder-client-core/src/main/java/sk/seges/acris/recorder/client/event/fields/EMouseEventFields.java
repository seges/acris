package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.MouseEvent;

public enum EMouseEventFields implements IEventFields {

	EVENT_CTRL_KEY(0, 1, MouseEvent.CTRL_KEY_INT_ATTRIBUTE),
	EVENT_ALT_KEY(EVENT_CTRL_KEY, 1, MouseEvent.ALT_KEY_INT_ATTRIBUTE),
	EVENT_SHIFT_KEY(EVENT_ALT_KEY, 1, MouseEvent.SHIFT_KEY_INT_ATTRIBUTE),
	EVENT_META_KEY(EVENT_SHIFT_KEY, 1, MouseEvent.META_KEY_INT_ATTRIBUTE),
	EVENT_RELATIVE(EVENT_META_KEY, 1, MouseEvent.RELATIVE_INT_ATTRIBUTE),
	EVENT_BUTTON(EVENT_RELATIVE, 3, MouseEvent.BUTTON_ATTRIBUTE),
	EVENT_EMPTY_4(EVENT_BUTTON, 8),
	EVENT_CLIENT_Y_LO(EVENT_EMPTY_4, 7, MouseEvent.CLIENT_Y_LO_ATTRIBUTE),
	EVENT_EMPTY_5(EVENT_CLIENT_Y_LO, 1),
	EVENT_CLIENT_Y_HI(EVENT_EMPTY_5, 7, MouseEvent.CLIENT_Y_HI_ATTRIBUTE),
	EVENT_EMPTY_6(EVENT_CLIENT_Y_HI, 1),

	EVENT_ACTION_TYPE(EVENT_EMPTY_6, 3, MouseEvent.TYPE_INT_ATTRIBUTE),
	EVENT_EMPTY_1(EVENT_ACTION_TYPE, 5),
	EVENT_CLIENT_X_LO(EVENT_EMPTY_1, 7, MouseEvent.CLIENT_X_LO_ATTRIBUTE),
	EVENT_EMPTY_2(EVENT_CLIENT_X_LO, 1),
	EVENT_CLIENT_X_HI(EVENT_EMPTY_2, 7, MouseEvent.CLIENT_X_HI_ATTRIBUTE),
	EVENT_EMPTY_3(EVENT_CLIENT_X_HI, 7),
	EVENT_TYPE(EVENT_EMPTY_3, 1, 0),
	EVENT_LENGTH(EVENT_TYPE, 1, 1);

	private FieldDefinition fieldDefinition;
	private long value;

	private EMouseEventFields(int position, int length, String field) {
		fieldDefinition = new FieldDefinition();
		fieldDefinition.setPosition(position);
		fieldDefinition.setLength(length);
		fieldDefinition.setField(field);
	}

    private EMouseEventFields(EMouseEventFields mouseEventFields, int length, String field) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(mouseEventFields.getFieldDefinition().getPosition() + mouseEventFields.getFieldDefinition().getLength());
        fieldDefinition.setLength(length);
        fieldDefinition.setField(field);
    }

	private EMouseEventFields(EMouseEventFields mouseEventFields, int length) {
		this(mouseEventFields, length, Long.valueOf(new String(new char[length]).replace("\0", "1"), 2));
	}

    private EMouseEventFields(EMouseEventFields mouseEventFields, int length, long value) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(mouseEventFields.getFieldDefinition().getPosition() + mouseEventFields.getFieldDefinition().getLength());
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