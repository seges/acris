package sk.seges.acris.recorder.client.ui;

import sk.seges.acris.recorder.client.recorder.BatchRecorder;

public class RecorderUI {
	private BatchRecorder recorder = new BatchRecorder();

	public void show() {
		//final HTML html = new HTML("Recording...");
//		final Timer time = new Timer() {
//			
//			@Override
//			public void run() {
//				html.setVisible(!html.isVisible());
//			}
//		};

//		ToggleButton toggleButton = new ToggleButton();
//		toggleButton.setText("Record...");
//		toggleButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (recorder.isRecording()) {
//					recorder.stopRecording();
//					time.cancel();
//					html.removeFromParent();
//				} else {
					recorder.startRecording();
//					time.schedule(500);
//				}
//			}
//		});
//		
//		RootPanel.get().add(toggleButton);
	}
}