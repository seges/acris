package sk.seges.acris.mvp.client.view.core.forms;

import java.io.Serializable;

public interface LayoutForm<T extends Serializable, F> {

	void setColumns(int columns);

	void clearValues();

	void addFormRow(boolean autofill, F... items);

	void initialize();
}