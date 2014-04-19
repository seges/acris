package sk.seges.acris.player.client.objects;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.KeyboardEvent;

public class KeyboardEventExecutor extends EventExecutor {

	@Override
	public void runAction(EventProperties cursorProperties, CompleteHandler completeHandler) {
		if (!(cursorProperties.getEvent() instanceof KeyboardEvent)) {
			completeHandler.onComplete();
			return;
		}

		KeyboardEvent keyboardEvent = (KeyboardEvent)cursorProperties.getEvent();

		if (keyboardEvent.getRelatedTargetXpath() != null) {
			Element element = keyboardEvent.getElement();
			if (element != null) {
				element.focus();
			}

            //TODO ignore special combinations, like
            //TODO ctrl + c, ctrl + v, ctrl + x
			switch (keyboardEvent.getTypeInt()) {
				case 0:
//					GQuery.$(element).val(GQuery.$(element).val() + (char)keyboardEvent.getKeyCode());
					//TODO - handle charCode also
					break;
				case 1:	//keypress
                    GQuery el = GQuery.$(element);
                    el.val(el.val() + (char) keyboardEvent.getKeyCode());
					//TODO - handle charCode also
					break;
				case 2:
//					GQuery.$(element).val(GQuery.$(element).val() + (char)keyboardEvent.getKeyCode());
					//TODO - handle charCode also
					break;
			}
		} else {
			//TODO makes sense?
			GQuery.$().val(GQuery.$().val() + (char)keyboardEvent.getKeyCode());
			//TODO - handle charCode also
		}

		super.runAction(cursorProperties, completeHandler);
	}
}