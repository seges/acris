package sk.seges.acris.player.client.objects;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;
import sk.seges.acris.player.client.exception.ReplayException;
import sk.seges.acris.player.client.listener.CompleteHandler;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.event.KeyboardEvent;

/**
 * Created by PeterSimun on 18.4.2014.
 */
public class ClipboardExecutor extends EventExecutor  {

    @Override
    public void runAction(EventProperties cursorProperties, CompleteHandler completeHandler) {
        if (!(cursorProperties.getEvent() instanceof ClipboardEvent)) {
            completeHandler.onComplete();
            return;
        }

        ClipboardEvent clipboardEvent = (ClipboardEvent)cursorProperties.getEvent();

        Element element = clipboardEvent.getElement();

        switch (clipboardEvent.getTypeInt()) {
            case ClipboardEvent.CUT_EVENT_TYPE:
                GQuery el = GQuery.$(element);
                String val = el.val();
                el.val(val.substring(0, clipboardEvent.getSelectionStart()) + val.substring(clipboardEvent.getSelectionEnd()));
                break;
            case ClipboardEvent.PASTE_EVENT_TYPE:
                el = GQuery.$(element);
                val = el.val();

                if (clipboardEvent.getSelectionEnd() > val.length()) {
                    throw new ReplayException("Unable to execute clipboard paste action on element " + clipboardEvent.getRelatedTargetXpath() + " on " +
                            "position: " + clipboardEvent.getSelectionStart() + " - " + clipboardEvent.getSelectionEnd(), clipboardEvent);
                }

                el.val(val.substring(0, clipboardEvent.getSelectionStart()) + clipboardEvent.getClipboardText() + val.substring(clipboardEvent.getSelectionEnd()));
                break;
            default:
                throw new RuntimeException("Unknown clipboard type: " + clipboardEvent.getTypeInt());
        }

        completeHandler.onComplete();
    }
}