/**
 * 
 */
package sk.seges.corpis.dao.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

/**
 * Result transformer capable of recognizing chain of references (many-to-one)
 * to a field. In SQL world it is a path with multiple joins. It can of course
 * recognize simple field access.
 * 
 * @author eldzi
 */
public class ChainedFieldsTransformer implements ResultTransformer {
	private static final long serialVersionUID = 2901661735728153514L;

	private final Class<?> resultClass;
	private Setter[] setters;
	private PropertyAccessor propertyAccessor;
	private String[] directFields;

	public ChainedFieldsTransformer(Class<?> resultClass, String[] directFields) {
		if (resultClass == null)
			throw new IllegalArgumentException("resultClass cannot be null");
		this.resultClass = resultClass;
		propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] {
				PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
				PropertyAccessorFactory.getPropertyAccessor("field") });
		this.directFields = directFields;
	}

	private boolean isEmbedded(String alias) {
		return alias.contains(AbstractHibernateCRUD.EMBEDDED_FIELD_DELIM);
	}

	/**
	 * Replace all "-" {@link AbstractHibernateCRUD#EMBEDDED_FIELD_DELIM} with "_"
	 * {@link AbstractHibernateCRUD#ALIAS_CHAIN_DELIM} in alias.
	 * <p>
	 * Example:
	 * <br>
	 * <code>user_mailTemplate-toUser_mailTemplate-commonStuff-mailAddress.city</code> -->
	 * <code>user_mailTemplate_toUser_mailTemplate_commonStuff_mailAddress.city</code>
	 * <p>
	 * Special case is when alias ends with embedded delimiter. It's replaced
	 * with "." {@link AbstractHibernateCRUD#FIELD_DELIM}
	 * <p>
	 * Example: 
	 * <br>
	 * <code>user_mailTemplate-toUser_mailTemplate-subject</code> -->
	 * <code>user_mailTemplate_toUser_mailTemplate.subject</code>
	 * 
	 * @param source
	 * @return
	 */
	private String convertAliasWithEmbeddedFields(String source) {
		int embeddedFieldDelimLastIndex = source.lastIndexOf(AbstractHibernateCRUD.EMBEDDED_FIELD_DELIM);
		int fieldDelimLastIndex = source.lastIndexOf(AbstractHibernateCRUD.FIELD_DELIM);

		if (embeddedFieldDelimLastIndex > fieldDelimLastIndex) {
			source = source.substring(0, embeddedFieldDelimLastIndex) + AbstractHibernateCRUD.FIELD_DELIM
					+ source.substring(embeddedFieldDelimLastIndex + 1);
		}
		return source.replaceAll(AbstractHibernateCRUD.EMBEDDED_FIELD_DELIM, "\\"
				+ AbstractHibernateCRUD.ALIAS_CHAIN_DELIM);

	}
	
	private String removeAllColons(String source) {
		return source.replace(AbstractHibernateCRUD.COLON, "");
	}

	public Object transformTuple(final Object[] tuple, String[] aliases) {
		final Object resultInst;

		try {
			if (setters == null) {
				setters = new Setter[aliases.length];
				for (int i = 0; i < aliases.length; i++) {
					String alias = aliases[i];
					if (alias == null) {
						alias = directFields[i];
					}
					if (isEmbedded(alias)) {
						alias = convertAliasWithEmbeddedFields(alias);
					}
					alias = removeAllColons(alias);
					
					// go by chain and find the last setter from the alias
					new FieldChainResolver<Class<?>>() {

						@Override
						protected Class<?> initializeResult() {
							return resultClass;
						}

						@Override
						protected void executeForLastField(int i, String field, Class<?> result) {
							// finally get setter for last field
							setters[i] = propertyAccessor.getSetter(result, field);
						}

						@Override
						protected Class<?> executeForIntermediateField(String field, Class<?> result) {
							// get chained field class
							Getter getter = propertyAccessor.getGetter(result, field);
							return getter.getReturnType();
						}
					}.resolve(alias, i);
				}
			}

			resultInst = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				if (setters[i] != null) {
					String alias = aliases[i];
					if (alias == null) {
						alias = directFields[i];
					}
					if (isEmbedded(alias)) {
						alias = convertAliasWithEmbeddedFields(alias);
					}
					alias = removeAllColons(alias);
					
					
					// go by chain and reconstruct object graph for only
					// projected fields. Also create chained objects along the
					// path if necessary.
					new FieldChainResolver<Object>() {

						@Override
						protected Object initializeResult() throws InstantiationException, IllegalAccessException {
							return resultInst;
						}

						@Override
						protected void executeForLastField(int i, String field, Object result) {
							// set final field value
							setters[i].set(result, tuple[i], null);
						}

						@Override
						protected Object executeForIntermediateField(String field, Object result)
								throws InstantiationException, IllegalAccessException {
							// set intermediate object association
							Getter getter = propertyAccessor.getGetter(result.getClass(), field);
							Setter setter = propertyAccessor.getSetter(result.getClass(), field);
							Object associate = getter.get(result);
							if (associate == null) {
								// there is no instance yet in referenced field
								associate = getter.getReturnType().newInstance();
							}

							setter.set(result, associate, null);
							return associate;
						}
					}.resolve(alias, i);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}

		return resultInst;
	}

	@SuppressWarnings("rawtypes")
	public List transformList(List collection) {
		return collection;
	}

	/**
	 * Emulates closure with multiple entrypoints/method overrides. Iterates
	 * over alias representing chain of fields and executes appropriate abstract
	 * methods.
	 * 
	 * It executes for not-last field executeForIntermediateField method that
	 * will usually prepare "environment" for the last field setup.
	 * 
	 * @author eldzi
	 * 
	 * @param <R>
	 */
	private static abstract class FieldChainResolver<R> {
		protected abstract R initializeResult() throws InstantiationException, IllegalAccessException;

		protected abstract R executeForIntermediateField(String field, R result) throws InstantiationException,
				IllegalAccessException;

		protected abstract void executeForLastField(int i, String field, R result);

		public void resolve(String alias, int i) throws InstantiationException, IllegalAccessException {
			if (alias != null) {
				R result = initializeResult();

				String lastField = alias;
				int fieldPartIndex = alias.indexOf(AbstractHibernateCRUD.FIELD_DELIM);
				if (fieldPartIndex != -1) {
					// example alias: user.birthplace.street.name
					// class meaning:
					// Order.this->User.user->Location.birthplace->Street.street->name
					String remaining = alias.substring(0, fieldPartIndex);
					int index = 0;

					while (index != -1) {
						int newIndex = remaining.indexOf(AbstractHibernateCRUD.ALIAS_CHAIN_DELIM, index + 1);
						if (newIndex == -1) {
							newIndex = remaining.length();
							lastField = remaining.substring(index, newIndex);
							index = -1;
						} else {
							lastField = remaining.substring(index, newIndex);
							index = newIndex + 1;
						}
						result = executeForIntermediateField(lastField, result);
					}

					lastField = alias.substring(fieldPartIndex + 1);
				}
				executeForLastField(i, lastField, result);
			}
		}
	}
}