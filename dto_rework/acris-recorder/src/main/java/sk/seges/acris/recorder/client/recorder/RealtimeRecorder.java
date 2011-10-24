package sk.seges.acris.recorder.client.recorder;

import sk.seges.acris.recorder.client.recorder.support.Recorder;
import sk.seges.acris.recorder.client.recorder.support.RecorderMode;

public class RealtimeRecorder extends Recorder {

	public RealtimeRecorder() {
		super(RecorderMode.REALTIME);
	}	
}