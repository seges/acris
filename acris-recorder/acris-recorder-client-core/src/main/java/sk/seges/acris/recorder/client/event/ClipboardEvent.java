package sk.seges.acris.recorder.client.event;

import com.google.gwt.user.client.Element;
import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

/**
 * Created by PeterSimun on 12.4.2014.
 */
@BeanWrapper
public class ClipboardEvent extends CustomEvent {

    public static final String SELECTION_START_ATTRIBUTE = "selectionStart";
    public static final String SELECTION_END_ATTRIBUTE = "selectionEnd";

    private int selectionStart;
    private int selectionEnd;

    private String clipboardText;

    public static final int PASTE_EVENT_TYPE = 1;
    public static final int CUT_EVENT_TYPE = 2;

    public ClipboardEvent(ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
    }

    public ClipboardEvent(Element element, int typeInt, ElementXpathCache elementXpathCache) {
        super(element, typeInt, elementXpathCache);
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public String getClipboardText() {
        return clipboardText;
    }

    public void setClipboardText(String clipboardText) {
        this.clipboardText = clipboardText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClipboardEvent))
            return false;
        if (!super.equals(o)) return false;

        ClipboardEvent that = (ClipboardEvent) o;

        if (selectionEnd != that.selectionEnd) return false;
        if (selectionStart != that.selectionStart) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + selectionStart;
        result = 31 * result + selectionEnd;
        return result;
    }

    public String getType() {
        switch (getTypeInt()) {
            case PASTE_EVENT_TYPE:
                return "Paste";
            case CUT_EVENT_TYPE:
                return "Cut";
        }
        throw new RuntimeException("Unknown clipboard type: " + getTypeInt());
    }

    @Override
    public String toString(boolean pretty, boolean detailed) {
        if (!pretty) {
            if (!detailed) {
                return getTypeInt() + " on " + relatedTargetXpath + " element";
            }
            return getTypeInt() + "clipboardText = "+ clipboardText + " on position " + getSelectionStart() + "(" + getSelectionEnd() + ")" +
                    " to " + relatedTargetXpath + " element";
        }

        if (!detailed) {
            return "CustomEvent [ type = " + getTypeInt() + ", on " + relatedTargetXpath + " element]";
        }

        return "CustomEvent [ type = " + getTypeInt() + ", clipboardText = " + clipboardText + "," + "selectionStart = " + getSelectionStart() +
                "selectionEnd =" + getSelectionEnd() + ", " + "on " + relatedTargetXpath + " element]";
    }
}