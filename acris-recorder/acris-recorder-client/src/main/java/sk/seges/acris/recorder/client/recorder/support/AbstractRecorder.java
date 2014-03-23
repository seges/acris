package sk.seges.acris.recorder.client.recorder.support;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.event.KeyboardEvent;
import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.acris.recorder.client.listener.RecorderListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecorder {
	
	private final NativePreviewHandler recordHandler;
	private HandlerRegistration handlerRegistration;
	
	private List<RecorderListener> recorderListeners = new ArrayList<RecorderListener>();

	private RecorderLevel recorderLevel = RecorderLevel.ALL;

	public AbstractRecorder() {
		this.recordHandler = contructRecorder();
	}

	public void setRecorderLevel(RecorderLevel recorderLevel) {
		this.recorderLevel = recorderLevel;
	}

	void fireListeners(AbstractGenericEvent event) {
		for (RecorderListener listener : recorderListeners) {
			listener.eventRecorded(event);
		}
	}
	
	private NativePreviewHandler contructRecorder() {
		NativePreviewHandler nativePreviewHandler = new NativePreviewHandler(){
		    public void onPreviewNativeEvent(NativePreviewEvent event) {
			Event gwtevent = Event.as(event.getNativeEvent());

			int type = DOM.eventGetType(gwtevent);

			if (recorderLevel.isRecordable(type)) {
				if (MouseEvent.isCorrectEvent(gwtevent)) {
					fireListeners(new MouseEvent(gwtevent));
				} else if (KeyboardEvent.isCorrectEvent(gwtevent)) {
					fireListeners(new KeyboardEvent(gwtevent));
				} else if (HtmlEvent.isCorrectEvent(gwtevent)) {
					fireListeners(new HtmlEvent(gwtevent));
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