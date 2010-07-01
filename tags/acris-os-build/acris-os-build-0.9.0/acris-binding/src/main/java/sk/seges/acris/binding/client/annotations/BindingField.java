package sk.seges.acris.binding.client.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.gwt.beansbinding.core.client.Converter;

/**
 * <p>Annotation used to specify binding on GWT widget. There are multiple 
 * types of binding, but always is used the same annotation and binding type
 * is determined based on binding widget and based on binding property (value).</p>
 * <p>Currently we are supporting 3 types of bindings:
 * <ul>
 * <li><b>One-to-One binding</b> - the most simplest type used to bind simple
 * value to the widget that can hold only simple value (e.g. {@link TextBox}, 
 * {@link CheckBox}).
 * <p>
 * Sample:
 * <pre>
 * {@code
 * @BindingField(User.NAME)
 * protected TextBox username = GWT.create(TextBox.class);
 * }</pre>
 * </p>
 * </li>
 * <li><b>One-to-Many binding</b> - more complex binding type used to bind object
 * with the list of objects represented in {@link ListBox} component.</li>
 * <p>
 * Sample:
 * <pre>
 * {@code
 * @BindingField(User.ROLE + "." + Role.NAME)
 * protected ListBox role = GWT.create(ListBox.class);
 * }</pre></p>
 * <p>In the sample above we bind role from User with ListBox which holds all
 * available roles. We are matching object through name attribute (in ListBox
 * are displayed role names - in <code>String</code> representation).
 * In order to have complete working example you have to also define data loader,
 * which provides data to the <code>ListBox</code>. By default is defined 
 * {@link EmptyLoaderCreator} which provides no data. You can define your own data
 * loader which can load data from database (using RPC) or load some predefined data.</p>
 * <pre>
 * {@code
 * @BindingField(User.ROLE + "." + Role.NAME)
 * @FieldSpecLoader(CustomDataLoader.class)
 * protected ListBox role = GWT.create(ListBox.class);
 * }</pre></p>
 * <p>
 * Now <code>CustomDataLoader</code> is called when ListBox is initialized. See 
 * {@link BindingSpecLoader} for more details.
 * </p><br/>
 * <li><b>Many-to-Many binding</b> - used to specify binding of {@link List} of values
 * to ListBox with multiselect functionality. Also should be used for binding to the
 * table, but currently it is only in experimental state</li>
 * </ul>
 * 
 * @author fat
 */
@Target( { ElementType.FIELD, ElementType.TYPE })
@Retention(RUNTIME)
public @interface BindingField {
	/**
	 * Required binding property to specify which property is bound with the
	 * bean
	 */
	public abstract String value();

	/**
	 * Optional default value when binding property is null. Default value is
	 * represented as String and in order to convert default value in another
	 * type of object you have to use {@link Converter}.
	 */
	public String defaultValue() default "null";

	/**
	 * Converter used to convert String representation of default value into
	 * the necessary object. Also can be used for localize text used by key 
	 * represented by defaultValue.
	 */
	public Class<?> converter() default Converter.class;
	
	public Class<?> validator() default Void.class;

	/**
	 * Not yet implemented. Supposed to create instance of the binding widget
	 * automatically.
	 * <pre>
	 * {@code
	 * @BindingField(..., autoIntialize=true)
	 * protected ListBox role;
	 * }</pre>
	 * will be automatically transformed into
	 * <pre>
	 * {@code
	 * @BindingField(..., autoIntialize=true)
	 * protected ListBox role = GWT.create(ListBox.class);
	 * }</pre>
	 */
	public boolean autoInitialize() default false;
}