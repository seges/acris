package sk.seges.acris.player.client;

import com.google.gwt.dom.client.BrowserEvents;
import org.junit.Test;
import sk.seges.acris.player.client.model.ClipboardTestEvent;
import sk.seges.acris.player.client.model.HtmlTestEvent;
import sk.seges.acris.player.client.model.KeyboardTestEvent;
import sk.seges.acris.player.client.model.MouseTestEvent;
import sk.seges.acris.recorder.client.event.ClipboardEvent;
import sk.seges.acris.recorder.client.event.KeyboardEvent;

public class DecodingTest extends AbstractDecodingTest {

	@Test
	public void testHtmlEventDecode() {
		testEventDecoding(new HtmlTestEvent(BrowserEvents.BLUR));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.CHANGE));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.CONTEXTMENU));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.ERROR));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.FOCUS));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.LOAD));
		testEventDecoding(new HtmlTestEvent(BrowserEvents.SCROLL));
	}

	@Test
	public void testKeyboardEventDecode() {
		testEventDecoding(new KeyboardTestEvent(BrowserEvents.KEYDOWN));
		testEventDecoding(new KeyboardTestEvent(BrowserEvents.KEYDOWN, 33));
		KeyboardTestEvent keyboardTestEvent = new KeyboardTestEvent(BrowserEvents.KEYDOWN);
		keyboardTestEvent.setCtrlKeyInt(1);
		keyboardTestEvent.setShiftKeyInt(1);
		testEventDecoding(keyboardTestEvent);
		testEventDecoding(new KeyboardTestEvent(BrowserEvents.KEYPRESS));
		keyboardTestEvent = new KeyboardTestEvent(BrowserEvents.KEYPRESS);
		keyboardTestEvent.setMetaKeyInt(1);
		keyboardTestEvent.setAltKeyInt(1);
		keyboardTestEvent.setCtrlKeyInt(1);
		testEventDecoding(keyboardTestEvent);
		testEventDecoding(new KeyboardTestEvent(BrowserEvents.KEYUP));

        keyboardTestEvent = new KeyboardTestEvent(BrowserEvents.KEYUP);
        keyboardTestEvent.setScrollType(KeyboardEvent.ScrollType.Y);
        keyboardTestEvent.setScrollOffset(20);
        testEventDecoding(keyboardTestEvent);

        keyboardTestEvent = new KeyboardTestEvent(BrowserEvents.KEYUP);
        keyboardTestEvent.setScrollType(KeyboardEvent.ScrollType.X);
        keyboardTestEvent.setScrollOffset(100);
        testEventDecoding(keyboardTestEvent);
	}

	@Test
	public void testMouseEventsDecode() {
		testEventDecoding(new MouseTestEvent(BrowserEvents.CLICK));
		testEventDecoding(new MouseTestEvent(BrowserEvents.DBLCLICK));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEDOWN));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEMOVE));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEOUT));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEOVER));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEUP));
		testEventDecoding(new MouseTestEvent(BrowserEvents.MOUSEWHEEL));
		testEventDecoding(new MouseTestEvent(BrowserEvents.CLICK, 2110, 1400));
		MouseTestEvent mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, 2110, 1400);
		mouseTestEvent.setCtrlKeyInt(1);
		mouseTestEvent.setAltKeyInt(1);
		mouseTestEvent.setRelativeInt(1);
		testEventDecoding(mouseTestEvent);

        mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, -95, 215);
        mouseTestEvent.setButton(1);
        mouseTestEvent.setRelativeInt(1);
        mouseTestEvent.setCtrlKeyInt(0);
        mouseTestEvent.setAltKeyInt(1);
        mouseTestEvent.setShiftKeyInt(0);
        mouseTestEvent.setMetaKeyInt(0);
        testEventDecoding(mouseTestEvent);

        mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, 234, -6);
        mouseTestEvent.setButton(1);
        mouseTestEvent.setRelativeInt(1);
        mouseTestEvent.setCtrlKeyInt(0);
        mouseTestEvent.setAltKeyInt(0);
        mouseTestEvent.setShiftKeyInt(0);
        mouseTestEvent.setMetaKeyInt(0);
        testEventDecoding(mouseTestEvent);

        mouseTestEvent = new MouseTestEvent(BrowserEvents.MOUSEOUT, -173, 254);
        mouseTestEvent.setButton(1);
        mouseTestEvent.setRelativeInt(1);
        mouseTestEvent.setCtrlKeyInt(0);
        mouseTestEvent.setAltKeyInt(0);
        mouseTestEvent.setShiftKeyInt(0);
        mouseTestEvent.setMetaKeyInt(0);
        testEventDecoding(mouseTestEvent);
	}

    @Test
    public void testClipboardEventsDecode() {
        testEventDecoding(new ClipboardTestEvent(ClipboardEvent.PASTE_EVENT_TYPE));
        testEventDecoding(new ClipboardTestEvent(ClipboardEvent.CUT_EVENT_TYPE));

        ClipboardTestEvent clipboardTestEvent = new ClipboardTestEvent(ClipboardEvent.PASTE_EVENT_TYPE);
        clipboardTestEvent.setSelectionStart(107);
        clipboardTestEvent.setSelectionEnd(302);
        testEventDecoding(clipboardTestEvent);
    }
}