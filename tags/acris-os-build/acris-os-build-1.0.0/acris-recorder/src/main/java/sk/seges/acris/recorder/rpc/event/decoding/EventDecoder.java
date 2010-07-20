package sk.seges.acris.recorder.rpc.event.decoding;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import sk.seges.acris.recorder.rpc.event.EventType;
import sk.seges.acris.recorder.rpc.event.HtmlEvent;
import sk.seges.acris.recorder.rpc.event.IRecordableEvent;
import sk.seges.acris.recorder.rpc.event.KeyboardEvent;
import sk.seges.acris.recorder.rpc.event.MouseEvent;
import sk.seges.acris.recorder.rpc.event.fields.EHtmlEventFields;
import sk.seges.acris.recorder.rpc.event.fields.EKeyboardEventFields;
import sk.seges.acris.recorder.rpc.event.fields.EMouseEventFields;
import sk.seges.acris.recorder.rpc.event.fields.FieldDefinition;
import sk.seges.acris.recorder.rpc.event.fields.IEventFields;
import sk.seges.acris.recorder.rpc.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.rpc.transfer.StringMapper;

public class EventDecoder {
	public static AbstractGenericEvent decodeEvent(int[] event, StringMapper stringMapper)
			throws IllegalArgumentException {
		EventType eventType = getEventType(event[0]);

		AbstractGenericEvent abstractGenericEvent = null;

		if (eventType.equals(EventType.HtmlEvent)) {
			abstractGenericEvent = new HtmlEvent();
			abstractGenericEvent = decodeEvent(abstractGenericEvent, EHtmlEventFields.values(), HtmlEvent.class, event,
					stringMapper);
		} else if (eventType.equals(EventType.KeyboardEvent)) {
			abstractGenericEvent = new KeyboardEvent();
			abstractGenericEvent = decodeEvent(abstractGenericEvent, EKeyboardEventFields.values(),
					KeyboardEvent.class, event, stringMapper);
		} else if (eventType.equals(EventType.MouseEvent)) {
			abstractGenericEvent = new MouseEvent();
			abstractGenericEvent = decodeEvent(abstractGenericEvent, EMouseEventFields.values(), MouseEvent.class,
					event, stringMapper);
		}

		return abstractGenericEvent;
	}

	public static AbstractGenericEvent decodeEvent(AbstractGenericEvent abstractGenericEvent,
			IEventFields[] eventFields, Class<? extends AbstractGenericEvent> clazz, int[] event,
			StringMapper stringMapper) throws IllegalArgumentException {

		for (IEventFields eventField : eventFields) {
			FieldDefinition fieldDefinition = eventField.getFieldDefinition();

			if (fieldDefinition.getField() == null) {
				// Do nothing
			} else {
				int result = ValueDecoder.readValueFromPosition(fieldDefinition.getPosition(), event, fieldDefinition
						.getLength());

				BeanInfo beanInfo = null;

				try {
					beanInfo = Introspector.getBeanInfo(clazz);
				} catch (IntrospectionException e) {
					throw new IllegalArgumentException("Unable to find bean for class " + clazz.toString());
				}

				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				boolean propertyFound = false;

				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if (fieldDefinition.getField().equals(propertyDescriptor.getName())) {
						try {
							if (eventField.isStringMapper()) {
								propertyDescriptor.getWriteMethod().invoke(abstractGenericEvent,
										new Object[] { stringMapper.get(result) });
							} else {
								propertyDescriptor.getWriteMethod().invoke(abstractGenericEvent,
										new Object[] { result });
							}
							propertyFound = true;
							break;
						} catch (Exception ex) {
							throw new IllegalArgumentException("Unable to write property with name '"
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

		return abstractGenericEvent;
	}

	public static EventType getEventType(int event) {
		int[] eventList = new int[1];
		eventList[0] = event;
		int type = ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_EVENT_TYPE_SHIFT, eventList, 1);

		EventType eventType = EventType.getEvent(type);

		if (eventType != null) {
			return eventType;
		}

		int typeNext = ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_EVENT_TYPE_SHIFT - 1, eventList, 1);
		int resultType = type * 2 + typeNext;

		eventType = EventType.getEvent(resultType);

		if (eventType != null) {
			return eventType;
		}

		throw new IllegalArgumentException("Unable to resolve event type");
	};

	public static int getEventLength(int event) {
		int[] eventList = new int[1];
		eventList[0] = event;
		return ValueDecoder.readValueFromPosition(IRecordableEvent.ENCODE_LENGTH_SHIFT, eventList, 1) + 1;
	}
}