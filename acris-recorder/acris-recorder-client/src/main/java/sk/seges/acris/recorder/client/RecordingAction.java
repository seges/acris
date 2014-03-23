package sk.seges.acris.recorder.client;

import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.ui.RecorderUI;

import com.google.gwt.core.client.EntryPoint;

public class RecordingAction implements EntryPoint {

	private BatchRecorder recorder = new BatchRecorder();

	public void onModuleLoad() {
		recorder.startRecording();
	}
}