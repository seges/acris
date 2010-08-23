package sk.seges.acris.binding.client.providers;

import sk.seges.acris.binding.client.providers.annotations.OneToMany;
import sk.seges.acris.binding.client.providers.support.AbstractBindingChangeHandlerAdapterProvider;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Extension of ListBoxAdapterProvider - enables bidirectional binding
 * 
 * @author eldzi
 * @author fat
 */
@OneToMany
public class ListBoxAutoAdapterProvider extends AbstractBindingChangeHandlerAdapterProvider<ListBox, Object> {
	private static final String PROPERTY_SELECTED_ITEM = "selectedItem";
	
	protected String getValue(ListBox list) {
		int index = list.getSelectedIndex();

		if (index == -1) {
			return null;
		}

		return list.getValue(index);
	}

	
//	protected void setValue(ListBox list, String value) {
//		assert list != null;
//
//		int count = list.getItemCount();
//
//		for (int i = 0; i < count; i++) {
//			if (list.getValue(i).equals(value.toString())) {
//				list.setSelectedIndex(i);
//				return;
//			}
//		}
//	}
// TODO: Toto je zle, treba neskvor pouzit zakomentovanu verziu, workaround pre hr oddelenie 
	protected void setValue(ListBox list, Object value) {
		int count = list.getItemCount();

		if (value == null) {
			list.setSelectedIndex(0);
			return;
		}
		
		for (int i = 0; i < count; i++) {
			if (list.getValue(i).equals(value.toString())) {
				list.setSelectedIndex(i);
				return;
			}
		}
	}

	@Override
	public Class<ListBox> getBindingWidgetClasses() {
		return ListBox.class;
	}

	@Override
	public boolean isSupportSuperclass() {
		return false;
	}
	
	@Override
	public String getBindingWidgetProperty() {
		return PROPERTY_SELECTED_ITEM;
	}
}