package sk.seges.acris.widget.client.table.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sk.seges.acris.widget.client.table.BeanTable;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.user.client.ui.CheckBox;

public abstract class AbstractCheckBoxBeanTable<T> extends BeanTable<T> {

	private Map<T, List<CheckBox>> entityToCheckBoxsMap = new HashMap<T, List<CheckBox>>();
	private T oldEntity;
	private int columnCounter = 0;
	private int checkBoxColumnsCount;

	public abstract List<Boolean> getEntityCheckBoxValues(T entity);

	public void addCheckBoxColumns(int columnsCount, String checkBoxColumnLabelPrefixOrName, boolean prefix ) {
		checkBoxColumnsCount = columnsCount;
		for (int i = 0; i < columnsCount; i++) {
			if (prefix) {
				addCheckBoxColumn(checkBoxColumnLabelPrefixOrName + " " + (i + 1) + ".");
			} else {
				addCheckBoxColumn(checkBoxColumnLabelPrefixOrName);
			}
		}
	}

	private void addCheckBoxColumn(String label) {
		CheckBoxColumnDefinition<T> checkBoxColumnDefinition = new CheckBoxColumnDefinition<T>() {

			@Override
			public CheckBox getCellValue(T rowValue) {

				if (oldEntity == null || !rowValue.equals(oldEntity)) {
					oldEntity = rowValue;
					columnCounter = 0;
				}

				if (!entityToCheckBoxsMap.containsKey(rowValue)) {
					entityToCheckBoxsMap.put(rowValue, new ArrayList<CheckBox>());
				}

				List<CheckBox> checkBoxList = entityToCheckBoxsMap.get(rowValue);
				List<Boolean> entityCheckBoxValueList = getEntityCheckBoxValues(rowValue);

				CheckBox checkBox;
				Boolean confirmed;
				if (checkBoxList.size() < entityCheckBoxValueList.size()) {
					checkBox = createCheckBox();
					confirmed = entityCheckBoxValueList.get(checkBoxList.size());
					checkBoxList.add(checkBox);
				} else {
					if (columnCounter >= checkBoxList.size()) {
						columnCounter = 0;
					}
					checkBox = checkBoxList.get(columnCounter);
					confirmed = entityCheckBoxValueList.get(columnCounter);
					columnCounter++;
				}
				if (Boolean.TRUE == confirmed) {
					checkBox.setValue(true);
				} else {
					checkBox.setValue(false);
				}
				return checkBox;
			}

		};
		addColumn(label, checkBoxColumnDefinition);
	}

	protected CheckBox createCheckBox() {
		return new CheckBox();
	}
	
	private class CheckBoxColumnDefinition<RowType> extends AbstractColumnDefinition<RowType, CheckBox> {

		@Override
		public CheckBox getCellValue(RowType rowValue) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCellValue(RowType rowValue, CheckBox cellValue) {
			// TODO Auto-generated method stub

		}
	}

	private List<CheckBox> getCheckBoxListForEntityId(T entity) {
		return entityToCheckBoxsMap.get(entity);
	}

	public List<Boolean> getCheckBoxValuesForEntity(T entity) {
		List<CheckBox> checkBoxList = getCheckBoxListForEntityId(entity);
		List<Boolean> checkBoxValueList = new ArrayList<Boolean>();
		for (CheckBox checkBox : checkBoxList) {
			checkBoxValueList.add(checkBox.getValue());
		}
		return checkBoxValueList;
	}

	public void selectAllCheckBoxes(boolean select) {
		Iterator<List<CheckBox>> iterator = entityToCheckBoxsMap.values().iterator();
		List<CheckBox> checkBoxList;
		while (iterator.hasNext()) {
			checkBoxList = iterator.next();
			for (CheckBox checkBox : checkBoxList) {
				checkBox.setValue(select);
			}
		}

	}

	/**
	 * @return the checkBoxColumnsCount
	 */
	public int getCheckBoxColumnsCount() {
		return checkBoxColumnsCount;
	}

}
