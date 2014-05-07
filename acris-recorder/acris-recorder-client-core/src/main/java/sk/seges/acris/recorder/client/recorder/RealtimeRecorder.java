package sk.seges.acris.recorder.client.recorder;

import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.recorder.support.RecorderMode;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class RealtimeRecorder extends Recorder {

	public RealtimeRecorder(ElementXpathCache elementXpathCache) {
		super(elementXpathCache, RecorderMode.REALTIME);
	}	
}