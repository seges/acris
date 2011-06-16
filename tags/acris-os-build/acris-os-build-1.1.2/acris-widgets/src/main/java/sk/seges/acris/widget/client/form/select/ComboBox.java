package sk.seges.acris.widget.client.form.select;

/*
 * Copyright 2008 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import sk.seges.acris.widget.client.ElementFlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesFocusEvents;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is a combo box widget implementation.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */                                                       
public class ComboBox extends TextButtonPanel
        implements SourcesFocusEvents, SourcesChangeEvents, SourcesKeyboardEvents, SourcesClickEvents {

	public interface ListDataModel {
	    /**
	     * This method adds a new item in the list.
	     *
	     * @param id an unique ID of the item. If there is an item with the same ID, this method replaces it with
	     *           a new value.
	     * @param item is an item to be placed into the list.
	     */
	    void add(String id, Object item);

	    /**
	     * This method adds a new item into the specified position.<p/>
	     * If the index < 0 it adds a new item into the 0 position. If the index > number of existing items it adds an item
	     * into the end of this list.
	     *
	     * @param index is an index value.
	     * @param id is an item ID.
	     * @param item is an item.
	     */
	    void add(int index, String id, Object item);

	    /**
	     * This method returns an item by its ID.
	     *
	     * @param id is an item ID.
	     *
	     * @return an item.
	     */
	    Object get(String id);

	    /**
	     * This method returns an item by its index.
	     *
	     * @param index is an item index.
	     *
	     * @return an item.
	     */
	    Object get(int index);

	    /**
	     * This method removes the specified item.
	     *
	     * @param id is an item ID.
	     */
	    void remove(String id);

	    /**
	     * This method removes an item specified by its index.
	     *
	     * @param index is an item index.
	     */
	    void remove(int index);

	    /**
	     * This method gets a selected item ID.
	     *
	     * @return an item ID.
	     */
	    String getSelectedId();

	    /**
	     * This method gets a selected item index.
	     *
	     * @return an item index.
	     */
	    int getSelectedIndex();

	    /**
	     * This method returns a selected item.
	     *
	     * @return a selected item.
	     */
	    Object getSelected();

	    /**
	     * This method sets a currently selected item specifying it by ID.
	     *
	     * @param id is an item ID.
	     */
	    void setSelectedId(String id);

	    /**
	     * This method sets a currently selected item specifying it by the index.<p/>
	     * If the index < 0 it selects item 0. If the index > then list size, it selects the last item. 
	     *
	     * @param index is an item index.
	     */
	    void setSelectedIndex(int index);

	    /**
	     * This methdo clears the list of items.
	     */
	    void clear();

	    /**
	     * This method returns <code>true</code> if the list of items is empty.
	     *
	     * @return a result of check.
	     */
	    boolean isEmpty();

	    /**
	     * Returns a number of items in the list.
	     *
	     * @return a number of items.
	     */
	    int getCount();
	}

	public interface ModelChangeListner {
		void modelChanged();
	}

	public static class ComboBoxDataModel implements ListDataModel {
	    /** a list of item IDs where each item is instance of <code>String</code> */
	    private List itemIds = new ArrayList();
	    /** a map of items where each item is pair of <code>String</code> ID and <code>Object</code> value */
	    private Map items = new HashMap();
	    /** a selected item ID */
	    private String selectedId;

	    private List<ModelChangeListner> modelChangeListeners = new ArrayList<ModelChangeListner>();
	    
	    public void addModelChangeListner(ModelChangeListner listener) {
	    	modelChangeListeners.add(listener);
	    }

	    public void removeModelChangeListner(ModelChangeListner listener) {
	    	modelChangeListeners.remove(listener);
	    }

	    /** {@inheritDoc} */
	    public void add(String id, Object item) {
	    	addItem(id, item);
	        for (ModelChangeListner listener : modelChangeListeners) {
	        	listener.modelChanged();
	        }
	    }

	    private void addItem(String id, Object item) {
	        List ids = getItemIds();
	        if (!ids.contains(id))
	            ids.add(id);
	        getItems().put(id, item);
	    }

	    /** {@inheritDoc} */
	    public void add(Map<String, Object> items) {
	    	for (Entry<String, Object> item : items.entrySet()) {
	    		addItem(item.getKey(), item.getValue());
	    	}
	        sort();
	        for (ModelChangeListner listener : modelChangeListeners) {
	        	listener.modelChanged();
	        }
	    }
	    
	    /** {@inheritDoc} */
	    public void add(int index, String id, Object item) {
	        List ids = getItemIds();
	        index = getValidIndex(index);

	        if (!ids.contains(id))
	            ids.add(index, id);

	        add(id, item);
	    }

	    /** {@inheritDoc} */
	    public Object get(String id) {
	        return getItems().get(id);
	    }

	    /** {@inheritDoc} */
	    public Object get(int index) {
	        if (isIndexValid(index))
	            return get((String) getItemIds().get(index));
	        else
	            return null;
	    }

	    /** {@inheritDoc} */
	    public void remove(String id) {
	        getItemIds().remove(id);
	        getItems().remove(id);
	        for (ModelChangeListner listener : modelChangeListeners) {
	        	listener.modelChanged();
	        }
	    }

	    /** {@inheritDoc} */
	    public void remove(int index) {
	        if (isIndexValid(index)) {
	            remove((String) getItemIds().get(index));
	            
	            for (ModelChangeListner listener : modelChangeListeners) {
	            	listener.modelChanged();
	            }

	        }
	    }

	    /** {@inheritDoc} */
	    public String getSelectedId() {
	        if (selectedId == null && itemIds.size() > 0)
	            selectedId = (String) itemIds.get(0);
	        return selectedId;
	    }

	    /** {@inheritDoc} */
	    public int getSelectedIndex() {
	        return getItemIds().indexOf(getSelectedId());
	    }

	    /** {@inheritDoc} */
	    public Object getSelected() {
	        return getItems().get(getSelectedId());
	    }

	    /** {@inheritDoc} */
	    public void setSelectedId(String id) {
	        this.selectedId = id;
	    }

	    /** {@inheritDoc} */
	    public void setSelectedIndex(int index) {
	        index = getValidIndex(index);
	        List ids = getItemIds();
	        if (ids.size() > 0)
	            setSelectedId((String) ids.get(index));
	    }

	    /** {@inheritDoc} */
	    public void clear() {
	        itemIds.clear();
	    }

	    /** {@inheritDoc} */
	    public boolean isEmpty() {
	        return itemIds.isEmpty();
	    }

	    /** {@inheritDoc} */
	    public int getCount() {
	        return itemIds.size();
	    }

	    /**
	     * Getter for property 'itemIds'.
	     *
	     * @return Value for property 'itemIds'.
	     */
	    protected List getItemIds() {
	        return itemIds;
	    }

	    /**
	     * Getter for property 'items'.
	     *
	     * @return Value for property 'items'.
	     */
	    protected Map getItems() {
	        return items;
	    }

	    /**
	     * This method checks whether the specified index is valid.
	     *
	     * @param index is an index value to check.
	     * @return <code>true</code> if the index is valid.
	     */
	    protected boolean isIndexValid(int index) {
	        return getItemIds().size() >= index;
	    }

	    /**
	     * This method calculates a valid index value taking into account the following rule:
	     * if the index < 0, it returns 0;
	     * if the index > then {@link #getItemIds()} size, it returns {@link #getItemIds()} size.
	     *
	     * @param invalidIndex is an index.
	     * @return a valid index value.
	     */
	    protected int getValidIndex(int invalidIndex) {
	        List ids = getItemIds();

	        if (invalidIndex < 0)
	            invalidIndex = 0;
	        if (invalidIndex > ids.size())
	            invalidIndex = ids.size();
	        return invalidIndex;
	    }
	    
	    public void sort(){
	    	Collections.sort(getItemIds());
	    }
	}
	
	public interface ListItemFactory {
	    /**
	     * This method creates a new widget that should be inserted into the list.
	     *
	     * @param value is a value to be used to construct the widget.
	     * @return a widget instance (can be equal to <code>null</code>).
	     */
	    Widget createWidget(Object value);

	    /**
	     * This method should convert the value to the text to be displayed in the selection text box.
	     *
	     * @param value is a value to be converted.
	     * @return textual representation of the value.
	     */
	    String convert(Object value);
	}

	public class IconItem {
	    /** an icon image name */
	    private String imageName;
	    /** an icon label */
	    private String label;

	    /**
	     * Creates an instance of this class and initilizes internal fields.
	     *
	     * @param imageName is an image name.
	     * @param label is a label.
	     */
	    public IconItem(String imageName, String label) {
	        this.imageName = imageName;
	        this.label = label;
	    }

	    /**
	     * Getter for property 'imageName'.
	     *
	     * @return Value for property 'imageName'.
	     */
	    public String getImageName() {
	        return imageName;
	    }

	    /**
	     * Getter for property 'label'.
	     *
	     * @return Value for property 'label'.
	     */
	    public String getLabel() {
	        return label;
	    }
	}

	public class DefaultListItemFactory implements ListItemFactory {
	    /**
	     * See class docs.
	     *
	     * @param value is a value to be adopted.
	     * @return a widget to be inserted into the list.
	     */
	    public Widget createWidget(Object value) {
	        if (value == null)
	            return new Label();
	        else if (value instanceof String || value instanceof Number)
	            return new Label(String.valueOf(value));
	        else if (value instanceof Date) {
	            return new Label(String.valueOf(value));
	        } else if (value instanceof IconItem) {
	            IconItem item = (IconItem) value;
	            FlexTable table = new FlexTable();
	            table.setStyleName("icon-item");
	            table.setWidget(0, 0, new Image(item.getImageName()));
	            table.setWidget(0, 1, new Label(item.getLabel()));
	            table.getCellFormatter().setWidth(0, 0, "1%");
	            table.getCellFormatter().setWidth(0, 1, "99%");
	            return table;
	        } else
	            return createWidget(value.toString());
	    }

	    public String convert(Object value) {
	        if (value == null)
	            return "";
	        else if (value instanceof String || value instanceof Number)
	            return String.valueOf(value);
	        else if (value instanceof Date)
	            return String.valueOf(value);
	        else if (value instanceof IconItem)
	            return ((IconItem)value).getLabel();
	        else 
	        	return convert(value.toString());
	            
	    }
	}

	public class ListPopupPanel extends PopupPanel {
	    /** a list of items */
	    private VerticalPanel list;
	    /** items scrolling widget */
	    private ScrollPanel scrollPanel;
	    /** a flag meaning whether this widget is hidden */
	    private boolean hidden = true;
	    /** a parent selection box */
	    private ComboBox comboBox;
	    /** a list of change listeners */
	    private List changeListeners;
	    /** item click  listener */
	    private ItemClickHandler itemClickListener;
	    /** mouse event listener */
	    private ListMouseHandler mouseEventsListener;

	    /**
	     * Creates an instance of this class and sets the parent combo box value.
	     *
	     * @param selectionTextBox is a selection box value.
	     */
	    protected ListPopupPanel(ComboBox selectionTextBox) {
	        super(true, false);
	        this.comboBox = selectionTextBox;
	        addPopupListener(new AutoPopupListener());
	    }

	    /**
	     * This method adds a listener that will be invoked on choice.
	     *
	     * @param listener is a listener to be added.
	     */
	    public void addChangeListener(ChangeListener listener) {
	        removeChangeListener(listener);
	        getChangeListeners().add(listener);
	    }

	    /**
	     * This method removes the change listener.
	     *
	     * @param listener a change listener.
	     */
	    public void removeChangeListener(ChangeListener listener) {
	        getChangeListeners().remove(listener);
	    }

	    /**
	     * Getter for property 'hidden'.
	     *
	     * @return Value for property 'hidden'.
	     */
	    public boolean isHidden() {
	        return hidden;
	    }

	    /** {@inheritDoc} */
	    public void hide() {
	        try {
	            getComboBox().getDelegateListener().onLostFocus(this);
	        } finally {
	            super.hide();
	            setHidden(true);
	        }
	    }

	    /** {@inheritDoc} */
	    public void show() {
	        setHidden(false);
	        super.show();
	        getComboBox().getDelegateListener().onFocus(this);
	    }
	    
	    /** {@inheritDoc} */
	    public void display() {
	        setStyleName("advanced-ListPopupPanel");
	        
	        if (!isHidden())
	            hide();
	                       
	        VerticalPanel panel = getList();
	        selectRow(getComboBox().getModel().getSelectedIndex());
	        getScrollPanel().setWidth((getComboBox().getOffsetWidth() - 2) + "px");
	        panel.setWidth((getComboBox().getOffsetWidth() - 2) + "px");
	        panel.setStyleName("list");

	        setWidget(getScrollPanel());
	        
	        show();
	        setPopupPosition(getComboBox().getAbsoluteLeft(),
	                getComboBox().getAbsoluteTop() + getComboBox().getOffsetHeight());

	        ScrollPanel table = getScrollPanel();
	        if (table.getOffsetHeight() > Window.getClientHeight() * 0.3) {
	            table.setHeight((int)(Window.getClientHeight() * 0.3) + "px");
	        } 
	    }

	    /**
	     * This method prepares the list of items for displaying.
	     */
	    public void prepareList() {
	        VerticalPanel panel = getList();
	        panel.clear();

	        ComboBoxDataModel model = getComboBox().getModel();
	        ListItemFactory itemFactory = getComboBox().getListItemFactory();

	        for (int i = 0; i < model.getCount(); i++) {
	            Widget widget = adoptItemWidget(itemFactory.createWidget(model.get(i)));
	            panel.add(widget);
	        }

	        selectRow(getComboBox().getModel().getSelectedIndex());
	        getScrollPanel().setWidth(getComboBox().getOffsetWidth() + "px");
	        panel.setWidth("100%");

	        panel.setStyleName("list");
	    }

	    /**
	     * This method higlights a selected row.
	     *
	     * @param newRow a row for selection.
	     */
	    protected void selectRow(int newRow) {
	        ComboBoxDataModel model = getComboBox().getModel();
	        VerticalPanel panel = getList();

	        int row = model.getSelectedIndex();

	        if (row >= 0 && row < panel.getWidgetCount())
	            panel.getWidget(row).removeStyleName("selected-row");

	        model.setSelectedIndex(newRow);
	        newRow = model.getSelectedIndex();

	        if (newRow >= 0 && newRow < panel.getWidgetCount())
	            panel.getWidget(newRow).addStyleName("selected-row");
	        
	    }

	    /**
	     * This method wraps the specified widget into the focus panel and adds necessary listeners.
	     *
	     * @param widget is an item widget to be wraped.
	     * @return a focus panel adopted for displaying.
	     */
	    protected ElementFlowPanel adoptItemWidget(Widget widget) {
	    	ElementFlowPanel panel = new ElementFlowPanel();
	    	panel.add(widget);
	        panel.setWidth("100%");
	        widget.setWidth("100%");

	        panel.sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEOVER);
	        
	        panel.addClickHandler(getItemClickListener());
	        ListMouseHandler mouseEventsListener = getMouseEventsListener();
	        panel.addHandler(mouseEventsListener, MouseOverEvent.getType());
	        panel.addHandler(mouseEventsListener, MouseOutEvent.getType());

	        panel.setStyleName("item");
	        return panel;
	    }

	    /**
	     * Setter for property 'hidden'.
	     *
	     * @param hidden Value to set for property 'hidden'.
	     */
	    protected void setHidden(boolean hidden) {
	        this.hidden = hidden;
	    }

	    /**
	     * Getter for property 'comboBox'.
	     *
	     * @return Value for property 'comboBox'.
	     */
	    protected ComboBox getComboBox() {
	        return comboBox;
	    }

	    /**
	     * Getter for property 'changeListeners'.
	     *
	     * @return Value for property 'changeListeners'.
	     */
	    protected List getChangeListeners() {
	        if (changeListeners == null)
	            changeListeners = new ArrayList();
	        return changeListeners;
	    }

	    /**
	     * Getter for property 'list'.
	     *
	     * @return Value for property 'list'.
	     */
	    protected VerticalPanel getList() {
	        if (list == null)
	            list = new VerticalPanel();
	        return list;
	    }

	    /**
	     * Getter for property 'scrollPanel'.
	     *
	     * @return Value for property 'scrollPanel'.
	     */
	    public ScrollPanel getScrollPanel() {
	        if(scrollPanel == null) {
	            scrollPanel = new ScrollPanel();
	            scrollPanel.setAlwaysShowScrollBars(false);
//	            SimplePanel wrapper = new SimplePanel();
//	            wrapper.setWidth("100%");
//	            DOM.setElementAttribute(wrapper.getElement(), "style", "background: white; z-index: -100");
//	            wrapper.add(getList());
//	            scrollPanel.setWidget(wrapper);
	            scrollPanel.setWidget(getList());
	        }
	        return scrollPanel;
	    }

	    /**
	     * Getter for property 'itemClickListener'.
	     *
	     * @return Value for property 'itemClickListener'.
	     */
	    public ClickHandler getItemClickListener() {
	        if (itemClickListener == null)
	            itemClickListener = new ItemClickHandler(this);
	        return itemClickListener;
	    }

	    /**
	     * Getter for property 'mouseEventsListener'.
	     *
	     * @return Value for property 'mouseEventsListener'.
	     */
	    public ListMouseHandler getMouseEventsListener() {
	        if (mouseEventsListener == null)
	            mouseEventsListener = new ListMouseHandler();
	        return mouseEventsListener;
	    }

	    /**
	     * This is a click listener required to dispatch click events.
	     */
	    protected class ItemClickHandler implements ClickHandler {
	        /** a list panel instance */
	        private ListPopupPanel panel;

	        /**
	         * Creates an instance of this class and initializes internal fields.
	         *
	         * @param panel is a list panel.
	         */
	        public ItemClickHandler(ListPopupPanel panel) {
	            this.panel = panel;
	        }

	        /**
	         * Getter for property 'panel'.
	         *
	         * @return Value for property 'panel'.
	         */
	        public ListPopupPanel getPanel() {
	            return panel;
	        }

			@Override
			public void onClick(ClickEvent event) {
	            selectRow(getList().getWidgetIndex((Widget)event.getSource()));

	            for (Iterator iterator = getChangeListeners().iterator(); iterator.hasNext();) {
	                ChangeListener changeListener = (ChangeListener) iterator.next();
	                changeListener.onChange(getPanel());
	            }
			}
	    }

	    /**
	     * This listener is required to handle mouse moving events over the list.
	     */
	    protected class ListMouseHandler implements MouseOverHandler, MouseOutHandler {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				((Widget)event.getSource()).removeStyleName("selected-row");
			}

			@Override
			public void onMouseOver(MouseOverEvent event) {
	            int index = getComboBox().getModel().getSelectedIndex();
	            if (index >= 0)
	                getList().getWidget(index).removeStyleName("selected-row");
	            ((Widget)event.getSource()).addStyleName("selected-row");
			}
	    }

	    /**
	     * This is listener that sets the choice button in up state, if the panel has been closed automatically.
	     */
	    protected class AutoPopupListener implements PopupListener {
	        /** {@inheritDoc} */
	        public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
	            if (autoClosed) {
	                hide();
	                getComboBox().getChoiceButton().setDown(false);
	            }
	        }
	    }
	}
	/** a combo box data model */
    private ComboBoxDataModel model;
    /** a list item factory */
    private ListItemFactory listItemFactory;
    /** a list popup panel */
    private ListPopupPanel listPanel;
    /** a combo box delegate listener */
    private DelegateListener delegateListener;

    public ComboBox() {
    	super((TextButtonResources)GWT.create(DefaultTextButtonResources.class));
    }

    public ComboBox(TextButtonResources textButtonResources) {
    	super(textButtonResources);
    }

    public ComboBox(Element element) {
    	super(element, (TextButtonResources)GWT.create(DefaultTextButtonResources.class));
    }

    public ComboBox(Element element, TextButtonResources textButtonResources) {
    	super(element, textButtonResources);
    }

    public void setModel(ComboBoxDataModel model) {
        if (model != null) {
            this.model = model;
            model.addModelChangeListner(new ModelChangeListner() {
				public void modelChanged() {
					listPanel.prepareList();
				}
            });
        }
    }

    public void setListItemFactory(ListItemFactory listItemFactory) {
        if (listItemFactory != null)
            this.listItemFactory = listItemFactory;
    }

    public void addChangeListener(ChangeListener listener) {
        removeChangeListener(listener);
        getDelegateListener().getChangeListeners().add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        getDelegateListener().getChangeListeners().remove(listener);
    }

    public void addClickListener(ClickListener listener) {
        removeClickListener(listener);
        getDelegateListener().getClickListeners().add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        getDelegateListener().getClickListeners().remove(listener);
    }

    public void addFocusListener(FocusListener listener) {
        removeFocusListener(listener);
        getDelegateListener().getFocusListeners().add(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        getDelegateListener().getFocusListeners().remove(listener);
    }

    public void addKeyboardListener(KeyboardListener listener) {
        removeKeyboardListener(listener);
        getDelegateListener().getKeyboardListeners().remove(listener);
    }

    @Override
    public void display() {
    	getListPanel().prepareList();
    	super.display();
    }
    
    public void removeKeyboardListener(KeyboardListener listener) {
        getDelegateListener().getKeyboardListeners().remove(listener);
    }

    public ComboBoxDataModel getModel() {
        if (model == null) {
            model = new ComboBoxDataModel();
            model.addModelChangeListner(new ModelChangeListner() {
				public void modelChanged() {
					getListPanel().prepareList();
				}
            });
        }
        return model;
    }

    public ListItemFactory getListItemFactory() {
        if (listItemFactory == null)
            listItemFactory = new DefaultListItemFactory();
        return listItemFactory;
    }

    /**
     * This method sets focus on this widget.<p/>
     * But note that the combo box is not a focus event sourcer. It siply delegtes this functionality
     * to the text box.
     *
     * @param focus is a falg of focus.
     */
    public void setFocus(boolean focus) {
        if (isCustomTextAllowed())
            getSelectedValue().setFocus(focus);
        else
            getChoiceButton().setFocus(focus);
    }

    public boolean isListPanelOpened() {
        return !getListPanel().isHidden();
    }

    public String getText() {
        return getSelectedValue().getText();
    }

    public Object getSelected() {
        return getModel().getSelected();
    }

    public int getSelectedIndex() {
        return getModel().getSelectedIndex();
    }

    public String getSelectedId() {
        return getModel().getSelectedId();
    }

    public void setSelectedId(String id) {
        getModel().setSelectedId(id);
    }

    public void setSelectedIndex(int index) {
        getModel().setSelectedIndex(index);
    }

    public void setListPopupOpened(boolean opened) {
        if (opened)
            getListPanel().show();
        else
            getListPanel().hide();
    }

    protected void prepareSelectedValue() {
        super.prepareSelectedValue();
        getSelectedValue().setText(getListItemFactory().convert(getModel().getSelected()));
    }

    protected void addComponentListeners() {
        TextBox value = getSelectedValue();
        ToggleButton button = getChoiceButton();
        
        getListPanel().addChangeListener(getDelegateListener());
        value.removeChangeListener(getDelegateListener());
        value.addChangeListener(getDelegateListener());
        button.removeFocusListener(getDelegateListener());
        button.addFocusListener(getDelegateListener());
        value.removeFocusListener(getDelegateListener());
        value.addFocusListener(getDelegateListener());
        button.removeFocusListener(getDelegateListener());
        button.addFocusListener(getDelegateListener());
        value.removeClickListener(getDelegateListener());
        value.addClickListener(getDelegateListener());
        button.removeClickListener(getDelegateListener());
        button.addClickListener(getDelegateListener());
        value.removeKeyboardListener(getDelegateListener());
        value.addKeyboardListener(getDelegateListener());
    }

    protected ListPopupPanel getListPanel() {
        if (listPanel == null)
            listPanel = new ListPopupPanel(this);
        return listPanel;
    }

    protected DelegateListener getDelegateListener() {
        if (delegateListener == null)
            delegateListener = new DelegateListener(this);
        return delegateListener;
    }

    /**
     * Universal listener that delegates all events handling to custom listeners.
     */
    protected class DelegateListener implements FocusListener, ClickListener, ChangeListener, KeyboardListener {
        /** a list of focused controls */
        private Set focuses;
        /** a combo box widget */

        private Widget widget;
        private FocusListenerCollection focusListeners;
        private ClickListenerCollection clickListeners;
        private ChangeListenerCollection changeListeners;
        private KeyboardListenerCollection keyboardListeners;

        /**
         * Creates an instance of this class passing a widget that will be an event sender.
         *
         * @param widget is a widget to be used.
         */
        public DelegateListener(Widget widget) {
            this.widget = widget;
        }

        public void onFocus(Widget sender) {
            getFocuses().add(sender);

            TextBox value = getSelectedValue();
            if (sender == value) {
                if (!isCustomTextAllowed())
                    value.addStyleName("selected-row");
                else
                    value.setSelectionRange(0, value.getText().length());
            }

            if (focuses.size() == 1)
                getFocusListeners().fireFocus(widget);
        }

        public void onLostFocus(Widget sender) {
            if (!isFocus())
                return;
            
            getFocuses().remove(sender);

            TextBox value = getSelectedValue();
            if (sender == value && !isCustomTextAllowed())
                    value.removeStyleName("selected-row");
            
            if (!isFocus())
                getFocusListeners().fireLostFocus(widget);
        }

        public void onClick(Widget sender) {
            int count = getModel().getCount();
            if (sender instanceof ToggleButton || !isCustomTextAllowed()) {
                if (count > 0)
                    getListPanel().display();
                else
                    getChoiceButton().setDown(false);
            }
            getClickListeners().fireClick(widget);
        }

        public void onChange(Widget sender) {
            if (sender == getListPanel()) {
                getSelectedValue().setText(getListItemFactory().convert(getModel().getSelected()));
                getListPanel().hide();
                getSelectedValue().removeStyleName("selected-row");
                getChoiceButton().setDown(false);
            }
            getChangeListeners().fireChange(widget);
        }

        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyDown(widget, keyCode, modifiers);
        }

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyPress(widget, keyCode, modifiers);
        }

        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            getKeyboardListeners().fireKeyUp(widget, keyCode, modifiers);
        }
        
        protected FocusListenerCollection getFocusListeners() {
            if (focusListeners == null)
                focusListeners = new FocusListenerCollection();
            return focusListeners;
        }

        protected ClickListenerCollection getClickListeners() {
            if (clickListeners == null)
                clickListeners = new ClickListenerCollection();
            return clickListeners;
        }

        protected ChangeListenerCollection getChangeListeners() {
            if (changeListeners == null)
                changeListeners = new ChangeListenerCollection();
            return changeListeners;
        }

        protected KeyboardListenerCollection getKeyboardListeners() {
            if (keyboardListeners == null)
                keyboardListeners = new KeyboardListenerCollection();
            return keyboardListeners;
        }

        protected Set getFocuses() {
            if (focuses == null)
                focuses = new HashSet();
            return focuses;
        }

        protected boolean isFocus() {
            return getFocuses().size() > 0;
        }
    }
    
    private boolean enabled = true;
    
	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) {
			return;
		}
		
		this.getSelectedValue().setEnabled(enabled);
		this.getChoiceButton().setEnabled(enabled);
		
		if (!enabled) {
	        TextBox value = getSelectedValue();
	        ToggleButton button = getChoiceButton();

	        getListPanel().removeChangeListener(getDelegateListener());
	        value.removeChangeListener(getDelegateListener());
	        button.removeFocusListener(getDelegateListener());
	        value.removeFocusListener(getDelegateListener());
	        button.removeFocusListener(getDelegateListener());
	        value.removeClickListener(getDelegateListener());
	        button.removeClickListener(getDelegateListener());
	        value.removeKeyboardListener(getDelegateListener());
		} else {
			addComponentListeners();
		}
		
		this.enabled = enabled;
	}

	public void addItems(Collection<String> items) {
		Map<String, Object> mapItems = new HashMap<String, Object>();
		for (String item : items) {
			mapItems.put(item.toString(), item);
		}
		getModel().add(mapItems);
	}

	public void addItems(String... items) {
		Map<String, Object> mapItems = new HashMap<String, Object>();
		for (String item : items) {
			mapItems.put(item.toString(), item);
		}
		getModel().add(mapItems);
	}
}