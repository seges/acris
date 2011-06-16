package sk.seges.acris.widget.showcase;

import sk.seges.acris.widget.client.form.select.ComboBox;
import sk.seges.acris.widget.client.form.select.ComboBox.ComboBoxDataModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;


public class Showcase implements EntryPoint {

	@Override	
	public void onModuleLoad() {

		Grid grid = new Grid(8, 2);
		RootPanel.get().add(grid);
		
		int i = 0;
		grid.setWidget(i/2, i%2, new Label("Name:"));
		i++;
		grid.setWidget(i/2, i%2, (TextBox)GWT.create(TextBox.class));
		i++;

		grid.setWidget(i/2, i%2, new Label("E-mail:"));
		i++;
		grid.setWidget(i/2, i%2, (TextBox)GWT.create(TextBox.class));
		i++;

		grid.setWidget(i/2, i%2, new Label("Telephone:"));
		i++;
		grid.setWidget(i/2, i%2, (TextBox)GWT.create(TextBox.class));
		i++;

		grid.setWidget(i/2, i%2, new Label("Format:"));
		i++;

		ComboBox combo = GWT.create(ComboBox.class);
		
		ComboBoxDataModel model = new ComboBoxDataModel();
		combo.setModel(model);
		combo.display();
		
		combo.addItems("Plain text", "HTML", "I don't care");
		grid.setWidget(i/2, i%2, combo);
		i+=2;

		CheckBox cb = GWT.create(CheckBox.class);
		cb.setText("Important");
		grid.setWidget(i/2, i%2, cb);
		i+=2;

		cb = GWT.create(CheckBox.class);
		cb.setText("Request response");
		cb.setEnabled(false);
		cb.setValue(true);
		grid.setWidget(i/2, i%2, cb);
		i++;

		grid.setWidget(i/2, i%2, new Label("Message:"));
		i++;
		TextArea ta = (TextArea)GWT.create(TextArea.class);
		grid.setWidget(i/2, i%2, ta);
		i+=2;

		Button button = (Button)GWT.create(Button.class);
		button.setText("Send");
		grid.setWidget(i/2, i%2, button);
	}
}