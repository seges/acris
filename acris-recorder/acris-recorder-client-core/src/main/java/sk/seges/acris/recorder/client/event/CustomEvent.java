package sk.seges.acris.recorder.client.event;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

/**
 * Created by PeterSimun on 12.4.2014.
 */
public abstract class CustomEvent extends AbstractGenericTargetableEvent {

    private int typeInt;

    protected CustomEvent(CacheMap cacheMap) {
        super(cacheMap);
    }

    protected CustomEvent(Element element, int typeInt, CacheMap cacheMap) {
        super(cacheMap);
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
}
