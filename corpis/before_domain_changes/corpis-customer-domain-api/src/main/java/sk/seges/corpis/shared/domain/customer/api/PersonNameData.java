/**
 * 
 */
package sk.seges.corpis.shared.domain.customer.api;

import java.io.Serializable;

import sk.seges.corpis.shared.domain.customer.api.Salutation;


/**
 * @author ladislav.gazo
 */
public interface PersonNameData extends Serializable {
	public static final String FIRST_NAME = "firstName";
	public static final String SURNAME = "surname";
	public static final String SALUTATION = "salutation";

	String getFirstName();
	void setFirstName(String firstName);
	
	String getSurname();
	void setSurname(String surname);
	
	Salutation getSalutation();
	void setSalutation(Salutation salutation);
}
