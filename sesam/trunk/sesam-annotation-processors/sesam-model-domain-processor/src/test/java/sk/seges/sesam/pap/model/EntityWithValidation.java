package sk.seges.sesam.pap.model;

import java.util.Date;

import sk.seges.sesam.pap.validation.annotation.NotNull;
import sk.seges.sesam.pap.validation.annotation.Pattern;
import sk.seges.sesam.pap.validation.annotation.Size;

public class EntityWithValidation extends Entity {

	@Size(min = 0, max = 255)
	public String getWebId() {
		return super.getWebId();
	}

	@NotNull
	@Size(min = 1, max = 255)
	@Pattern(regexp = "[a-zA-Z0-9\\-_]*")
	public String getNiceurl() {
		return super.getNiceurl();
	}

	@NotNull
	public Date getDate() {
		return super.getDate();
	}
}