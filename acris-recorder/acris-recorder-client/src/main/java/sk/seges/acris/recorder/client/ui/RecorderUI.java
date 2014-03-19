package sk.seges.acris.recorder.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import sk.seges.acris.recorder.client.recorder.BatchRecorder;

public class RecorderUI {
	private BatchRecorder recorder = new BatchRecorder();

	public void show() {

//		ScriptElement scriptElement = Document.get().createScriptElement();
//		scriptElement.setInnerHTML("var ipaddress;\n" +
//				"function setip(json){\n" +
//				"	ipaddress = json.ip;\n" +
//				"}");
//		RootPanel.get().getElement().appendChild(scriptElement);
//		RootPanel.get().getElement().appendChild(Document.get().createScriptElement("http://jsonip.appspot.com/?callback=setip"));

		final HTML html = new HTML("Recording...");
		final Timer time = new Timer() {

			@Override
			public void run() {
				html.setVisible(!html.isVisible());
			}
		};

		ToggleButton toggleButton = new ToggleButton();
		toggleButton.setText("Record...");
		toggleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (recorder.isRecording()) {
					recorder.stopRecording();
					time.cancel();
					html.removeFromParent();
				} else {
					recorder.startRecording();
					time.schedule(500);
				}
			}
		});

		RootPanel.get().add(toggleButton);
	}
}