/**
 * 
 */
package sk.seges.acris.binding.client.holder.validation;

/**
 * Wrapping widget representing highlight of a property-widget. Usually if you
 * want to decorate property-widget (widget representing bound property value)
 * you wrap the widget to separate wrapper widget. This widget is meant as
 * highlighted widget. There is a need to revert to original state when the
 * invalid constraint on the property is resolved. Highlighted widget has such
 * means for that.
 * 
 * @author eldzi
 */
public interface HighlightedWidget {
	/**
	 * Revert back to original state when the propert-widget was not wrapped to
	 * a highlighted widget.
	 */
	void removeHighlight();
}
