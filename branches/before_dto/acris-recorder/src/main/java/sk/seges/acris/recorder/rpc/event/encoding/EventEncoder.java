package sk.seges.acris.recorder.rpc.event.encoding;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import sk.seges.acris.recorder.rpc.event.HtmlEvent;
import sk.seges.acris.recorder.rpc.event.KeyboardEvent;
import sk.seges.acris.recorder.rpc.event.MouseEvent;
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
			result = encodeEvent(result, EHtmlEventFields.values(), HtmlEvent.class, event, stringMapper);
		} else if (event instanceof KeyboardEvent) {
			result = new int[EKeyboardEventFields.EVENT_LENGTH.getValue() + 1];
			result = encodeEvent(result, EKeyboardEventFields.values(), KeyboardEvent.class, event, stringMapper);
		} else if (event instanceof MouseEvent) {
			result = new int[EMouseEventFields.EVENT_LENGTH.getValue() + 1];
			result = encodeEvent(result, EMouseEventFields.values(), MouseEvent.class, event, stringMapper);
		} else {
			throw new IllegalArgumentException("Unknown event type");
		}

		return result;
	}

	private static int[] encodeEvent(int[] result, IEventFields[] eventFields, Class<? extends AbstractGenericEvent> clazz,
			AbstractGenericEvent event, StringMapper stringMapper) throws IllegalArgumentException {
		for (IEventFields eventField : eventFields) {
			FieldDefinition fieldDefinition = eventField.getFieldDefinition();

			if (fieldDefinition.getField() == null) {
				result = ValueEncoder
						.writeValueOnPosition(eventField.getValue(), fieldDefinition.getPosition(), result);
			} else {

				BeanInfo beanInfo = null;

				try {
					beanInfo = Introspector.getBeanInfo(clazz);
				} catch (IntrospectionException e) {
					throw new IllegalArgumentException("Unable to find bean for class '" + clazz.toString());
				}

				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				boolean propertyFound = false;

				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if (fieldDefinition.getField().equals(propertyDescriptor.getName())) {
						try {
							Object propertyResult = propertyDescriptor.getReadMethod().invoke(event, new Object[] {});

							if (propertyResult == null) {
								throw new IllegalArgumentException("Unable to obtain property with name '"
										+ fieldDefinition.getField() + "' in " + clazz.toString());
							}

							if (eventField.isStringMapper()) {
								int position = stringMapper.getStringPosition(propertyResult.toString());
								result = ValueEncoder.writeValueOnPosition(position, fieldDefinition.getPosition(),
										result);
							} else {
								result = ValueEncoder.writeValueOnPosition((Integer) propertyResult, fieldDefinition
										.getPosition(), result);
							}

							propertyFound = true;
							break;
						} catch (Exception ex) {
							throw new IllegalArgumentException("Unable to find read property with name '"
									+ fieldDefinition.getField() + "' in " + clazz.toString());
						}
					}
				}

				if (!propertyFound) {
					throw new IllegalArgumentException("Unable to find property with name '"
							+ fieldDefinition.getField() + "' in " + clazz.toString());
				}
			}
		}
		
		return result;
	}
}