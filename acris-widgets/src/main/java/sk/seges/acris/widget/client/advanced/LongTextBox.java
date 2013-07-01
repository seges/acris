package sk.seges.acris.widget.client.advanced;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.LongBox;

/**
 * extends {@link LongBox}, allows only digits: [0..9]
 * 
 * 
 *
 */
public class LongTextBox extends LongBox {
	
	public LongTextBox() {
		addKeyPressHandler(keyPressHandler);
	}
	
	
	private KeyPressHandler keyPressHandler = new KeyPressHandler() {
		@Override
		public void onKeyPress(KeyPressEvent event) {

			if (isReadOnly() || !isEnabled()) {
				return;
			}

			int keyCode = event.getNativeEvent().getKeyCode();

			switch (keyCode) {
			case KeyCodes.KEY_LEFT:
			case KeyCodes.KEY_RIGHT:
			case KeyCodes.KEY_BACKSPACE:
				// case KeyCodes.KEY_DELETE:
			case KeyCodes.KEY_TAB:
			case KeyCodes.KEY_UP:
			case KeyCodes.KEY_DOWN:
				return;
			}

			char charCode = event.getCharCode();
			
			// filter out non-digits
			if (Character.isDigit(charCode)) {
				return;
			}

			cancelKey();
		}
	};
}
