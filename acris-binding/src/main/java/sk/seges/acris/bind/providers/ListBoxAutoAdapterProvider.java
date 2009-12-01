package sk.seges.acris.bind.providers;

import sk.seges.acris.bind.providers.annotations.OneToMany;
import sk.seges.acris.bind.providers.support.AbstractBindingChangeHandlerAdapterProvider;

import com.google.gwt.user.client.ui.ListBox;

/**
 * Extension of ListBoxAdapterProvider - enables bidirectional binding
 * 
 * @author eldzi
 * @author fat
 */
@OneToMany
public class ListBoxAutoAdapterProvider extends AbstractBindingChangeHandlerAdapterProvider<ListBox, Object> {

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

		for (int i = 0; i < count; i++) {
			if (value == null) {
				continue;
			}
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
}