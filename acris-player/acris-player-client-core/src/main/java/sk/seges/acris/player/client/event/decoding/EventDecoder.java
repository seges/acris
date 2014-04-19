package sk.seges.acris.player.client.event.decoding;

import sk.seges.acris.core.client.bean.BeanWrapper;
import sk.seges.acris.recorder.client.event.*;
import sk.seges.acris.recorder.client.event.fields.*;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class EventDecoder {

    private final CacheMap cacheMap;

    public EventDecoder(CacheMap cacheMap) {
        this.cacheMap = cacheMap;
    }

	public AbstractGenericEvent decodeEvent(byte[] event) throws IllegalArgumentException {

		long value = longFromByteArray(event);

		EventType eventType = getEventType(value);

		AbstractGenericEvent abstractGenericEvent = null;

		if (eventType.equals(EventType.HtmlEvent)) {

			abstractGenericEvent = new HtmlEvent(cacheMap);

			HtmlEventBeanWrapper htmlEventBeanWrapper = new HtmlEventBeanWrapper();
			htmlEventBeanWrapper.setBeanWrapperContent((HtmlEvent) abstractGenericEvent);

			decodeEvent(EHtmlEventFields.values(), value, htmlEventBeanWrapper);
		} else if (eventType.equals(EventType.KeyboardEvent)) {
			abstractGenericEvent = new KeyboardEvent(cacheMap);

			KeyboardEventBeanWrapper keyboardEventBeanWrapper = new KeyboardEventBeanWrapper();
			keyboardEventBeanWrapper.setBeanWrapperContent((KeyboardEvent) abstractGenericEvent);

			decodeEvent(EKeyboardEventFields.values(), value, keyboardEventBeanWrapper);
		} else if (eventType.equals(EventType.MouseEvent)) {
			abstractGenericEvent = new MouseEvent(cacheMap);

			MouseEventBeanWrapper mouseEventBeanWrapper = new MouseEventBeanWrapper();
			mouseEventBeanWrapper.setBeanWrapperContent((MouseEvent)abstractGenericEvent);

			decodeEvent(EMouseEventFields.values(), value, mouseEventBeanWrapper);
		} else if (eventType.equals(EventType.CustomEvent)) {
            abstractGenericEvent = new ClipboardEvent(cacheMap);

            //Currently supporting only clipboard events. When more comes, it should be distinguished
            //using 3rd and 17th bit
            ClipboardEventBeanWrapper clipboardEventBeanWrapper = new ClipboardEventBeanWrapper();
            clipboardEventBeanWrapper.setBeanWrapperContent((ClipboardEvent)abstractGenericEvent);

            decodeEvent(EClipboardEventFields.values(), value, clipboardEventBeanWrapper);
        }

		return abstractGenericEvent;
	}

	public static final long longFromByteArray(byte[] bytes) {
		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value += ((long) bytes[bytes.length - i - 1] & 0xffL) << (8 * i);
		}
		return value;
	}

	public static void decodeEvent(IEventFields[] eventFields, long event, BeanWrapper<? extends AbstractGenericEvent> beanWrapper) throws IllegalArgumentException {

		for (IEventFields eventField : eventFields) {
			FieldDefinition fieldDefinition = eventField.getFieldDefinition();

			if (fieldDefinition.getField() == null) {
				// Do nothing
			} else {
				int result = ValueDecoder.readValueFromPosition(fieldDefinition.getPosition(), event, fieldDefinition.getLength());
				beanWrapper.setBeanAttribute(fieldDefinition.getField(), result);
			}
		}
	}

	public static EventType getEventType(long event) {

		int type = ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_EVENT_TYPE_SHIFT, event, 2);

		EventType eventType = EventType.getEvent(type);

		if (eventType != null) {
			return eventType;
		}

		throw new IllegalArgumentException("Unable to resolve event type");
	};

	public static int getEventLength(long event) {
		return ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_LENGTH_SHIFT, event, 1) + 1;
	}
}