/**
 * 
 */
package sk.seges.acris.security.shared.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.client.rpc.IDataTransferObject;

/**
 * @author eldzi
 */
public class ClientSession<T> implements IDataTransferObject {

	private static final long serialVersionUID = 1909822586051716898L;

	private static final String SESSION_ID_ATTRIBUTE = "sessionId";

	private Map<String, PropertyHolder> session;
	private Map<String, Serializable> clientSession;

	public static class PropertyHolder {

		enum ValueType {
			BOOLEAN {
				@Override
				public Object getValue(PropertyHolder propertyHolder) {
					return propertyHolder.booleanValue;
				}

				@Override
				public ValueType setValue(PropertyHolder propertyHolder, Object value) {
					propertyHolder.booleanValue = (Boolean) value;
					return ValueType.BOOLEAN;
				}

				@Override
				public Class<?> appliesFor() {
					return Boolean.class;
				}
			},
			STRING {
				@Override
				public Object getValue(PropertyHolder propertyHolder) {
					return propertyHolder.booleanValue;
				}

				@Override
				public ValueType setValue(PropertyHolder propertyHolder, Object value) {
					propertyHolder.stringValue = (String) value;
					return ValueType.STRING;
				}

				@Override
				public Class<?> appliesFor() {
					return String.class;
				}
			},
			ARRAY {
				@Override
				public Object getValue(PropertyHolder propertyHolder) {
					return propertyHolder.arrayValue;
				}

				@Override
				public ValueType setValue(PropertyHolder propertyHolder, Object value) {
					propertyHolder.arrayValue = (SessionArrayHolder) value;
					return ValueType.ARRAY;
				}

				@Override
				public Class<?> appliesFor() {
					return SessionArrayHolder.class;
				}
			},
			ENUM {
				@Override
				public Object getValue(PropertyHolder propertyHolder) {
					return propertyHolder.enumValue;
				}

				@Override
				public ValueType setValue(PropertyHolder propertyHolder, Object value) {
					propertyHolder.enumValue = (Enum<?>) value;
					return ValueType.ENUM;
				}

				@Override
				public Class<?> appliesFor() {
					return Enum.class;
				}
			};

			public abstract Object getValue(PropertyHolder propertyHolder);
			public abstract ValueType setValue(PropertyHolder propertyHolder, Object value);
			public abstract Class<?> appliesFor();

			public static ValueType valueFor(Object obj) {
				for (ValueType valueType: ValueType.values()) {
					if (valueType.appliesFor().equals(obj.getClass())) {
						return valueType;
					}
				}

				if (obj.getClass().isEnum()) {
					return ValueType.ENUM;
				}

				throw new RuntimeException("Not supported class " + obj.getClass().getName());
			}
		}

		ValueType valueType;

		Boolean booleanValue;
		String stringValue;
		SessionArrayHolder arrayValue;
		Enum<?> enumValue;

		protected  PropertyHolder() {};

		public PropertyHolder(Object value) {
			setValue(value);
		}

		public void setValue(Object value) {
			this.valueType = ValueType.valueFor(value).setValue(this, value);
		}

		public Object getValue() {
			return valueType.getValue(this);
		}
	}

	public Map<String, PropertyHolder> getSession() {
		if (session == null) {
			session = new HashMap<String, PropertyHolder>();
		}
		return session;
	}

	private Map<String, Serializable> getClientSession() {
		if (clientSession == null) {
			clientSession = new HashMap<String, Serializable>();
		}
		return clientSession;
	}
	
	public void setSession(Map<String, PropertyHolder> session) {
		this.session = session;
	}

	public String getSessionId() {
		return (String) getSession().get(SESSION_ID_ATTRIBUTE).getValue();
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, new PropertyHolder(sessionId));
	}

	private T user;
	
	public T getUser() {
		return user;
	}

	public void setUser(T user) {
		this.user = user;
	}

	public void putSharedProperty(String key, String value) {
		getSession().put(key, new PropertyHolder(value));
	}

	public void putSharedProperty(String key, Boolean value) {
		getSession().put(key, new PropertyHolder(value));
	}

	public void putSharedProperty(String key, SessionArrayHolder value) {
		getSession().put(key, new PropertyHolder(value));
	}

	public void putSharedProperty(String key, Enum<?> value) {
		getSession().put(key, new PropertyHolder(value));
	}

	public void put(String key, Serializable value) {
		getClientSession().put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <S> S get(String key) {
		if (session == null && clientSession == null) {
			return null;
		}
		if (session != null && session.containsKey(key)) {
			return (S) session.get(key);
		}
		if (clientSession != null && clientSession.containsKey(key)) {
			return (S) clientSession.get(key);
		}
		
		return null;
	}

	public ClientSession<T> merge(ClientSession<T> value) {
		for(Entry<String, PropertyHolder> entry : value.getSession().entrySet()) {
			if(getSession().containsKey(entry.getKey())) {
				continue;
			}
			
			session.put(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public void clear() {
		getClientSession().clear();
		getSession().clear();
	}
}