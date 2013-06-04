package sk.seges.acris.widget.client.advanced;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

/**
 * ListBox specific for enums<br />
 * used in {@link BeanTable} if ListBox is needed in filter<br />
 * implements HasValues, get/setValue methods convert from/to string (ListBox
 * item is of type string) to enum
 * 
 * @author marta
 * 
 * @param <T>
 *            enum
 */
public class EnumListBoxWithValue<T extends Enum<T>> extends ListBox implements HasValue<T> {

	Class<T> clazz = null;

	public EnumListBoxWithValue() {}

	/**
	 * concrete enum must be specified in constructor or via init method, it is
	 * necessary for converting from string name to enum constant
	 * 
	 * @param enumClazz
	 */
	public EnumListBoxWithValue(Class<T> enumClazz) {
		this.clazz = enumClazz;
	}

	/**
	 * 
	 * @param enumClazz
	 */
	public void init(Class<T> enumClazz) {
		this.clazz = enumClazz;
	}

	/**
	 * @return enum constant, which name is selected in listbox
	 */
	public T getValue() {
		return getEnumValue(getValue(getSelectedIndex()));
	}

	/**
	 * @param if value is in listbox, it finds it and set the element as
	 *        selected
	 * 
	 */
	public void setValue(T value) {
		if (value == null)
			return;
		for (int i = 0; i < super.getItemCount(); i++) {
			T actual = getEnumValue(getValue(i));
			if (actual == null) {
				continue;
			}
			if (value.name().equals(actual.name())) {
				super.setSelectedIndex(i);
			}
		}
	}

	/**
	 * 
	 * @param name string
	 * @return enum value from String name
	 */
	public T getEnumValue(String name) {
		if (clazz == null || name == null || name.length() < 1)
			return null;
		try {
			return Enum.valueOf(clazz, name);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * loads elements from enumList to ListBox
	 * 
	 * @param enumList
	 *            usually Enum.values(), but can be any list with elements of
	 *            type T
	 */
	public void load(List<T> enumList) {
		super.clear();
		addItem("", null);
		for (T enum1 : enumList) {
			addItem(enum1.name(), enum1.name());
		}
	}
	
	/**
	 * loads enum elements to ListBox
	 * @param translatedEnums map of enums - as key is enum item, as value it's translated name
	 */
	public void load(Map<T, String> translatedEnums) {
		super.clear();
		addItem("", null);
		for (T enum1 : translatedEnums.keySet()) {
			addItem(translatedEnums.get(enum1), enum1.name());
		}
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		this.setValue(value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

}
