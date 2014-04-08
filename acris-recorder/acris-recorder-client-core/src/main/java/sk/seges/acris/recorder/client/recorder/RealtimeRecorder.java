package sk.seges.acris.recorder.client.recorder;

import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.recorder.support.RecorderMode;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class RealtimeRecorder extends Recorder {

	public RealtimeRecorder(CacheMap cacheMap) {
		super(cacheMap, RecorderMode.REALTIME);
	}	
}