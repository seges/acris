package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.players.Layers;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class MouseEventExecutor extends AnimationObject {

	public MouseEventExecutor(int duration, CacheMap cacheMap) {
		super(duration, cacheMap);
		CursorImages cursorImages = GWT.create(CursorImages.class);
		setWidget(cursorImages.mouse().createImage());
		DOM.setStyleAttribute(getWidget().getElement(), "position", "absolute");
		DOM.setStyleAttribute(getWidget().getElement(), "zIndex", Layers.CURSOR_POSITION);
		setPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
	}
}