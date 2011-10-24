package sk.seges.acris.widget.client.animation;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FadeAnimation extends Animation {

	public static int SPEED_DEFAULT = 400;
	public static int SPEED_FAST = 200;
	public static int SPEED_SLOW = 600;

	private Element element;
	private Style style;
	private AsyncCallback<?> callback;

	private double start;
	private double end;

	public FadeAnimation(Element element) {
		super();
		this.element = element;
		style = element.getStyle();
	}

	@Override
	protected void onUpdate(double progress) {
		double opacity = start + (progress * (end - start));
		style.setOpacity(opacity);
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		style.setOpacity(end);
		if (callback != null) {
			callback.onSuccess(null);
		}
	}

	public Element getElement() {
		return element;
	}

	public void fadeIn() {
		fadeIn(null);
	}

	public void fadeIn(AsyncCallback<?> callback) {
		fade(SPEED_DEFAULT, 1, callback);
	}

	public void fadeOut() {
		fadeOut(null);
	}

	public void fadeOut(AsyncCallback<?> callback) {
		fade(SPEED_DEFAULT, 0, callback);
	}

	public void fade(int duration, double to) {
		fade(duration, to, null);
	}

	public void fade(int duration, double to, AsyncCallback<?> callback) {
		String opacity = style.getOpacity();
		this.start = opacity.isEmpty() ? 1 : Double.valueOf(opacity);
		this.end = to;
		this.callback = callback;
		run(duration);
	}
}
