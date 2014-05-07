package sk.seges.acris.recorder.client;

import com.google.gwt.core.client.EntryPoint;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class RecordingAction implements EntryPoint {

	private BatchRecorder recorder = new BatchRecorder(new ElementXpathCache(30));

	public void onModuleLoad() {
		recorder.startRecording();
	}
}