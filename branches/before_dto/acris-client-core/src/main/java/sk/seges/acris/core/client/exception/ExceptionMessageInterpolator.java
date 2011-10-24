/**
 * 
 */
package sk.seges.acris.core.client.exception;

/**
 * "Translator" between exception and "user readable" message. The message is
 * sent to the client to be e.g. displayed in the dialog by exception message
 * handler.
 * 
 * @author ladislav.gazo
 */
public interface ExceptionMessageInterpolator<T extends Throwable> {
	Class<T> getAppliedClass();

	String interpolate(T cause);
}
