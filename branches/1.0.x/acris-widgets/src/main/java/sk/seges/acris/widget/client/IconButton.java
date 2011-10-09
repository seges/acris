package sk.seges.acris.widget.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

public class IconButton extends PushButton {
	private Image image;
	private String IMAGE_BUTTON_STYLE="acris-icon-button-style";

	public IconButton() {
		super();
	}

	public IconButton(Image image) {
		this();
		setImage(image);
	}

	public IconButton(AbstractImagePrototype image) {
		this();
		setImage(image);
	}

	public IconButton(ImageResource image) {
		this();
		setImage(image);
	}

	public Image getImage() {
		return image;
	}

	@Override
	public void setText(String text) {
		if (image == null) {
			super.setText(text);
		} else {
			super.setHTML(image.getElement().getString() + text);
		}
	}
	
	@Override
	public void setHTML(String html) {
		if (image == null) {
			super.setHTML(html);
		} else {
			super.setHTML(image.getElement().getString() + html);
		}
	}
	
	public void setImage(AbstractImagePrototype image) {
		setImage(image.createImage());
	}

	public void setImage(ImageResource image) {
		setImage(new Image(image));
	}

	public void setImage(Image image) {
		this.image = image;
		image.addStyleName(IMAGE_BUTTON_STYLE);
		String text = getText();
		if (text == null) {
			text = "";
		}
		super.setHTML(image.getElement().getString() + text);
	}
}