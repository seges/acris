package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class Cursor extends AnimationObject {

	public Cursor(int duration, CacheMap cacheMap) {
		super(duration, cacheMap);
		CursorImages cursorImages = GWT.create(CursorImages.class);
		setWidget(cursorImages.mouse().createImage());
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		setPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
	}
	
	public Cursor(CacheMap cahceMap) {
		this(REALTIME_OBJECT_SPEED, cahceMap);
	}
}