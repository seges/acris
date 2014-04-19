package sk.seges.acris.player.client.model;

import com.google.gwt.user.client.Element;
import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

/**
 * Created by PeterSimun on 17.4.2014.
 */
public class ClipboardTestEvent extends ClipboardEvent {

    public static class MockCacheMap extends CacheMap {

        private final Element element;
        private final String xpath;

        public MockCacheMap(Element element, String xpath) {
            super(30);
            this.element = element;
            this.xpath = xpath;
        }

        @Override
        public Element resolveElement(String xpath) {
            return element;
        }

        @Override
        public String resolveXpath(Element element) {
            return xpath;
        }
    }

    public ClipboardTestEvent(int typeInt) {
        this(new MockElement(), "//div[id='testxpath']", typeInt);
    }

    protected ClipboardTestEvent(MockElement element, String xpath, int typeInt) {
        super(element, typeInt, new MockCacheMap(element, xpath));
    }
}
