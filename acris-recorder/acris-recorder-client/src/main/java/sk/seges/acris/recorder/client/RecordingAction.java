package sk.seges.acris.recorder.client;

import com.google.gwt.core.client.EntryPoint;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class RecordingAction implements EntryPoint {

	private BatchRecorder recorder = new BatchRecorder(new CacheMap(30));

	public void onModuleLoad() {
		recorder.startRecording();
	}
}