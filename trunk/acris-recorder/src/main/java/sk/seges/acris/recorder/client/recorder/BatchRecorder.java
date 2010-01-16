package sk.seges.acris.recorder.client.recorder;

import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.recorder.support.RecorderMode;


public class BatchRecorder extends Recorder {

	public BatchRecorder() {
		super(RecorderMode.BATCH);
	}
}