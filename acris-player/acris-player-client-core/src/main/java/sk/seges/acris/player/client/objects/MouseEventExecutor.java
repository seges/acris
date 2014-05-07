package sk.seges.acris.player.client.objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import sk.seges.acris.player.client.command.SelectionCommand;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.player.client.objects.utils.InputHolder;
import sk.seges.acris.player.client.objects.utils.SelectionUtils;
import sk.seges.acris.player.client.players.Layers;
import sk.seges.acris.player.client.support.CssSupport;
import sk.seges.acris.recorder.client.event.MouseEvent;

public class MouseEventExecutor extends AnimationObject {

    private InputHolder inputHolder;
    private final boolean viewMode;
    private final CssSupport cssSupport = new CssSupport();

	public MouseEventExecutor(boolean viewMode, int speed, InputHolder inputHolder) {
		super(speed);
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

        switch (cursorProperties.getEvent().getTypeInt()) {
            case MouseEvent.MOUSE_CLICK_TYPE:
                //TODO click on the paragraph discards selection
                GQuery.$(mouseEvent.getElement()).click();
                //do not call fireEvent (native). It won't work properly
                //in some situations click is executed twice and leads to inappropriate
                //results - like on sitehero.sk - clicking on preview button
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_DOUBLE_CLICK_TYPE:
                //TODO tripple click selects whole text in the paragraph
                //Edit or select text
                if (viewMode) {
                    //select text
                    SelectionUtils selectionUtils = new SelectionUtils(mouseEvent.getElement());
                    SelectionUtils.SelectionHolder selection = selectionUtils.getWordAtPoint(mouseEvent.getAbsoluteClientX(), mouseEvent.getAbsoluteClientY());
                    //TODO if selection is null or empty
                    if (selection != null) {

                        String text;

                        if (mouseEvent.getElement().getNodeName().toLowerCase().equals(InputElement.TAG)) {
                            text = GQuery.$(mouseEvent.getElement()).val();
                        } else {
                            text = GQuery.$(selection.node).text();
                            selectionUtils = new SelectionUtils(selection.node);
                        }

                        if (text != null && text.length() > 0) {
                            int end = selection.end;
                            if (text.length() > end && text.charAt(end) == ' ') {
                                end++;
                            }

                            selectionUtils.selectText(selection.start, end);
                        }
                    }
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
                        //TODO sometimes placeholder text is still there - if window does not have focus
                        inputHolder.blur();
                        inputHolder.focus(mouseEvent.getElement(), mouseEvent.getRelatedTargetXpath());
                    }

                    SelectionUtils selectionUtils = new SelectionUtils(mouseEvent.getElement());
                    SelectionUtils.SelectionHolder selection = selectionUtils.getCharacterAtPoint(
                            mouseEvent.getAbsoluteClientX(), mouseEvent.getAbsoluteClientY());
                    inputHolder.setSelectionStart(selection);
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

                if (inputHolder.isButtonPressed(NativeEvent.BUTTON_LEFT)) {
                    SelectionUtils.SelectionHolder selection =
                            new SelectionUtils(mouseEvent.getElement()).getCharacterAtPoint(mouseEvent.getAbsoluteClientX(), mouseEvent.getAbsoluteClientY());
                    new SelectionCommand(inputHolder.getSelectionStart(), selection).execute();
                } else {
//                    GQuery.$(mouseEvent.getElement()).mousemove();
                }

                super.runAction(cursorProperties, completeHandler);
                //completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_WHEEL_TYPE:
                GQuery.$(GQuery.window).scrollTo(mouseEvent.getClientX(), mouseEvent.getClientY());
                completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_OVER_TYPE:
                GQuery.$(mouseEvent.getElement()).mouseover();
//                mouseEvent.fireEvent();;

                //applyHoverStyle(mouseEvent.getElement());
                cssSupport.applyPseudoClassStyle(mouseEvent.getElement(), CssSupport.PseudoClassType.HOVER);
                super.runAction(cursorProperties, completeHandler);
                //completeHandler.onComplete();
                return;
            case MouseEvent.MOUSE_OUT_TYPE:
//                mouseEvent.fireEvent();;
                GQuery.$(mouseEvent.getElement()).mouseout();
                cssSupport.restoreStyles(mouseEvent.getElement());
                super.runAction(cursorProperties, completeHandler);
                //completeHandler.onComplete();
                return;
        }

        super.runAction(cursorProperties, completeHandler);
    }
}