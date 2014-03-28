package sk.seges.acris.player.client.objects;


import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class Tooltip extends AnimationObject {

	private boolean show = false;
//	private sk.seges.acris.component.Tooltip tooltip;
	
	public Tooltip(CacheMap cacheMap) {
		this(DEFAULT_OBJECT_SPEED, cacheMap);
	}
	
	public Tooltip(int speed, CacheMap cacheMap) {
		super(null, speed, cacheMap);
//		tooltip = new sk.seges.acris.component.Tooltip();
//		setElement(tooltip.getElement());
//		setPosition(Window.getClientWidth() / 2,  Window.getClientHeight() / 2);
//		DOM.setStyleAttribute(getElement(), "position", "absolute");
	}
	
	private void showTooltip() {
		if (!show) {
			show = true;
//			tooltip.show(Window.getClientWidth() / 2,  Window.getClientHeight() / 2);
		}
	}
	
}