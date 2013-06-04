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

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * This is a basic class for all text boxs with a button.
 *
 * @see org.gwt.advanced.client.ui.widget.ComboBox
 * @see org.gwt.advanced.client.ui.widget.DatePicker
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public abstract class TextButtonPanel extends SimplePanel  {

	public interface TextButtonResources extends ClientBundle {
		ImageResource button();
	}
	
	interface DefaultTextButtonResources extends TextButtonResources {
		
		@Source("dropdown-arrow.gif")
		ImageResource button();
	}
	
	public class LockingPanel extends PopupPanel {
	    /**
	     * Creates an instance of this class.
	     */
	    public LockingPanel() {
	        super(false, false);
	    }

	    /**
	     * Shows the panel.
	     */
	    public void lock() {
	        setStyleName("advanced-LockingPanel");
	        setPopupPosition(0, 0);
	        setWidth("100%");
	        setHeight("100%");
	        setPixelSize(Window.getClientWidth(), Window.getClientHeight());

	        show();
	    }

	    /**
	     * Hides the panel.
	     */
	    public void unlock() {
	        hide();
	    }
	}
	
	/** widget layout */
    private FlexTable layout;
    /** a selected value box */
    private TextBox selectedValue;
    /** a choice button */
    private ToggleButton choiceButton;
    /** a choice button image */
    private Image choiceButtonImage;
    /** this flag means whether it's possible to enter a custom text */
    private boolean customTextAllowed;
    /** a falg meaning whether the widget locked */
    private boolean locked;
    /** a locking panel to lock the screen */
    private LockingPanel lockingPanel;
    /** choice button visibility flag */
    private boolean choiceButtonVisible = true;
    /** widget width */
    private String width;
    /** widget height */
    private String height;

	protected TextButtonPanel(TextButtonResources resources) {
        choiceButtonImage = new Image(resources.button());
	}

	protected TextButtonPanel(Element elem, TextButtonResources resources) {
    	super(elem);
        choiceButtonImage = new Image(resources.button());
	}
	
    public boolean isCustomTextAllowed() {
        return customTextAllowed;
    }

    public void setCustomTextAllowed(boolean customTextAllowed) {
        this.customTextAllowed = customTextAllowed;
    }

    public void setChoiceButtonImage(Image choiceButtonImage) {
        this.choiceButtonImage = choiceButtonImage;
    }

    public boolean isChoiceButtonVisible() {
        return choiceButtonVisible;
    }

    public void setChoiceButtonVisible(boolean choiceButtonVisible) {
        this.choiceButtonVisible = choiceButtonVisible;
    }

    public void display() {
        FlexTable layout = getLayout();
        while (layout.getRowCount() > 0)
            layout.removeRow(layout.getRowCount() - 1);

        addComponentListeners();

        layout.setWidget(0, 0, getSelectedValue());
        layout.setWidget(0, 1, getChoiceButton());

        prepareSelectedValue();
        if(isChoiceButtonVisible())
            prepareChoiceButton();

        setStyleName("advanced-TextButtonPanel");
        setWidget(layout);
    }

    public void setWidth(String width) {
        super.setWidth(width);
        this.width = width;
        prepareSelectedValue();
    }

   public void setHeight(String height) {
        super.setHeight(height);
        this.height = height;
        prepareSelectedValue();
    }

    /**
     * This method gets a maximum length of the text box.<p/>
     * It makes sence if you allow custom values entering.<p/>
     * See also {@link #isCustomTextAllowed()} and {@link #setCustomTextAllowed(boolean)}.
     *
     * @return a maximum length of the text box.
     */
    public int getMaxLength() {
        return getSelectedValue().getMaxLength();
    }

    /**
     * This method sets a maximum length of the text box.<p/>
     * It makes sence if you allow custom values entering.<p/>
     * See also {@link #isCustomTextAllowed()} and {@link #setCustomTextAllowed(boolean)}.
     *
     * @param length is a maximum length of the text box.
     */
    public void setMaxLength(int length) {
        getSelectedValue().setMaxLength(length);
    }

    /**
     * This method adds component listeners.
     */
    protected abstract  void addComponentListeners();

    /**
     * Prepares the selected value box for displaying.
     */
    protected void prepareSelectedValue() {
        TextBox selectedValue = getSelectedValue();
        selectedValue.setReadOnly(!isCustomTextAllowed());
        selectedValue.setStyleName("selected-value");

        if(getHeight() != null) {
            getLayout().setHeight("100%");
            getLayout().getCellFormatter().setHeight(0, 0, "100%");
            getSelectedValue().setHeight("79%");
        }

        if (getWidth() != null) {
            getLayout().setWidth("100%");
            getLayout().getCellFormatter().setWidth(0, 0, "100%");
            getSelectedValue().setWidth("100%");
        }
    }

    protected void prepareChoiceButton() {
        ToggleButton dropDownButton = getChoiceButton();
        dropDownButton.getUpFace().setImage(choiceButtonImage);
        dropDownButton.getDownFace().setImage(choiceButtonImage);
        dropDownButton.setStyleName("choice-button");
    }

    protected FlexTable getLayout() {
        if (layout == null) {
            layout = new FlexTable();
            layout.setBorderWidth(0);
            layout.setCellPadding(0);
            layout.setCellSpacing(0);
        }
        return layout;
    }

    protected TextBox getSelectedValue() {
        if (selectedValue == null)
            selectedValue = new TextBox();
        return selectedValue;
    }

    protected ToggleButton getChoiceButton() {
        if (choiceButton == null)
            choiceButton = new ToggleButton();
        return choiceButton;
    }

    public int getChoiceButtonOffsetWidth() {
        return getChoiceButton().getOffsetWidth();
    }
    
    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        setLocked(true);
        getLockingPanel().lock();
    }

    public void unlock() {
        display();
        getLockingPanel().unlock();
        setLocked(false);
    }

    protected void setLocked(boolean locked) {
        this.locked = locked;
    }

    protected LockingPanel getLockingPanel() {
        if (lockingPanel == null)
            lockingPanel = new LockingPanel();
        return lockingPanel;
    }

    protected String getWidth() {
        return width;
    }

    protected String getHeight() {
        return height;
    }
}
