package sk.seges.acris.widget.client.advanced;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.DoubleBox;

/**
 * extends {@link DoubleBox}, allows only digits: [0..9,'.', ',']
 * 
 * @author psenicka
 *
 */
public class DoubleTextBox extends DoubleBox {
	
	public DoubleTextBox() {
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

			// check for decimal ','
			if (('.' == charCode || ',' == charCode) && (getValue() == null || (!getText().contains(",") && !getText().contains(".")))) {
				return;
			}

			// filter out non-digits
			if (Character.isDigit(charCode)) {
				return;
			}

			cancelKey();
		}
	};
}
