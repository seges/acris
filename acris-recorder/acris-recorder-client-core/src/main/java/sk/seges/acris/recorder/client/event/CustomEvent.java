package sk.seges.acris.recorder.client.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

/**
 * Created by PeterSimun on 12.4.2014.
 */
public abstract class CustomEvent extends AbstractGenericTargetableEvent {

    private int typeInt;

    protected CustomEvent(ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
    }

    protected CustomEvent(Element element, int typeInt, ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
        initTarget(element, null);
        setTypeInt(typeInt);
    }

    @Override
    protected NativeEvent createEvent(Element el) {
        //not a native event, cannot create one
        return null;
    }

    @Override
    public int getTypeInt() {
        return typeInt;
    }

    @Override
    public void setTypeInt(int type) {
        this.typeInt = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomEvent))
            return false;
        if (!super.equals(o)) return false;

        CustomEvent that = (CustomEvent) o;

        if (typeInt != that.typeInt) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + typeInt;
        return result;
    }
}
