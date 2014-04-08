package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.action.MouseMoveAction;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.players.Layers;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class MouseEventExecutor extends AnimationObject {

	public MouseEventExecutor(int duration, CacheMap cacheMap) {
		super(duration, cacheMap);
		CursorImages cursorImages = GWT.create(CursorImages.class);
		setWidget(cursorImages.mouse().createImage());
		DOM.setStyleAttribute(getWidget().getElement(), "position", "fixed");
		DOM.setStyleAttribute(getWidget().getElement(), "zIndex", Layers.CURSOR_POSITION);
		setPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
	}

    @Override
    public void runAction(EventProperties cursorProperties, final CompleteHandler completeHandler) {
        if (cursorProperties.getEvent() == null || !(cursorProperties.getEvent() instanceof MouseEvent)) {
            super.runAction(cursorProperties, completeHandler);
            return;
        }

        MouseEvent mouseEvent = (MouseEvent)cursorProperties.getEvent();

        switch (cursorProperties.getEvent().getTypeInt()) {
            case MouseEvent.MOUSE_WHEEL_TYPE:
                cursorProperties.getEvent().fireEvent();
                GQuery.$(GQuery.window).scrollTo(mouseEvent.getClientX(), mouseEvent.getClientY());
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_OVER_TYPE:
                cursorProperties.getEvent().fireEvent();
                GQuery.$(mouseEvent.getElement()).mouseover();
                return;
            case MouseEvent.MOUSE_OUT_TYPE:
                cursorProperties.getEvent().fireEvent();
                GQuery.$(mouseEvent.getElement()).mouseleave();
                return;
        }

        super.runAction(cursorProperties, completeHandler);
    }
}