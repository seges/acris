package sk.seges.acris.json.client.samples;

import java.util.Collection;
import java.util.HashSet;

import org.gwttime.time.DateTime;

import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.JsonizerBuilder;
import sk.seges.acris.json.client.samples.data.FooSampler;
import sk.seges.acris.json.client.samples.data.SampleData;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SampleDataShowcase implements EntryPoint {

	private VerticalPanel vp;

	private WritePanel targetCodePanel;

	private EditableJsonWritePanel editableJsonWritePanel;

	@Override
	public void onModuleLoad() {

		vp = new VerticalPanel();
		RootPanel.get().add(vp);

		SampleImages images = GWT.create(SampleImages.class);
		PushButton imageButton = new PushButton(new Image(images.arrow()));
		imageButton.setWidth("50px");
		DOM.setStyleAttribute(imageButton.getElement(), "marginLeft", "200px");
		DOM.setStyleAttribute(imageButton.getElement(), "marginTop", "30px");
		DOM.setStyleAttribute(imageButton.getElement(), "marginBottom", "30px");
		imageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				jsonize();
			}

		});
		RootPanel.get().add(imageButton);

		RootPanel.get().add(targetCodePanel = createCodePanel());

		serialize();
		jsonize();
	}

	protected void serialize() {

		SampleData sampleData = new SampleData();
		sampleData.setData("text");

		FooSampler fooSampler = new FooSampler();
		fooSampler.setValue(new DateTime());

		Collection<FooSampler> samples = new HashSet<FooSampler>();
		samples.add(fooSampler);
		
		sampleData.setFooSampler(fooSampler);
		sampleData.setSamples(samples);

		SampleDataSerializer sds = new SampleDataSerializer();

		String jsonString = sds.serialize(sampleData);

		vp.clear();
		editableJsonWritePanel = new EditableJsonWritePanel();
		editableJsonWritePanel.write("JSON representation");
		editableJsonWritePanel.writep("(You can modify the ").writeb("blue").writelnp(" values as you want)");
		editableJsonWritePanel.setJson(jsonString);
		vp.add(editableJsonWritePanel);
	}

	protected void jsonize() {
		JSONValue value = JSONParser.parse(editableJsonWritePanel.getJson());

		JsonizerBuilder jsonizerBuilder = new JsonizerBuilder();
		
		IJsonizer jsonnizer = jsonizerBuilder.create();
		SampleData data = jsonnizer.fromJson(value, SampleData.class);

		targetCodePanel.setWidgetText("data", data.getData());
		targetCodePanel.setWidgetText("value", data.getFooSampler().getValue().toString());
	}

	protected WritePanel createCodePanel() {
		WritePanel wp = new WritePanel();
		wp.writelng("@JsonObject");
		wp.writep("public class").writeln(" SampleData { ");
		wp.indent();
		wp.writeln(" ");
		wp.writelng("@Field");
		wp.writep("private ").write("String data = ").writeb("\"").addLabelWidgetb("data").writeb("\"").writeln(";");
		wp.writeln(" ");
		wp.writelng("@Field");
		wp.writep("private ").write("FooSampler fooSampler = ").writeg("@JsonObject ").writep("public class ").writeln(
				"FooSampler { ");
		wp.indent();
		wp.writeln(" ");
		wp.writelng("@Field");
		wp.writeg("@DateTimePattern").write("(").writeb("\"y-M-d'T'H:m:s.SSSZ\"").writeln(")");
		wp.writep("private ").write("DateTime value = ").writeb("\"").addLabelWidgetb("value").writeb("\"")
				.writeln(";");
		wp.outdent();
		wp.writeln("}");
		wp.outdent();
		wp.writeln("}");

		return wp;
	}
}