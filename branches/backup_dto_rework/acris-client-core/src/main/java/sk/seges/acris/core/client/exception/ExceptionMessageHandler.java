/**
 * 
 */
package sk.seges.acris.core.client.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for handling exceptions in a way presentable to the client.
 * Holds a map of interpolators (see
 * {@link #register(ExceptionMessageInterpolator)} ) assigned to particular
 * exceptions.
 * 
 * @see ExceptionMessageInterpolator
 * 
 * @author ladislav.gazo
 */
public class ExceptionMessageHandler {
	private final Map<Class<? extends Throwable>, ExceptionMessageInterpolator<? extends Throwable>> handlers = new HashMap<Class<? extends Throwable>, ExceptionMessageInterpolator<? extends Throwable>>();

	@SuppressWarnings("unchecked")
	public String handle(Throwable cause) {
		ExceptionMessageInterpolator interpolator = handlers.get(cause.getClass());
		if (interpolator != null) {
			return interpolator.interpolate(cause);
		}
		return null;
	}

	public void register(ExceptionMessageInterpolator<? extends Throwable> interpolator) {
		handlers.put(interpolator.getAppliedClass(), interpolator);
	}
}
