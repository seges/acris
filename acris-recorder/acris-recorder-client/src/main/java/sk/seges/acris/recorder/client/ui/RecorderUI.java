package sk.seges.acris.recorder.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;

public class RecorderUI {
	private BatchRecorder recorder = new BatchRecorder();

	public void show() {

		Button button = new Button("Test");
		button.getElement().setId("nic");
		recorder.startRecording();
		RootPanel.get().add(button);
	}
}