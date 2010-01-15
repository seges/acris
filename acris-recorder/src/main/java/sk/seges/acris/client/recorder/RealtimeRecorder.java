package sk.seges.acris.client.recorder;

import sk.seges.acris.client.recorder.support.RecorderMode;
import sk.seges.acris.client.recorder.support.Recorder;

public class RealtimeRecorder extends Recorder {

	public RealtimeRecorder() {
		super(RecorderMode.REALTIME);
	}	
}