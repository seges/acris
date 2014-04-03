package sk.seges.acris.recorder.client.test;

import com.google.gwt.dom.client.BrowserEvents;
import org.junit.Test;
import sk.seges.acris.recorder.client.model.HtmlTestEvent;
import sk.seges.acris.recorder.client.model.KeyboardTestEvent;
import sk.seges.acris.recorder.client.model.MouseTestEvent;

public class EncodingTest extends AbstractEventTest {

	@Test
	public void testEncodeHtmlEvents() {
		testEvent(new HtmlTestEvent(BrowserEvents.BLUR), "01111111111111111111111111111000");
		testEvent(new HtmlTestEvent(BrowserEvents.CHANGE), "01111111111111111111111111111001");
		testEvent(new HtmlTestEvent(BrowserEvents.CONTEXTMENU), "01111111111111111111111111111010");
		testEvent(new HtmlTestEvent(BrowserEvents.ERROR), "01111111111111111111111111111011");
		testEvent(new HtmlTestEvent(BrowserEvents.FOCUS), "01111111111111111111111111111100");
		testEvent(new HtmlTestEvent(BrowserEvents.LOAD), "01111111111111111111111111111101");
		testEvent(new HtmlTestEvent(BrowserEvents.SCROLL), "01111111111111111111111111111110");
	}

	@Test
	public void testEncodeKeyboardEvents() {
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN), "01001000001111001011011111000000");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN, 33, '6'), "01000110110111001000011111000000");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYDOWN) {
			@Override
			public int getCtrlKeyInt() {
				return 1;
			}

			@Override
			public int getShiftKeyInt() {
				return 1;
			}
		}, "01001000001111001011011111010100");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYPRESS), "01001000001111001011011111000001");
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
		}, "01001000001111001011011111101101");
		testEvent(new KeyboardTestEvent(BrowserEvents.KEYUP), "01001000001111001011011111000010");
	}

	@Test
	public void testEncodeMouseEvents() {
		testEvent(new MouseTestEvent(BrowserEvents.CLICK), "1011111110001011100000101111100010001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.DBLCLICK), "1011111110001011100000101111100110001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEDOWN), "1011111110001011100000101111101010001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEMOVE), "1011111110001011100000101111101110001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOUT), "1011111110001011100000101111110010001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOVER), "1011111110001011100000101111110110001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEUP), "1011111110001011100000101111111010001010100101001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEWHEEL), "1011111110001011100000101111111110001010100101001111111111100000");

		testEvent(new MouseTestEvent(BrowserEvents.CLICK, 2560, 1600), "1011111110010100100000001111100010001100110000001111111111100000");
		testEvent(new MouseTestEvent(BrowserEvents.MOUSEOUT, 2560, 1600) {
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
		}, "1011111110010100100000001111110010001100110000001111111111110011");

	}
}