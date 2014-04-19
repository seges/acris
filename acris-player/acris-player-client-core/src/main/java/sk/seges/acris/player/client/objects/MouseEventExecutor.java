package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.utils.InputHolder;
import sk.seges.acris.player.client.objects.utils.SelectionUtils;
import sk.seges.acris.player.client.players.Layers;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class MouseEventExecutor extends AnimationObject {

    private InputHolder inputHolder;
    private final boolean viewMode;

	public MouseEventExecutor(boolean viewMode, int duration, CacheMap cacheMap, InputHolder inputHolder) {
		super(duration, cacheMap);
        this.viewMode = viewMode;
		CursorImages cursorImages = GWT.create(CursorImages.class);
		setWidget(cursorImages.mouse().createImage());
		DOM.setStyleAttribute(getWidget().getElement(), "position", "fixed");
		DOM.setStyleAttribute(getWidget().getElement(), "zIndex", Layers.CURSOR_POSITION);
		setPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
        this.inputHolder = inputHolder;
	}

    @Override
    public void runAction(EventProperties cursorProperties, final CompleteHandler completeHandler) {
        if (cursorProperties.getEvent() == null || !(cursorProperties.getEvent() instanceof MouseEvent)) {
            super.runAction(cursorProperties, completeHandler);
            return;
        }

        MouseEvent mouseEvent = (MouseEvent)cursorProperties.getEvent();

        cursorProperties.getEvent().fireEvent();

        switch (cursorProperties.getEvent().getTypeInt()) {
            case MouseEvent.MOUSE_CLICK_TYPE:

                GQuery.$(mouseEvent.getElement()).click();
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_DOUBLE_CLICK_TYPE:
                //Edit or select text
                if (viewMode) {
                    //select text
                    SelectionUtils selectionUtils = new SelectionUtils();
                    JsArrayNumber selection = selectionUtils.getWordAtPoint(mouseEvent.getElement(), mouseEvent.getClientX(), mouseEvent.getClientY());
                    //TODO if selection is null or empty
                    selectionUtils.selectText(mouseEvent.getElement(), (int) selection.get(0), (int) selection.get(1));
                } else {
                    //edit text
                    GQuery.$(mouseEvent.getElement()).dblclick();
                }
                //GQuery.$(mouseEvent.getElement()).
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_DOWN_TYPE:

                inputHolder.setMouseButton(mouseEvent.getButton(), true);

                if (mouseEvent.getButton() == Event.BUTTON_LEFT) {
                    //clear the focus after down, and set it to the element - if focusable, if not, body will have a focus
                    if (mouseEvent.getElement() != null) {
                        inputHolder.blur();
                        inputHolder.focus(mouseEvent.getElement(), mouseEvent.getRelatedTargetXpath());
                    }
                }
                //other buttons are ignored because action is taken on up event, online down event is different

                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_UP_TYPE:
                inputHolder.setMouseButton(mouseEvent.getButton(), false);
                if (mouseEvent.getButton() == Event.BUTTON_LEFT) {
                    //TODO handle d'n'd
                    GQuery.$(mouseEvent.getElement()).mouseup();
                } else {
                    //TODO show notification - user opens context menu, but we cannot reopen it using javascript
//                    GQuery.$(mouseEvent.getElement()).trigger(Event.ONCONTEXTMENU);

                }
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_MOVE_TYPE:

                GQuery.$(mouseEvent.getElement()).mousemove();
                super.runAction(cursorProperties, completeHandler);
                //completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_WHEEL_TYPE:
                GQuery.$(GQuery.window).scrollTo(mouseEvent.getClientX(), mouseEvent.getClientY());
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_OVER_TYPE:
                GQuery.$(mouseEvent.getElement()).mouseover();
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_OUT_TYPE:
                GQuery.$(mouseEvent.getElement()).mouseout();
                completeHandler.onComplete();
                return;
        }

        super.runAction(cursorProperties, completeHandler);
    }
}