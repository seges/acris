package sk.seges.acris.player.client.objects.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PeterSimun on 12.4.2014.
 */
public class InputHolder {

    private Map<Integer, Boolean> mouseButtons = new HashMap<Integer, Boolean>();

    private boolean shiftKey;
    private boolean altKey;
    private boolean metaKey;
    private boolean ctrlKey;

    private Element focusedElement;
    private String focusedElementXPath;

    public InputHolder() {
        mouseButtons.put(NativeEvent.BUTTON_LEFT, false);
        mouseButtons.put(NativeEvent.BUTTON_MIDDLE, false);
        mouseButtons.put(NativeEvent.BUTTON_RIGHT, false);
    }

    public void setMouseButton(int button, boolean value) {
        mouseButtons.put(button, value);
    }

    public List<Integer> getPressedMouseButtons() {
        List<Integer> buttons = new ArrayList<Integer>();

        for (Map.Entry<Integer, Boolean> buttonEntry: mouseButtons.entrySet()) {
            if (buttonEntry.getValue()) {
                buttons.add(buttonEntry.getKey());
            }
        }

        return buttons;
    }

    public void setShiftKey(boolean pressed) {
        this.shiftKey = pressed;
    }

    public void setAltKey(boolean altKey) {
        this.altKey = altKey;
    }

    public void setMetaKey(boolean metaKey) {
        this.metaKey = metaKey;
    }

    public void setCtrlKey(boolean ctrlKey) {
        this.ctrlKey = ctrlKey;
    }

    public boolean isShiftKey() {
        return shiftKey;
    }

    public boolean isAltKey() {
        return altKey;
    }

    public boolean isMetaKey() {
        return metaKey;
    }

    public boolean isCtrlKey() {
        return ctrlKey;
    }

    public void focus(Element element, String xpath) {
        element.focus();
        this.focusedElement = element;
        this.focusedElementXPath = xpath;
    }

    public void blur() {
        if (focusedElement != null) {
            focusedElement.blur();
            focusedElementXPath = null;
            focusedElement = null;
        }

        blurActiveElement();
    }

    private native void blurActiveElement() /*-{
        if ($doc.activeElement != null) {
            $doc.activeElement.blur();
        }
    }-*/;

    //Select text
    //http://stackoverflow.com/questions/646611/programmatically-selecting-partial-text-in-an-input-field
}