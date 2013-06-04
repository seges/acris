/**
 * 
 */
package sk.seges.corpis.shared.domain.customer.api;

import java.io.Serializable;

import sk.seges.corpis.shared.domain.api.CountryData;



/**
 * @author ladislav.gazo
 */
public interface AddressData extends Serializable {
	public static final String STREET = "street";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";
	public static final String STATE = "state";
	public static final String ZIP = "zip";

	String getStreet();
	void setStreet(String street);
	
	String getCity();
	void setCity(String city);
	
	CountryData<?> getCountry();
	void setCountry(CountryData<?> country);
	
	String getState();
	void setState(String state);
	
	String getZip();
	void setZip(String zip);
}
