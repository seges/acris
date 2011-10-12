package sk.seges.acris.scaffold.model.view;

import sk.seges.acris.scaffold.annotation.FieldComposer;
import sk.seges.acris.scaffold.annotation.Path;

public class CustomerDisplayName {
	@FieldComposer
	public static String compose(@Path("customer.firstName") String firstName,
			@Path("customer.surname") String surname) {
		return firstName + " " + surname;
	}
}