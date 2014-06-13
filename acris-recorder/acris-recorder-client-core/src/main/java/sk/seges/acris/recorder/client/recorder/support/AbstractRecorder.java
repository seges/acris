package sk.seges.acris.recorder.client.recorder.support;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.event.KeyboardEvent;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.listener.RecorderListener;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecorder {
	
	private final NativePreviewHandler recordHandler;
    protected final ElementXpathCache elementXpathCache;

	private HandlerRegistration handlerRegistration;
	
	private List<RecorderListener> recorderListeners = new ArrayList<RecorderListener>();

	private RecorderLevel recorderLevel = RecorderLevel.ALL;

	public AbstractRecorder(ElementXpathCache elementXpathCache) {
        this.elementXpathCache = elementXpathCache;
		this.recordHandler = contructRecorder();
	}

	public void setRecorderLevel(RecorderLevel recorderLevel) {
		this.recorderLevel = recorderLevel;
	}

	void fireListeners(AbstractGenericEvent event) {
        GWT.log(event.toString(true, true));
        for (RecorderListener listener : recorderListeners) {
			listener.eventRecorded(event);
		}
	}

    protected native void removeHandlers(Element element) /*-{
        //TODO if there was any oncut/onpaste previously, this removes it. Only remove our handlers or get back
        //TODO previous handlers
        element.oncut = null;
        element.onpaste = null;
    }-*/;

    protected native void addOnChangeHandler(Element element, String value, int start, int end, int type) /*-{
        var temp = this;  // hack to hold on to 'this' reference
        var previousInput = element.oninput;
        element.oninput = function() {
            element.oninput = previousInput;
            temp.@sk.seges.acris.recorder.client.recorder.support.AbstractRecorder::handleChange(Lcom/google/gwt/dom/client/Element;Ljava/lang/String;III)(element, value, start, end, type);
            return false;
        }
    }-*/;

    protected native void addCutPasteHandler(Element element) /*-{
        var temp = this;  // hack to hold on to 'this' reference
        element.oncut = $entry(function() {
            temp.@sk.seges.acris.recorder.client.recorder.support.AbstractRecorder::handleCut(Lcom/google/gwt/dom/client/Element;)(element);
            return false;
        });
        element.onpaste = $entry(function() {
            temp.@sk.seges.acris.recorder.client.recorder.support.AbstractRecorder::handlePaste(Lcom/google/gwt/dom/client/Element;)(element);
            return false;
        });
    }-*/;

    private String getValue(Element element) {
        return DOM.getElementProperty((com.google.gwt.user.client.Element) element, "value");
    }

    @SuppressWarnings("unused")
    private void handleChange(Element element, String value, int start, int end, int type) {
        String newValue = getValue(element);

        ClipboardEvent clipboardEvent = new ClipboardEvent((com.google.gwt.user.client.Element) element, type, elementXpathCache);
        clipboardEvent.setSelectionStart(start);
        clipboardEvent.setSelectionEnd(end);
        if (type == ClipboardEvent.PASTE_EVENT_TYPE) {
            clipboardEvent.setClipboardText(newValue.substring(start, newValue.length() - value.length() + end));
        }
        fireListeners(clipboardEvent);
    }

    public static native JsArrayNumber getSelection(com.google.gwt.dom.client.Element el) /*-{
        var start = 0, end = 0, normalizedValue, range,
            textInputRange, len, endRange;

        if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
            start = el.selectionStart;
            end = el.selectionEnd;
        } else {
            range = document.selection.createRange();

            if (range && range.parentElement() == el) {
                len = el.value.length;
                normalizedValue = el.value.replace(/\r\n/g, "\n");

                // Create a working TextRange that lives only in the input
                textInputRange = el.createTextRange();
                textInputRange.moveToBookmark(range.getBookmark());

                // Check if the start and end of the selection are at the very end
                // of the input, since moveStart/moveEnd doesn't return what we want
                // in those cases
                endRange = el.createTextRange();
                endRange.collapse(false);

                if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
                    start = end = len;
                } else {
                    start = -textInputRange.moveStart("character", -len);
                    start += normalizedValue.slice(0, start).split("\n").length - 1;

                    if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
                        end = len;
                    } else {
                        end = -textInputRange.moveEnd("character", -len);
                        end += normalizedValue.slice(0, end).split("\n").length - 1;
                    }
                }
            }
        }
        return [ start, end] ;
    }-*/;

    @SuppressWarnings("unused")
    private void handleCut(Element element) {
        JsArrayNumber selection = getSelection(element);
        int start = (int) selection.get(0);
        int end = (int) selection.get(1);
        addOnChangeHandler(element, getValue(element), start, end, ClipboardEvent.CUT_EVENT_TYPE);
    }

    @SuppressWarnings("unused")
    private void handlePaste(Element element) {
        JsArrayNumber selection = getSelection(element);
        int start = (int) selection.get(0);
        int end = (int) selection.get(1);
        addOnChangeHandler(element, getValue(element), start, end, ClipboardEvent.PASTE_EVENT_TYPE);
    }

    private Element activeElement;

    //Not all browsers are supported, but majority supports active element
    //On the not supported browsers cut/paste functionality won't probably work
    private native Element getActiveElement() /*-{
        return $doc.activeElement;
    }-*/;

    private NativePreviewHandler contructRecorder() {
		NativePreviewHandler nativePreviewHandler = new NativePreviewHandler(){
		    public void onPreviewNativeEvent(NativePreviewEvent event) {
                Event gwtevent = Event.as(event.getNativeEvent());

                int type = DOM.eventGetType(gwtevent);

                //copy, cut, paste events are not processed - should be handled manually
                //TODO handle ctrl + v, ctrl + x actions - see change of the value before paste, cut
                //and after and store the diff
                //also handle paste, cut actions through context menu
                //handling onpaste event: http://help.dottoro.com/ljuimtmq.php

                if (recorderLevel.isRecordable(type)) {

                    Element currentActiveElement = getActiveElement();

                    if (currentActiveElement != null && (activeElement == null || !activeElement.equals(currentActiveElement))) {
                        //other element was focused, so handle onpaste events
                        if (activeElement != null) {
                            removeHandlers(activeElement);
                        }

                        if (!currentActiveElement.getNodeName().toLowerCase().equals("body")) {
                            addCutPasteHandler(currentActiveElement);
                        }
                        activeElement = currentActiveElement;
                    }

                    if (MouseEvent.isCorrectEvent(gwtevent)) {
                        MouseEvent mouseEvent = new MouseEvent(elementXpathCache, gwtevent);
                        fireListeners(mouseEvent);
                    } else if (KeyboardEvent.isCorrectEvent(gwtevent)) {
                        KeyboardEvent keyboardEvent = new KeyboardEvent(elementXpathCache, gwtevent);
                        fireListeners(keyboardEvent);
                    } else if (HtmlEvent.isCorrectEvent(gwtevent)) {
                        HtmlEvent htmlEvent = new HtmlEvent(elementXpathCache, gwtevent);
                        fireListeners(htmlEvent);
                    }
                }
		    }
		};

		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
		   		stopRecording();
			}
		});

		return nativePreviewHandler;
	}
	
	public void stopRecording() {

		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
		handlerRegistration = null;
	}

	public boolean isRecording() {
		return (handlerRegistration != null);
	}
	
	public void startRecording() {
		handlerRegistration = Event.addNativePreviewHandler(recordHandler);
	}
	
	public void addRecordListener(RecorderListener recorderListener) {
		recorderListeners.add(recorderListener);
	}

	public void removeRecordListener(RecorderListener recorderListener) {
		recorderListeners.remove(recorderListener);
	}
}