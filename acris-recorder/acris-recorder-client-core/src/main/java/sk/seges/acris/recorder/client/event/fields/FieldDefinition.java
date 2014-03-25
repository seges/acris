package sk.seges.acris.recorder.client.event.fields;

import java.io.Serializable;

public class FieldDefinition implements Serializable {

	private int position;
	private int length;
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}