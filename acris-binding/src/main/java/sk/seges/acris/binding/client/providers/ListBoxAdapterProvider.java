/**
 * 
 */
package sk.seges.acris.binding.client.providers;

import java.util.ArrayList;
import java.util.List;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;
import org.gwt.beansbinding.ui.client.impl.ListBindingManager;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author ladislav.gazo
 *
 */
public class ListBoxAdapterProvider extends org.gwt.beansbinding.ui.client.adapters.ListBoxAdapterProvider {
	private static int getEqualItemIndex(List<?> elements, Object item) {
		int i = 0;
		for (Object lbmi : elements) {
			Object toCompare;
			if (lbmi instanceof BeanWrapper<?>) {
				toCompare = ((BeanWrapper<?>) lbmi).getBeanWrapperContent();
			} else {
				toCompare = lbmi;
			}
			Object toCompareItem;
			if (item instanceof BeanWrapper<?>) {
				toCompareItem = ((BeanWrapper<?>) item).getBeanWrapperContent();
			} else {
				toCompareItem = item;
			}
			if (toCompare.equals(toCompareItem)) {
				return i;
			}
			i++;
		}

		return -1;
	}

	private static final String SELECTED_ITEM_P = "selectedItem".intern();
	private static final String SELECTED_ITEMS_P = "selectedItems".intern();
	private static final String SELECTED_ITEM_TEXT_P = "selectedItemText".intern();
	private static final String SELECTED_ITEMS_TEXT_P = "selectedItemsText".intern();
	private static final String SELECTED_ITEM_VALUE_P = "selectedItemValue".intern();
	private static final String SELECTED_ITEMS_VALUE_P = "selectedItemsValue".intern();

	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}
		return new Adapter((ListBox) source, property);
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return (type == ListBox.class) ? ListBoxAdapterProvider.Adapter.class : null;
	}

	public static final class Adapter extends BeanAdapterBase implements ChangeHandler {
		private ListBox list;
		private Object cachedElementOrElements;

		private HandlerRegistration changeHandlerReg;

		private Adapter(ListBox list, String property) {
			super(property);
			this.list = list;
		}

		public Object getSelectedItem() {
			return ListBoxAdapterProvider.getSelectedItem(list);
		}

		public void setSelectedItem(Object item) {
			ListBoxAdapterProvider.setSelectedItem(list, item);
			onChange(null);
		}

		public List<Object> getSelectedItems() {
			return ListBoxAdapterProvider.getSelectedItems(list);
		}

		public void setSelectedItems(List<Object> items) {
			ListBoxAdapterProvider.setSelectedItems(list, items);
			onChange(null);
		}

		public String getSelectedItemText() {
			return ListBoxAdapterProvider.getSelectedItemText(list);
		}

		public List<String> getSelectedItemsText() {
			return ListBoxAdapterProvider.getSelectedItemsText(list);
		}

		public String getSelectedItemValue() {
			return ListBoxAdapterProvider.getSelectedItemValue(list);
		}

		public List<String> getSelectedItemsValue() {
			return ListBoxAdapterProvider.getSelectedItemsValue(list);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStarted()
		 */
		@Override
		protected void listeningStarted() {
			if (property == SELECTED_ITEM_P) {
				cachedElementOrElements = getSelectedItem();
			} else if (property == SELECTED_ITEM_TEXT_P) {
				cachedElementOrElements = getSelectedItem();
			} else if (property == SELECTED_ITEM_VALUE_P) {
				cachedElementOrElements = getSelectedItemValue();
			} else if (property == SELECTED_ITEMS_P) {
				cachedElementOrElements = getSelectedItems();
			} else if (property == SELECTED_ITEMS_TEXT_P) {
				cachedElementOrElements = getSelectedItems();
			} else if (property == SELECTED_ITEMS_VALUE_P) {
				cachedElementOrElements = getSelectedItemsValue();
			}
			changeHandlerReg = list.addChangeHandler(this);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase#listeningStopped()
		 */
		@Override
		protected void listeningStopped() {
			if (changeHandlerReg != null) {
				changeHandlerReg.removeHandler();
				changeHandlerReg = null;
			}
			cachedElementOrElements = null;
		}

		public void onChange(ChangeEvent event) {
			Object oldElementOrElements = cachedElementOrElements;
			if (property == SELECTED_ITEM_P) {
				cachedElementOrElements = getSelectedItem();
			} else if (property == SELECTED_ITEM_TEXT_P) {
				cachedElementOrElements = getSelectedItem();
			} else if (property == SELECTED_ITEM_VALUE_P) {
				cachedElementOrElements = getSelectedItemValue();
			} else if (property == SELECTED_ITEMS_P) {
				cachedElementOrElements = getSelectedItems();
			} else if (property == SELECTED_ITEMS_TEXT_P) {
				cachedElementOrElements = getSelectedItems();
			} else if (property == SELECTED_ITEMS_VALUE_P) {
				cachedElementOrElements = getSelectedItemsValue();
			}
			firePropertyChange(oldElementOrElements, cachedElementOrElements);
		}
	}

	private static Object getSelectedItem(ListBox list) {
		assert list != null;

		int index = list.getSelectedIndex();

		Object model = list.getElement().getPropertyObject("model");
		if (model instanceof ListBindingManager) {
			ListBindingManager lbm = (ListBindingManager) model;
			if (index != -1) {
				return lbm.getElement(index);
			} else {
				return null;
			}
		} else {
			return index;
		}
	}

	private static void setSelectedItem(ListBox list, Object item) {
		assert list != null;

		Object model = list.getElement().getPropertyObject("model");
		if (model instanceof ListBindingManager) {
			ListBindingManager lbm = (ListBindingManager) model;
			list.setSelectedIndex(getEqualItemIndex(lbm.getElements(), item));
		} else {
			list.setSelectedIndex((Integer) item);
		}
	}

	private static List<Object> getSelectedItems(ListBox list) {
		assert list != null;

		List<Object> elements = new ArrayList<Object>();

		if (list.getSelectedIndex() == -1) {
			return elements;
		}

		Object model = list.getElement().getPropertyObject("model");
		if (model instanceof ListBindingManager) {
			ListBindingManager lbm = (ListBindingManager) model;
			for (int i = 0, n = list.getItemCount(); i < n; ++i) {
				if (list.isItemSelected(i)) {
					elements.add(lbm.getElement(i));
				}
			}
		} else {
			for (int i = 0, n = list.getItemCount(); i < n; ++i) {
				if (list.isItemSelected(i)) {
					elements.add(i);
				}
			}
		}

		return elements;
	}

	private static void setSelectedItems(ListBox list, List<Object> indexes) {
		assert list != null;

		Object model = list.getElement().getPropertyObject("model");
		if (model instanceof ListBindingManager) {
			ListBindingManager lbm = (ListBindingManager) model;
			for (Object index : indexes) {
				list.setSelectedIndex(getEqualItemIndex(lbm.getElements(), index));
			}
		} else {
			for (Object index : indexes) {
				list.setItemSelected((Integer) index, true);
			}
		}
	}

	private static String getSelectedItemText(ListBox list) {
		assert list != null;

		int index = list.getSelectedIndex();

		if (index == -1) {
			return null;
		}

		return list.getItemText(index);
	}

	private static List<String> getSelectedItemsText(ListBox list) {
		assert list != null;

		List<String> elements = new ArrayList<String>();

		if (list.getSelectedIndex() == -1) {
			return elements;
		}

		for (int i = 0, n = list.getItemCount(); i < n; ++i) {
			if (list.isItemSelected(i)) {
				elements.add(list.getItemText(i));
			}
		}

		return elements;
	}

	private static String getSelectedItemValue(ListBox list) {
		assert list != null;

		int index = list.getSelectedIndex();

		if (index == -1) {
			return null;
		}

		return list.getValue(index);
	}

	private static List<String> getSelectedItemsValue(ListBox list) {
		assert list != null;

		List<String> elements = new ArrayList<String>();

		if (list.getSelectedIndex() == -1) {
			return elements;
		}

		for (int i = 0, n = list.getItemCount(); i < n; ++i) {
			if (list.isItemSelected(i)) {
				elements.add(list.getValue(i));
			}
		}

		return elements;
	}
}
