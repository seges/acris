/**
 * 
 */
package sk.seges.acris.widget.client.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gwt.gen2.table.client.CellEditor;
import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.Widget;

/**
 * Specifies column properties, e.g. filtering.
 * 
 * @author ladislav.gazo
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecColumn {
	/** Used to set explicitly field if name of the method is not the field name.*/
	String field() default "";
	Class<? extends Widget> filterWidgetType() default Widget.class;
	String filterOperation() default "";
	/** @return array of messages classes, which are constructor parameters for {@link sk.seges.acris.i18n.DynamicTranslator} */
	Class<? extends ConstantsWithLookup>[] messagesClasses() default ConstantsWithLookup.class;
	@SuppressWarnings("unchecked")
	Class<? extends CellEditor> editor() default CellEditor.class;
	@SuppressWarnings("unchecked")
	Class<? extends CellRenderer> renderer() default CellRenderer.class;
}
