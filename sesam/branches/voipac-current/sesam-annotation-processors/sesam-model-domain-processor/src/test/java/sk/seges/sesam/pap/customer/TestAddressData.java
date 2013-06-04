/**
 * 
 */
package sk.seges.sesam.pap.customer;

import java.io.Serializable;

public interface TestAddressData extends Serializable {

	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";
	public static final String STATE = "state";
	public static final String ZIP = "zip";

	String getStreet();

	void setStreet(String street);

	String getCity();

	void setCity(String city);

	TestCountryData<?> getCountry();

	void setCountry(TestCountryData<?> country);

	String getState();

	void setState(String state);

	String getZip();

	void setZip(String zip);
}
