package sk.seges.corpis.domain.pay.tatra;

/**
 * @author spok </br> <a
 *         href="http://www.tatrabanka.sk/tatrapay/TatraPay_technicka_prirucka.pdf"
 *         >http://www.tatrabanka.sk/tatrapay/TatraPay_technicka_prirucka.pdf</a
 *         >
 * 
 */
public enum TatraPayParameter {
    
    /**
     * payment type, constraint: only TatraPay, example: TatraPay
     */
    PT("PT"),

    /**
     * merchant identification, length: 3-4, example: 123
     */
    MID("MID"),

    /**
     * amount, length: 13+2, example: 12345.50
     */
    AMT("AMT"),

    /**
     * currency, length: 3, SKK = 703, EUR = 978
     */
    CURR("CURR"),

    /**
     * variable symbol, length: max 10, example: 1234567890
     */
    VS("VS"),

    /**
     * specific symbol, length: max 10, example: 987654321
     */
    SS("SS"),

    /**
     * constant symbol, length: max 4, example: services = 0308, goods = 0008
     */
    CS("CS"),
    
    /**
     * return url, example: http://www.portabook.com
     */
    RURL("RURL"),
    
    /**
     * IP adresa klienta, example: 1.1.1.1
     */
    IPC("IPC"),
    
    /**
     * Meno klienta, example: Peter Novak
     */
    NAME("NAME"),
    
    /**
     * security signature, length: 16, example: A6BC1DE2FG4H8484
     */
    SIGN("SIGN"),

    /**
     *return short message system, length: max 15, constraint telephone number,
     * example: 091234567
     */
    RSMS("RSMS"),

    /**
     * return email, length: max 35, example: novak@domena.sk
     */
    REM("REM"),

    /**
     * description, length: max 20, constraint: no diacritic, example:
     * payment_for_books
     */
    DESC("DESC"),

    /**
     * automatic redirect, length: 1, examples: 0 - manual, 1 - automatic after
     * 9 seconds
     */
    AREDIR("AREDIR"),

    /**
     * language, length: 2, example: sk, en, de, hu
     */
    LANG("LANG"),
    
    /**
     *  result 
     */
    RESULT("RES"),
    APPROVAL_CODE("AC");
    
    private String name;

    
    private TatraPayParameter(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }
}
