package sk.seges.acris.recorder.client;

import com.google.gwt.core.client.EntryPoint;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;

public class RecordingAction implements EntryPoint {

	private BatchRecorder recorder = new BatchRecorder();

	public void onModuleLoad() {
		recorder.startRecording();
	}
}