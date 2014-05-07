package sk.seges.acris.recorder.client.recorder;

import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.recorder.support.RecorderMode;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;


public class BatchRecorder extends Recorder {

	public BatchRecorder(ElementXpathCache elementXpathCache) {
		super(elementXpathCache, RecorderMode.BATCH);
	}
}