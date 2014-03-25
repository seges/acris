package sk.seges.acris.recorder.client.event.encoding;

import sk.seges.acris.core.client.bean.BeanWrapper;
import sk.seges.acris.recorder.client.event.*;
import sk.seges.acris.recorder.client.event.fields.*;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;

public class EventEncoder {

	public static byte[] encodeEvent(AbstractGenericEvent event) throws IllegalArgumentException {

		if (event instanceof HtmlEvent) {
			HtmlEventBeanWrapper htmlEventBeanWrapper = new HtmlEventBeanWrapper();
			htmlEventBeanWrapper.setBeanWrapperContent((HtmlEvent) event);
			return encodeEvent(EHtmlEventFields.values(), htmlEventBeanWrapper);
		}

		if (event instanceof KeyboardEvent) {
			KeyboardEventBeanWrapper keyboardEventBeanWrapper = new KeyboardEventBeanWrapper();
			keyboardEventBeanWrapper.setBeanWrapperContent((KeyboardEvent) event);
			return encodeEvent(EKeyboardEventFields.values(), keyboardEventBeanWrapper);
		}

		if (event instanceof MouseEvent) {
			MouseEventBeanWrapper mouseEventBeanWrapper = new MouseEventBeanWrapper();
			mouseEventBeanWrapper.setBeanWrapperContent((MouseEvent)event);
			return encodeEvent(EMouseEventFields.values(), mouseEventBeanWrapper);
		}

		throw new IllegalArgumentException("Unknown event type");
	}

	private static final byte[] longToByteArray(long value) {
		byte[] result = new byte[] {
				(byte)(value >>> 56),
				(byte)(value >>> 48),
				(byte)(value >>> 40),
				(byte)(value >>> 32),
				(byte)(value >>> 24),
				(byte)(value >>> 16),
				(byte)(value >>> 8),
				(byte)value};

		if ((result[0] + result[1] + result[2] + result[3]) == 0) {
			return new byte[] {
					(byte)(value >>> 24),
					(byte)(value >>> 16),
					(byte)(value >>> 8),
					(byte)value};
		}

		return result;
	}

	private static byte[] encodeEvent(IEventFields[] eventFields, BeanWrapper<? extends AbstractGenericEvent> beanWrapper) throws IllegalArgumentException {

		long result = 0;

		for (IEventFields eventField : eventFields) {
			FieldDefinition fieldDefinition = eventField.getFieldDefinition();

			if (fieldDefinition.getField() == null) {
				result = ValueEncoder
						.writeValueOnPosition(eventField.getValue(), fieldDefinition.getPosition(), result);
			} else {

				Object propertyResult = beanWrapper.getBeanAttribute(fieldDefinition.getField());

				if (propertyResult == null) {
					throw new IllegalArgumentException("Unable to obtain property with name '"
							+ fieldDefinition.getField() + "' in " + beanWrapper.getBeanWrapperContent().getClass().getName().toString());
				}

				result = ValueEncoder.writeValueOnPosition(((Integer) propertyResult).longValue(), fieldDefinition.getPosition(), result);
			}
		}
		
		return longToByteArray(result);
	}
}