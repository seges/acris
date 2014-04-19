package sk.seges.acris.recorder.client.event.fields;

import sk.seges.acris.recorder.client.event.ClipboardEvent;

/**
 * Created by PeterSimun on 12.4.2014.
 */
public enum EClipboardEventFields implements IEventFields {

    EVENT_ACTION_TYPE(0, 2, ClipboardEvent.TYPE_INT_ATTRIBUTE),
    CLIPBOARD_RESERVED_1(EVENT_ACTION_TYPE, 1),
    EMPTY(CLIPBOARD_RESERVED_1, 1),
    START(EMPTY, 11, ClipboardEvent.SELECTION_START_ATTRIBUTE),
    EMPTY_1(START, 1),
    CLIPBOARD_RESERVED_2(EMPTY_1, 1),
    EMPTY_2(CLIPBOARD_RESERVED_2, 1),
    END(EMPTY_2, 11, ClipboardEvent.SELECTION_END_ATTRIBUTE),
    EVENT_TYPE(END, 2, 1),
    EVENT_LENGTH(EVENT_TYPE, 1, 0);

    private FieldDefinition fieldDefinition;
    private long value;

    private EClipboardEventFields(int position, int length, String field) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(position);
        fieldDefinition.setLength(length);
        fieldDefinition.setField(field);
    }

    private EClipboardEventFields(EClipboardEventFields clipboardEventFields, int length, String field) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(clipboardEventFields.getFieldDefinition().getPosition() + clipboardEventFields.getFieldDefinition().getLength());
        fieldDefinition.setLength(length);
        fieldDefinition.setField(field);
    }

    private EClipboardEventFields(EClipboardEventFields cliboardEventFields, int length) {
        this(cliboardEventFields, length, Long.valueOf(new String(new char[length]).replace("\0", "1"), 2));
    }

    private EClipboardEventFields(EClipboardEventFields cliboardEventFields, int length, long value) {
        fieldDefinition = new FieldDefinition();
        fieldDefinition.setPosition(cliboardEventFields.getFieldDefinition().getPosition() + cliboardEventFields.getFieldDefinition().getLength());
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
