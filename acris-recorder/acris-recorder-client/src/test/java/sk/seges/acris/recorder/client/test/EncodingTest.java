package sk.seges.acris.recorder.client.test;

import com.google.gwt.dom.client.BrowserEvents;
import org.junit.Test;
import sk.seges.acris.recorder.client.model.HtmlTestEvent;
import sk.seges.acris.recorder.client.model.KeyboardTestEvent;
import sk.seges.acris.recorder.client.model.MouseTestEvent;

public class EncodingTest extends AbstractEventTest {

	@Test
	public void testEncodeHtmlEvents() {
		testEvent(new HtmlTestEvent(BrowserEvents.BLUR), "01100000000000000000000000000000");
		testEvent(new HtmlTestEvent(BrowserEvents.CHANGE), "01100000000000000000000000000001");
		testEvent(new HtmlTestEvent(BrowserEvents.CONTEXTMENU), "01100000000000000000000000000010");
		testEvent(new HtmlTestEvent(BrowserEvents.ERROR), "01100000000000000000000000000011");
		testEvent(new HtmlTestEvent(BrowserEvents.FOCUS), "01100000000000000000000000000100");
		testEvent(new HtmlTestEvent(BrowserEvents.LOAD), "01100000000000000000000000000101");
		testEvent(new HtmlTestEvent(BrowserEvents.SCROLL), "01100000000000000000000000000110");
	}

	@Test
	public void testEncodeKeyboardEvents() {
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN), "01000000000100000100101101000000");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN, 33, '6'), "01000000000011011000100001000000");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN) {
			@Override
			public int getCtrlKeyInt() {
				return 1;
			}

			@Override
			public int getShiftKeyInt() {
				return 1;
			}
		}, "01000000000100000100101101010100");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYPRESS), "01000000000100000100101101000001");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYPRESS) {
			@Override
			public int getMetaKeyInt() {
				return 1;
			}

			@Override
			public int getAltKeyInt() {
				return 1;
			}

			@Override
			public int getCtrlKeyInt() {
				return 1;
			}
		}, "01000000000100000100101101101101");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYUP), "01000000000100000100101101000010");
	}

	@Test
	public void testEncodeMouseEvents() {
		testEvent(new MouseTestEvent(BrowserEvents.CLICK), "0011000000000001000000000010000010000001001011000000001100100000");
		testEvent(new MouseTestEvent(BrowserEvents.DBLCLICK), "0011000000000001000000000010000010000001001011000000001100100001");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEDOWN), "0011000000000001000000000010000010000001001011000000001100100010");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEMOVE), "0011000000000001000000000010000010000001001011000000001100100011");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOUT), "0011000000000001000000000010000010000001001011000000001100100100");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOVER), "0011000000000001000000000010000010000001001011000000001100100101");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEUP), "0011000000000001000000000010000010000001001011000000001100100110");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEWHEEL), "0011000000000001000000000010000010000001001011000000001100100111");

		testEvent(new MouseTestEvent(BrowserEvents.CLICK, 2560, 1600, 2110, 1400), "0110010000000010100000000010000010000101011110000100000111110000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOUT, 2560, 1600, 2110, 1400) {
			@Override
			public int getCtrlKeyInt() {
				return 1;
			}

			@Override
			public int getAltKeyInt() {
				return 1;
			}

			@Override
			public int getRelativeInt() {
				return 1;
			}
		}, "0110010000000010100000000011001110000101011110000100000111110100");

	}
}