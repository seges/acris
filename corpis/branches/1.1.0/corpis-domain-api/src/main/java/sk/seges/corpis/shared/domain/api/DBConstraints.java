package sk.seges.corpis.shared.domain.api;


public class DBConstraints {

	public static final int COUNTRY_LENGTH = 3;
	public static final int LANGUAGE_LENGTH = 8;
	public static final int ANALYTICS_LENGTH = 4096;
	public static final int DOMAIN_LENGTH = 8;
	
	// Address
	public static final byte ZIP_LENGTH = 5;
	
	// CompanyName
	public static final byte COMPANY_LENGTH = 50;
	
	// CustomerBase
	public static final byte SHORTCUT_LENGTH = 10;
	public static final String CORRESP_TABLE_PREFIX = "corresp_";

	// Person
	public static final byte PERSON_LENGTH = 50;
}