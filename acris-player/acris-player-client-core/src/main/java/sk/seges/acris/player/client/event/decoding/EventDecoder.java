package sk.seges.acris.player.client.event.decoding;

import sk.seges.acris.core.client.bean.BeanWrapper;
import sk.seges.acris.recorder.client.event.*;
import sk.seges.acris.recorder.client.event.fields.*;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class EventDecoder {

	public static AbstractGenericEvent decodeEvent(byte[] event) throws IllegalArgumentException {

		long value = longFromByteArray(event);

		EventType eventType = getEventType(value);

		AbstractGenericEvent abstractGenericEvent = null;

		if (eventType.equals(EventType.HtmlEvent)) {

			abstractGenericEvent = new HtmlEvent();

			HtmlEventBeanWrapper htmlEventBeanWrapper = new HtmlEventBeanWrapper();
			htmlEventBeanWrapper.setBeanWrapperContent((HtmlEvent) abstractGenericEvent);

			decodeEvent(EHtmlEventFields.values(), value, htmlEventBeanWrapper);
		} else if (eventType.equals(EventType.KeyboardEvent)) {
			abstractGenericEvent = new KeyboardEvent();

			KeyboardEventBeanWrapper keyboardEventBeanWrapper = new KeyboardEventBeanWrapper();
			keyboardEventBeanWrapper.setBeanWrapperContent((KeyboardEvent) abstractGenericEvent);

			decodeEvent(EKeyboardEventFields.values(), value, keyboardEventBeanWrapper);
		} else if (eventType.equals(EventType.MouseEvent)) {
			abstractGenericEvent = new MouseEvent();

			MouseEventBeanWrapper mouseEventBeanWrapper = new MouseEventBeanWrapper();
			mouseEventBeanWrapper.setBeanWrapperContent((MouseEvent)abstractGenericEvent);

			decodeEvent(EMouseEventFields.values(), value, mouseEventBeanWrapper);
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

		int type = ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_EVENT_TYPE_SHIFT, event, 1);

		EventType eventType = EventType.getEvent(type);

		if (eventType != null) {
			return eventType;
		}

		int typeNext = ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_EVENT_TYPE_SHIFT - 1, event, 1);
		int resultType = type * 2 + typeNext;

		eventType = EventType.getEvent(resultType);

		if (eventType != null) {
			return eventType;
		}

		throw new IllegalArgumentException("Unable to resolve event type");
	};

	public static int getEventLength(long event) {
		return ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_LENGTH_SHIFT, event, 1) + 1;
	}
}