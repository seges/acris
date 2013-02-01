package sk.seges.acris.recorder.rpc.event.encoding;

import sk.seges.acris.core.client.bean.BeanWrapper;
import sk.seges.acris.recorder.rpc.event.HtmlEvent;
import sk.seges.acris.recorder.rpc.event.HtmlEventBeanWrapper;
import sk.seges.acris.recorder.rpc.event.KeyboardEvent;
import sk.seges.acris.recorder.rpc.event.KeyboardEventBeanWrapper;
import sk.seges.acris.recorder.rpc.event.MouseEvent;
import sk.seges.acris.recorder.rpc.event.MouseEventBeanWrapper;
import sk.seges.acris.recorder.rpc.event.fields.EHtmlEventFields;
import sk.seges.acris.recorder.rpc.event.fields.EKeyboardEventFields;
import sk.seges.acris.recorder.rpc.event.fields.EMouseEventFields;
import sk.seges.acris.recorder.rpc.event.fields.FieldDefinition;
import sk.seges.acris.recorder.rpc.event.fields.IEventFields;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;

public class EventEncoder {
	public static int[] encodeEvent(AbstractGenericEvent event, StringMapper stringMapper)
			throws IllegalArgumentException {

		int[] result = null;

		if (event instanceof HtmlEvent) {
			result = new int[EHtmlEventFields.EVENT_LENGTH.getValue() + 1];
			HtmlEventBeanWrapper htmlEventBeanWrapper = new HtmlEventBeanWrapper();
			htmlEventBeanWrapper.setBeanWrapperContent((HtmlEvent) event);
			result = encodeEvent(result, EHtmlEventFields.values(), htmlEventBeanWrapper, stringMapper);
		} else if (event instanceof KeyboardEvent) {
			result = new int[EKeyboardEventFields.EVENT_LENGTH.getValue() + 1];
			KeyboardEventBeanWrapper keyboardEventBeanWrapper = new KeyboardEventBeanWrapper();
			keyboardEventBeanWrapper.setBeanWrapperContent((KeyboardEvent) event);
			result = encodeEvent(result, EKeyboardEventFields.values(), keyboardEventBeanWrapper, stringMapper);
		} else if (event instanceof MouseEvent) {
			result = new int[EMouseEventFields.EVENT_LENGTH.getValue() + 1];
			MouseEventBeanWrapper mouseEventBeanWrapper = new MouseEventBeanWrapper();
			mouseEventBeanWrapper.setBeanWrapperContent((MouseEvent)event);
			result = encodeEvent(result, EMouseEventFields.values(), mouseEventBeanWrapper, stringMapper);
		} else {
			throw new IllegalArgumentException("Unknown event type");
		}

		return result;
	}

	private static int[] encodeEvent(int[] result, IEventFields[] eventFields, BeanWrapper<? extends AbstractGenericEvent> beanWrapper,
			StringMapper stringMapper) throws IllegalArgumentException {
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

				if (eventField.isStringMapper()) {
					int position = stringMapper.getStringPosition(propertyResult.toString());
					result = ValueEncoder.writeValueOnPosition(position, fieldDefinition.getPosition(),
							result);
				} else {
					result = ValueEncoder.writeValueOnPosition((Integer) propertyResult, fieldDefinition
							.getPosition(), result);
				}

//				if (!propertyFound) {
//					throw new IllegalArgumentException("Unable to find property with name '"
//							+ fieldDefinition.getField() + "' in " + clazz.toString());
//				}
			}
		}
		
		return result;
	}
}