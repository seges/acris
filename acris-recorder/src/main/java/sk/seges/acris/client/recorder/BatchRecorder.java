package sk.seges.acris.client.recorder;

import sk.seges.acris.client.recorder.support.RecorderMode;
import sk.seges.acris.client.recorder.support.Recorder;


public class BatchRecorder extends Recorder {

	public BatchRecorder() {
		super(RecorderMode.BATCH);
	}
}