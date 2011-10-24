package sk.seges.acris.common.etc;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public enum Countries {
    AFGHANISTAN("AFGHANISTAN", false, 5, "en", "af", "af"),
    ALBANIA("ALBANIA", false, 6, "sq", "AL", "al"),
    ALGERIA("ALGERIA", false, 7, "ar", "DZ", "dz"),
    AMERICAN_SAMOA("AMERICAN SAMOA", false, 8, "en", "as", "as"),
    ANDORRA("ANDORRA", false, 9, "en", "ad", "ad"),
    ANGOLA("ANGOLA", false, 10, "en", "ao", "ao"),
    ANGUILLA("ANGUILLA", false, 11, "en", "ai", "ai"),
    ANTIGUA_AND_BARBUDA("ANTIGUA AND BARBUDA", false, 12, "en", "ag", "ag"),
    ARGENTINA("ARGENTINA", false, 13, "es", "AR", "ar"),
    ARMENIA("ARMENIA", false, 14, "en", "am", "am"),
    ARUBA("ARUBA", false, 15, "en", "aw", "aw"),
    AUSTRALIA("AUSTRALIA", false, 16, "en", "au", "au"),
    AUSTRIA("AUSTRIA", true, 17, "de", "at", "at"),
    AZERBAIJAN("AZERBAIJAN", false, 18, "en", "az", "az"),
    BAHAMAS("BAHAMAS", false, 20, "en", "bs", "bs"),
    BAHRAIN("BAHRAIN", false, 21, "ar", "bh", "bh"),
    BANGLADESH("BANGLADESH", false, 22, "en", "db", "bd"),
    BARBADOS("BARBADOS", false, 23, "en", "bb", "bb"),
    BELARUS("BELARUS", false, 26, "be", "by", "by"),
    BELGIUM("BELGIUM", true, 24, "fr", "be", "be"),
    BELIZE("BELIZE", false, 25, "en", "bz", "bz"),
    BENIN("BENIN", false, 27, "en", "bj", "bj"),
    BERMUDA("BERMUDA", false, 28, "en", "bm", "bm"),
    BHUTAN("BHUTAN", false, 29, "en", "bt", "bt"),
    BOLIVIA("BOLIVIA", false, 30, "es", "bo", "bo"),
    BOSNIA_AND_HERZEGOWINA("BOSNIA AND HERZEGOWINA", false, 32, "sr", "ba", "ba"),
    BOTSWANA("BOTSWANA", false, 33, "en", "bw", "bw"),
    BRAZIL("BRAZIL", false, 34, "en", "br", "br"),
    BRUNEI_DARUSSALAM("BRUNEI DARUSSALAM", false, 36, "en", "bn", "bn"),
    BULGARIA("BULGARIA", true, 37, "en", "bg", "bg"),
    BURKINA_FASO("BURKINA FASO", false, 38, "en", "bf", "bf"),
    BURUNDI("BURUNDI", false, 39, "en", "bi", "bi"),
    CAMBODIA("CAMBODIA", false, 40, "en", "kh", "kh"),
    CAMEROON("CAMEROON", false, 41, "en", "cm", "cm"),
    CANADA("CANADA", false, 42, "en", "ca", "ca"),
    CAPE_VERDE("CAPE VERDE", false, 44, "en", "cv", "cv"),
    CAYMAN_ISLANDS("CAYMAN ISLANDS", false, 45, "en", "ky", "ky"),
    CENTRAL_AFRICAN_REPUBLIC("CENTRAL AFRICAN REPUBLIC", false, 46, "en", "cf", "cf"),
    CHAD("CHAD", false, 96, "en", "td", "td"),
    CHILE("CHILE", false, 97, "en", "cl", "cl"),
    CHINA("CHINA", false, 98, "en", "cn", "cn"),
    COLOMBIA("COLOMBIA", false, 47, "en", "co", "co"),
    COMOROS("COMOROS", false, 48, "en", "km", "km"),
    CONGO("CONGO, Democratic Republic of (was Zaire)", false, 49, "en", "cg", "cg"),
    COOK_ISLANDS("COOK ISLANDS", false, 50, "en", "ck", "ck"),
    COSTA_RICA("COSTA RICA", false, 51, "en", "cr", "cr"),
    CROATIA("CROATIA", false, 52, "en", "hr", "hr"),
    CUBA("CUBA", false, 53, "en", "cu", "cu"),
    CYPRUS("CYPRUS", true, 56, "en", "cy", "cy"),
    CZECH_REPUBLIC("CZECH REPUBLIC", true, 57, "en", "cz", "cz"),
    DENMARK("DENMARK", true, 58, "en", "dk", "dk"),
    DJIBOUTI("DJIBOUTI", false, 59, "en", "dj", "dj"),
    DOMINICA("DOMINICA", false, 60, "en", "dm", "dm"),
    DOMINICAN_REPUBLIC("DOMINICAN REPUBLIC", false, 61, "en", "do", "do"),
    ECUADOR("ECUADOR", false, 62, "en", "ec", "ec"),
    EGYPT("EGYPT", false, 63, "en", "eg", "eg"),
    EL_SALVADOR("EL SALVADOR", false, 64, "en", "sv", "sv"),
    ERITREA("ERITREA", false, 65, "en", "er", "er"),
    ESTONIA("ESTONIA", true, 66, "en", "ee", "ee"),
    ETHIOPIA("ETHIOPIA", false, 67, "en", "et", "et"),
    FALKLAND_ISLANDS("FALKLAND ISLANDS (MALVINAS)", false, 68, "en", "fk", "fk"),
    FAROE_ISLANDS("FAROE ISLANDS", false, 69, "en", "fo", "fo"),
    FIJI("FIJI", false, 70, "en", "fj", "fj"),
    FINLAND("FINLAND", true, 71, "en", "fi", "fi"),
    FRANCE("FRANCE", true, 72, "en", "fr", "fr"),
    FRENCH_GUIANA("FRENCH GUIANA", false, 73, "en", "gf", "gf"),
    FRENCH_POLYNESIA("FRENCH POLYNESIA", false, 74, "en", "pf", "pf"),
    GABON("GABON", false, 75, "en", "ga", "ga"),
    GAMBIA("GAMBIA", false, 225, "en", "gm", "gm"),
    GEORGIA("GEORGIA", false, 77, "en", "ge", "ge"),
    GERMANY("GERMANY", true, 78, "en", "de", "de"),
    GHANA("GHANA", false, 79, "en", "gh", "gh"),
    GIBRALTAR("GIBRALTAR", false, 80, "en", "gi", "gi"),
    GREECE("GREECE", true, 81, "en", "gr", "gr"),
    GREENLAND("GREENLAND", false, 82, "en", "gl", "gl"),
    GRENADA("GRENADA", false, 83, "en", "gd", "gd"),
    GUADELOUPE("GUADELOUPE", false, 84, "en", "gp", "gp"),
    GUAM("GUAM", false, 85, "en", "gu", "gu"),
    GUATEMALA("GUATEMALA", false, 86, "en", "gt", "gt"),
    GUINEA("GUINEA", false, 88, "en", "gn", "gn"),
    GUINEA_BISSAU("GUINEA-BISSAU", false, 89, "en", "gw", "gw"),
    GUYANA("GUYANA", false, 91, "en", "gy", "gy"),
    HAITI("HAITI", false, 92, "en", "ht", "ht"),
    HONDURAS("HONDURAS", false, 93, "en", "hn", "hn"),
    HONG_KONG("HONG KONG", false, 94, "en", "hk", "hk"),
    HUNGARY("HUNGARY", true, 95, "en", "hu", "hu"),
    ICELAND("ICELAND", false, 99, "en", "is", "is"),
    INDIA("INDIA", false, 100, "en", "in", "in"),
    INDONESIA("INDONESIA", false, 101, "en", "id", "id"),
    IRAN("IRAN (ISLAMIC REPUBLIC OF)", false, 102, "en", "ir", "ir"),
    IRAQ("IRAQ", false, 103, "en", "iq", "iq"),
    IRELAND("IRELAND", true, 104, "en", "ie", "ie"),
    ISRAEL("ISRAEL", false, 105, "en", "il", "il"),
    ITALY("ITALY", true, 106, "en", "it", "it"),
    JAMAICA("JAMAICA", false, 108, "en", "jm", "jm"),
    JAPAN("JAPAN", false, 109, "en", "jp", "jp"),
    JORDAN("JORDAN", false, 111, "en", "jo", "jo"),
    KAZAKHSTAN("KAZAKHSTAN", false, 112, "en", "kz", "kz"),
    KENYA("KENYA", false, 113, "en", "ke", "ke"),
    KIRIBATI("KIRIBATI", false, 114, "en", "ki", "ki"),
    KOREA("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", false, 115, "en", "kp", "kp"),
    KUWAIT("KUWAIT", false, 118, "en", "kw", "kw"),
    KYRGYZSTAN("KYRGYZSTAN", false, 119, "en", "kg", "kg"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("LAO PEOPLE'S DEMOCRATIC REPUBLIC", false, 120, "en", "la", "la"),
    LATVIA("LATVIA", true, 121, "en", "lv", "lv"),
    LEBANON("LEBANON", false, 122, "en", "lb", "lb"),
    LESOTHO("LESOTHO", false, 123, "en", "ls", "ls"),
    LIBERIA("LIBERIA", false, 124, "en", "lr", "lr"),
    LIBYAN_ARAB_JAMAHIRIYA("LIBYAN ARAB JAMAHIRIYA", false, 125, "en", "ly", "ly"),
    LIECHTENSTEIN("LIECHTENSTEIN", false, 126, "en", "li", "li"),
    LITHUANIA("LITHUANIA", true, 127, "en", "lt", "lt"),
    LUXEMBOURG("LUXEMBOURG", true, 128, "en", "lu", "lu"),
    MACAU("MACAU", false, 129, "en", "mo", "mo"),
    MACEDONIA("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", false, 130, "en", "mk", "mk"),
    MADAGASCAR("MADAGASCAR", false, 131, "en", "mg", "mg"),
    MALAWI("MALAWI", false, 133, "en", "mw", "mw"),
    MALAYSIA("MALAYSIA", false, 134, "en", "my", "my"),
    MALDIVES("MALDIVES", false, 135, "en", "mv", "mv"),
    MALI("MALI", false, 136, "en", "ml", "ml"),
    MALTA("MALTA", true, 137, "en", "mt", "mt"),
    MARSHALL_ISLANDS("MARSHALL ISLANDS", false, 138, "en", "mh", "mh"),
    MARTINIQUE("MARTINIQUE", false, 139, "en", "mq", "mq"),
    MAURITANIA("MAURITANIA", false, 140, "en", "mr", "mr"),
    MAURITIUS("MAURITIUS", false, 141, "en", "mu", "mu"),
    MAYOTTE("MAYOTTE", false, 142, "en", "yt", "yt"),
    MEXICO("MEXICO", false, 144, "en", "mx", "mx"),
    MICRONESIA("MICRONESIA, FEDERATED STATES OF", false, 145, "en", "fm", "fm"),
    MOLDOVA("MOLDOVA, REPUBLIC OF", false, 146, "en", "md", "md"),
    MONACO("MONACO", false, 147, "en", "mc", "mc"),
    MONGOLIA("MONGOLIA", false, 148, "en", "mn", "mn"),
    MONTSERRAT("MONTSERRAT", false, 150, "en", "ms", "ms"),
    MOROCCO("MOROCCO", false, 151, "en", "ma", "ma"),
    MOZAMBIQUE("MOZAMBIQUE", false, 152, "en", "mz", "mz"),
    MYANMAR("MYANMAR", false, 153, "en", "mm", "mm"),
    NAMIBIA("NAMIBIA", false, 154, "en", "na", "na"),
    NAURU("NAURU", false, 155, "en", "nr", "nr"),
    NEPAL("NEPAL", false, 156, "en", "np", "np"),
    NETHERLANDS("NETHERLANDS", true, 157, "en", "nl", "nl"),
    NETHERLANDS_ANTILLES("NETHERLANDS ANTILLES", false, 158, "en", "an", "an"),
    NEW_CALEDONIA("NEW CALEDONIA", false, 160, "en", "nc", "nc"),
    NEW_ZEALAND("NEW ZEALAND", false, 161, "en", "nz", "nz"),
    NICARAGUA("NICARAGUA", false, 162, "en", "ni", "ni"),
    NIGER("NIGER", false, 163, "en", "ne", "ne"),
    NIGERIA("NIGERIA", false, 164, "en", "ng", "ng"),
    NIUE("NIUE", false, 165, "en", "nu", "nu"),
    NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", false, 194, "en", "mp", "mp"),
    NORWAY("NORWAY", false, 166, "en", "no", "no"),
    OMAN("OMAN", false, 167, "en", "om", "om"),
    PAKISTAN("PAKISTAN", false, 168, "en", "pk", "pk"),
    PALAU("PALAU", false, 169, "en", "pw", "pw"),
    PALESTINIAN("PALESTINIAN TERRITORY, Occupied", false, 170, "en", "ps", "ps"),
    PANAMA("PANAMA", false, 171, "en", "pa", "pa"),
    PAPUA_NEW_GUINEA("PAPUA NEW GUINEA", false, 172, "en", "pg", "pg"),
    PARAGUAY("PARAGUAY", false, 173, "en", "py", "py"),
    PERU("PERU", false, 174, "en", "pe", "pe"),
    PHILIPPINES("PHILIPPINES", false, 175, "en", "ph", "ph"),
    POLAND("POLAND", true, 176, "en", "pl", "pl"),
    PORTUGAL("PORTUGAL", true, 177, "en", "pt", "pt"),
    PUERTO_RICO("PUERTO RICO", false, 178, "en", "pr", "pr"),
    QATAR("QATAR", false, 179, "en", "qa", "qa"),
    REUNION("REUNION", false, 180, "en", "re", "re"),
    ROMANIA("ROMANIA", true, 181, "en", "ro", "ro"),
    RUSSIAN_FEDERATION("RUSSIAN FEDERATION", false, 183, "en", "ru", "ru"),
    RWANDA("RWANDA", false, 184, "en", "rw", "rw"),
    SAINT_KITTS_AND_NEVIS("SAINT KITTS AND NEVIS", false, 188, "en", "kn", "kn"),
    SAINT_LUCIA("SAINT LUCIA", false, 189, "en", "lc", "lc"),
    SAINT_PIERRE_AND_MIQUELON("SAINT PIERRE AND MIQUELON", false, 192, "en", "pm", "pm"),
    SAINT_VINCENT_AND_THE_GRENADINES("SAINT VINCENT AND THE GRENADINES", false, 193, "en", "vc", "vc"),
    SAN_MARINO("SAN MARINO", false, 197, "en", "sm", "sm"),
    SAO_TOME_AND_PRINCIPE("SAO TOME AND PRINCIPE", false, 198, "en", "st", "st"),
    SAUDI_ARABIA("SAUDI ARABIA", false, 199, "en", "sa", "sa"),
    SENEGAL("SENEGAL", false, 201, "en", "sn", "sn"),
    SERBIA_AND_MONTENEGRO("SERBIA AND MONTENEGRO", false, 202), //FIXME Serbia, Montenegro are 2 dif countries
    SEYCHELLES("SEYCHELLES", false, 203, "en", "sc", "sc"),
    SIERRA_LEONE("SIERRA LEONE", false, 204, "en", "sl", "sl"),
    SINGAPORE("SINGAPORE", false, 205, "en", "sg", "sg"),
    SLOVAKIA("SLOVAKIA", true, 206, "sk", "sk", "sk"),
    SLOVENIA("SLOVENIA", true, 207, "en", "si", "si"),
    SOLOMON_ISLANDS("SOLOMON ISLANDS", false, 208, "en", "sb", "sb"),
    SOMALIA("SOMALIA", false, 209, "en", "so", "so"),
    SOUTH_AFRICA("SOUTH AFRICA", false, 210, "en", "za", "za"),
    SPAIN("SPAIN", true, 212, "en", "es", "es"),
    SRI_LANKA("SRI LANKA", false, 213, "en", "lk", "lk"),
    SUDAN("SUDAN", false, 214, "en", "sd", "sd"),
    SURINAME("SURINAME", false, 215, "en", "sr", "sr"),
    SWAZILAND("SWAZILAND", false, 216, "en", "cz", "cz"),
    SWEDEN("SWEDEN", true, 217, "en", "se", "se"),
    SWITZERLAND("SWITZERLAND", false, 218, "en", "ch", "ch"),
    SYRIAN_ARAB_REPUBLIC("SYRIAN ARAB REPUBLIC", false, 219, "en", "sy", "sy"),
    TAIWAN("TAIWAN", false, 221, "en", "tw", "tw"),
    TAJIKISTAN("TAJIKISTAN", false, 222, "en", "tj", "tj"),
    TANZANIA("TANZANIA, UNITED REPUBLIC OF", false, 223, "en", "tz", "tz"),
    THAILAND("THAILAND", false, 224, "en", "th", "th"),
    TOGO("TOGO", false, 227, "en", "tg", "tg"),
    TONGA("TONGA", false, 228, "en", "to", "to"),
    TRINIDAD_AND_TOBAGO("TRINIDAD AND TOBAGO", false, 229, "en", "tt", "tt"),
    TUNISIA("TUNISIA", false, 230, "en", "tn", "tn"),
    TURKEY("TURKEY", false, 231, "en", "tr", "tr"),
    TURKMENISTAN("TURKMENISTAN", false, 232, "en", "tm", "tm"),
    TURKS_AND_CAICOS_ISLANDS("TURKS AND CAICOS ISLANDS", false, 233, "en", "tc", "tc"),
    TUVALU("TUVALU", false, 234, "en", "tv", "tv"),
    UGANDA("UGANDA", false, 235, "en", "ug", "ug"),
    UKRAINE("UKRAINE", false, 236, "en", "ua", "ua"),
    UNITED_ARAB_EMIRATES("UNITED ARAB EMIRATES", false, 237, "en", "ae", "ae"),
    UNITED_KINGDOM("UNITED KINGDOM", true, 238, "en", "uk", "uk"),
    UNITED_STATES("UNITED STATES", false, 241, "en", "us", "us"),
    URUGUAY("URUGUAY", false, 240, "en", "uy", "uy"),
    UZBEKISTAN("UZBEKISTAN", false, 242, "en", "uz", "uz"),
    VANUATU("VANUATU", false, 243, "en", "vu", "vu"),
    VATICAN("VATICAN CITY STATE (HOLY SEE)", false, 244, "en", "va", "va"),
    VENEZUELA("VENEZUELA", false, 245, "en", "ve", "ve"),
    VIETNAM("VIETNAM", false, 246, "en", "vn", "vn"),
    WALLIS_AND_FUTUNA_ISLANDS("WALLIS AND FUTUNA ISLANDS", false, 248, "en", "wf", "wf"),
    YEMEN("YEMEN", false, 252, "en", "ye", "ye"),
    ZAMBIA("ZAMBIA", false, 253, "en", "zm", "zm"),
    ZIMBABWE("ZIMBABWE", false, 254, "en", "zw", "zw")
	;

	private String label;
	private boolean europeanUnion;
	private int id;
	private String domain;
	private String country;
	private String lang;
	
	@Deprecated
	private static Map<String, Countries> countryMap = new HashMap<String, Countries>();

	static {
	    for(Countries country : Countries.values()) {
	        countryMap.put(country.name(), country);
	    }
	}

	@Deprecated
	private Countries(String label, boolean europeanUnion, int id){
		this.label = label;
		this.europeanUnion = europeanUnion;
		this.id = id;
	}

	private Countries(String label, boolean europeanUnion, int id, String lang, String country, String domain){
		this.label = label;
		this.europeanUnion = europeanUnion;
		this.id = id;
		this.domain = domain;
		this.lang = lang;
		this.country = country;
	}

	/**
	 * Use {@link #valueOf(String)}
	 * @param countryName
	 * @return
	 */
	@Deprecated
	public static Countries ofValue(String countryName) {
	    return countryMap.get(countryName);
	}

	public String getDomain() {
		return domain;
	}
	
	public String getLang() {
		return lang;
	}

	public String getCountry() {
		return country;
	}
	
	public String getLabel() {
		return label;
	}

	public boolean isEuropeanUnionCountry(){
		return europeanUnion;
	}
	
	public Integer getId() {
	    return id;
	}

	public static void main(String[] args) {
        System.out.println(Countries.ofValue("ZAMBIA"));
        System.out.println(Countries.ofValue("AAA"));
    }
	
	public static Countries valueOfLang(String lang) {
		for(Countries country : Countries.values()) {
			if(lang.equals(country.getLang())) {
				return country;
			}
		}
		return null;
	}
}
