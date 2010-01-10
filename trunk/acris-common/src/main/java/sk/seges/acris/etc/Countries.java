package sk.seges.acris.etc;

import java.util.HashMap;
import java.util.Map;

public enum Countries {
    AFGHANISTAN("AFGHANISTAN", false, 5, "af", "af"),
    ALBANIA("ALBANIA", false, 6, "al", "al"),
    ALGERIA("ALGERIA", false, 7, "dz", "dz"),
    AMERICAN_SAMOA("AMERICAN SAMOA", false, 8, "as", "as"),
    ANDORRA("ANDORRA", false, 9, "ad", "ad"),
    ANGOLA("ANGOLA", false, 10, "ao", "ao"),
    ANGUILLA("ANGUILLA", false, 11, "ai", "ai"),
    ANTIGUA_AND_BARBUDA("ANTIGUA AND BARBUDA", false, 12, "ag", "ag"),
    ARGENTINA("ARGENTINA", false, 13, "ar", "ar"),
    ARMENIA("ARMENIA", false, 14, "am", "am"),
    ARUBA("ARUBA", false, 15, "aw", "aw"),
    AUSTRALIA("AUSTRALIA", false, 16, "au", "au"),
    AUSTRIA("AUSTRIA", true, 17, "at", "at"),
    AZERBAIJAN("AZERBAIJAN", false, 18, "az", "az"),
    BAHAMAS("BAHAMAS", false, 20, "bs", "bs"),
    BAHRAIN("BAHRAIN", false, 21, "bh", "bh"),
    BANGLADESH("BANGLADESH", false, 22, "db", "bd"),
    BARBADOS("BARBADOS", false, 23, "bb", "bb"),
    BELARUS("BELARUS", false, 26, "by", "by"),
    BELGIUM("BELGIUM", true, 24, "be", "be"),
    BELIZE("BELIZE", false, 25, "bz", "bz"),
    BENIN("BENIN", false, 27, "bj", "bj"),
    BERMUDA("BERMUDA", false, 28, "bm", "bm"),
    BHUTAN("BHUTAN", false, 29, "bt", "bt"),
    BOLIVIA("BOLIVIA", false, 30, "bo", "bo"),
    BOSNIA_AND_HERZEGOWINA("BOSNIA AND HERZEGOWINA", false, 32, "ba", "ba"),
    BOTSWANA("BOTSWANA", false, 33, "bw", "bw"),
    BRAZIL("BRAZIL", false, 34, "br", "br"),
    BRUNEI_DARUSSALAM("BRUNEI DARUSSALAM", false, 36, "bn", "bn"),
    BULGARIA("BULGARIA", true, 37, "bg", "bg"),
    BURKINA_FASO("BURKINA FASO", false, 38, "bf", "bf"),
    BURUNDI("BURUNDI", false, 39, "bi", "bi"),
    CAMBODIA("CAMBODIA", false, 40, "kh", "kh"),
    CAMEROON("CAMEROON", false, 41, "cm", "cm"),
    CANADA("CANADA", false, 42, "ca", "ca"),
    CAPE_VERDE("CAPE VERDE", false, 44, "cv", "cv"),
    CAYMAN_ISLANDS("CAYMAN ISLANDS", false, 45, "ky", "ky"),
    CENTRAL_AFRICAN_REPUBLIC("CENTRAL AFRICAN REPUBLIC", false, 46, "cf", "cf"),
    CHAD("CHAD", false, 96, "td", "td"),
    CHILE("CHILE", false, 97, "cl", "cl"),
    CHINA("CHINA", false, 98, "cn", "cn"),
    COLOMBIA("COLOMBIA", false, 47, "co", "co"),
    COMOROS("COMOROS", false, 48, "km", "km"),
    CONGO("CONGO, Democratic Republic of (was Zaire)", false, 49, "cg", "cg"),
    COOK_ISLANDS("COOK ISLANDS", false, 50, "ck", "ck"),
    COSTA_RICA("COSTA RICA", false, 51, "cr", "cr"),
    CROATIA("CROATIA", false, 52, "hr", "hr"),
    CUBA("CUBA", false, 53, "cu", "cu"),
    CYPRUS("CYPRUS", true, 56, "cy", "cy"),
    CZECH_REPUBLIC("CZECH REPUBLIC", true, 57, "cz", "cz"),
    DENMARK("DENMARK", true, 58, "dk", "dk"),
    DJIBOUTI("DJIBOUTI", false, 59, "dj", "dj"),
    DOMINICA("DOMINICA", false, 60, "dm", "dm"),
    DOMINICAN_REPUBLIC("DOMINICAN REPUBLIC", false, 61, "do", "do"),
    ECUADOR("ECUADOR", false, 62, "ec", "ec"),
    EGYPT("EGYPT", false, 63, "eg", "eg"),
    EL_SALVADOR("EL SALVADOR", false, 64, "sv", "sv"),
    ERITREA("ERITREA", false, 65, "er", "er"),
    ESTONIA("ESTONIA", true, 66, "ee", "ee"),
    ETHIOPIA("ETHIOPIA", false, 67, "et", "et"),
    FALKLAND_ISLANDS("FALKLAND ISLANDS (MALVINAS)", false, 68, "fk", "fk"),
    FAROE_ISLANDS("FAROE ISLANDS", false, 69, "fo", "fo"),
    FIJI("FIJI", false, 70, "fj", "fj"),
    FINLAND("FINLAND", true, 71, "fi", "fi"),
    FRANCE("FRANCE", true, 72, "fr", "fr"),
    FRENCH_GUIANA("FRENCH GUIANA", false, 73, "gf", "gf"),
    FRENCH_POLYNESIA("FRENCH POLYNESIA", false, 74, "pf", "pf"),
    GABON("GABON", false, 75, "ga", "ga"),
    GAMBIA("GAMBIA", false, 225, "gm", "gm"),
    GEORGIA("GEORGIA", false, 77, "ge", "ge"),
    GERMANY("GERMANY", true, 78, "de", "de"),
    GHANA("GHANA", false, 79, "gh", "gh"),
    GIBRALTAR("GIBRALTAR", false, 80, "gi", "gi"),
    GREECE("GREECE", true, 81, "gr", "gr"),
    GREENLAND("GREENLAND", false, 82, "gl", "gl"),
    GRENADA("GRENADA", false, 83, "gd", "gd"),
    GUADELOUPE("GUADELOUPE", false, 84, "gp", "gp"),
    GUAM("GUAM", false, 85, "gu", "gu"),
    GUATEMALA("GUATEMALA", false, 86, "gt", "gt"),
    GUINEA("GUINEA", false, 88, "gn", "gn"),
    GUINEA_BISSAU("GUINEA-BISSAU", false, 89, "gw", "gw"),
    GUYANA("GUYANA", false, 91, "gy", "gy"),
    HAITI("HAITI", false, 92, "ht", "ht"),
    HONDURAS("HONDURAS", false, 93, "hn", "hn"),
    HONG_KONG("HONG KONG", false, 94, "hk", "hk"),
    HUNGARY("HUNGARY", true, 95, "hu", "hu"),
    ICELAND("ICELAND", false, 99, "is", "is"),
    INDIA("INDIA", false, 100, "in", "in"),
    INDONESIA("INDONESIA", false, 101, "id", "id"),
    IRAN("IRAN (ISLAMIC REPUBLIC OF)", false, 102, "ir", "ir"),
    IRAQ("IRAQ", false, 103, "iq", "iq"),
    IRELAND("IRELAND", true, 104, "ie", "ie"),
    ISRAEL("ISRAEL", false, 105, "il", "il"),
    ITALY("ITALY", true, 106, "it", "it"),
    JAMAICA("JAMAICA", false, 108, "jm", "jm"),
    JAPAN("JAPAN", false, 109, "jp", "jp"),
    JORDAN("JORDAN", false, 111, "jo", "jo"),
    KAZAKHSTAN("KAZAKHSTAN", false, 112, "kz", "kz"),
    KENYA("KENYA", false, 113, "ke", "ke"),
    KIRIBATI("KIRIBATI", false, 114, "ki", "ki"),
    KOREA("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", false, 115, "kp", "kp"),
    KUWAIT("KUWAIT", false, 118, "kw", "kw"),
    KYRGYZSTAN("KYRGYZSTAN", false, 119, "kg", "kg"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("LAO PEOPLE'S DEMOCRATIC REPUBLIC", false, 120, "la", "la"),
    LATVIA("LATVIA", true, 121, "lv", "lv"),
    LEBANON("LEBANON", false, 122, "lb", "lb"),
    LESOTHO("LESOTHO", false, 123, "ls", "ls"),
    LIBERIA("LIBERIA", false, 124, "lr", "lr"),
    LIBYAN_ARAB_JAMAHIRIYA("LIBYAN ARAB JAMAHIRIYA", false, 125, "ly", "ly"),
    LIECHTENSTEIN("LIECHTENSTEIN", false, 126, "li", "li"),
    LITHUANIA("LITHUANIA", true, 127, "lt", "lt"),
    LUXEMBOURG("LUXEMBOURG", true, 128, "lu", "lu"),
    MACAU("MACAU", false, 129, "mo", "mo"),
    MACEDONIA("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", false, 130, "mk", "mk"),
    MADAGASCAR("MADAGASCAR", false, 131, "mg", "mg"),
    MALAWI("MALAWI", false, 133, "mw", "mw"),
    MALAYSIA("MALAYSIA", false, 134, "my", "my"),
    MALDIVES("MALDIVES", false, 135, "mv", "mv"),
    MALI("MALI", false, 136, "ml", "ml"),
    MALTA("MALTA", true, 137, "mt", "mt"),
    MARSHALL_ISLANDS("MARSHALL ISLANDS", false, 138, "mh", "mh"),
    MARTINIQUE("MARTINIQUE", false, 139, "mq", "mq"),
    MAURITANIA("MAURITANIA", false, 140, "mr", "mr"),
    MAURITIUS("MAURITIUS", false, 141, "mu", "mu"),
    MAYOTTE("MAYOTTE", false, 142, "yt", "yt"),
    MEXICO("MEXICO", false, 144, "mx", "mx"),
    MICRONESIA("MICRONESIA, FEDERATED STATES OF", false, 145, "fm", "fm"),
    MOLDOVA("MOLDOVA, REPUBLIC OF", false, 146, "md", "md"),
    MONACO("MONACO", false, 147, "mc", "mc"),
    MONGOLIA("MONGOLIA", false, 148, "mn", "mn"),
    MONTSERRAT("MONTSERRAT", false, 150, "ms", "ms"),
    MOROCCO("MOROCCO", false, 151, "ma", "ma"),
    MOZAMBIQUE("MOZAMBIQUE", false, 152, "mz", "mz"),
    MYANMAR("MYANMAR", false, 153, "mm", "mm"),
    NAMIBIA("NAMIBIA", false, 154, "na", "na"),
    NAURU("NAURU", false, 155, "nr", "nr"),
    NEPAL("NEPAL", false, 156, "np", "np"),
    NETHERLANDS("NETHERLANDS", true, 157, "nl", "nl"),
    NETHERLANDS_ANTILLES("NETHERLANDS ANTILLES", false, 158, "an", "an"),
    NEW_CALEDONIA("NEW CALEDONIA", false, 160, "nc", "nc"),
    NEW_ZEALAND("NEW ZEALAND", false, 161, "nz", "nz"),
    NICARAGUA("NICARAGUA", false, 162, "ni", "ni"),
    NIGER("NIGER", false, 163, "ne", "ne"),
    NIGERIA("NIGERIA", false, 164, "ng", "ng"),
    NIUE("NIUE", false, 165, "nu", "nu"),
    NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", false, 194, "mp", "mp"),
    NORWAY("NORWAY", false, 166, "no", "no"),
    OMAN("OMAN", false, 167, "om", "om"),
    PAKISTAN("PAKISTAN", false, 168, "pk", "pk"),
    PALAU("PALAU", false, 169, "pw", "pw"),
    PALESTINIAN("PALESTINIAN TERRITORY, Occupied", false, 170, "ps", "ps"),
    PANAMA("PANAMA", false, 171, "pa", "pa"),
    PAPUA_NEW_GUINEA("PAPUA NEW GUINEA", false, 172, "pg", "pg"),
    PARAGUAY("PARAGUAY", false, 173, "py", "py"),
    PERU("PERU", false, 174, "pe", "pe"),
    PHILIPPINES("PHILIPPINES", false, 175, "ph", "ph"),
    POLAND("POLAND", true, 176, "pl", "pl"),
    PORTUGAL("PORTUGAL", true, 177, "pt", "pt"),
    PUERTO_RICO("PUERTO RICO", false, 178, "pr", "pr"),
    QATAR("QATAR", false, 179, "qa", "qa"),
    REUNION("REUNION", false, 180, "re", "re"),
    ROMANIA("ROMANIA", true, 181, "ro", "ro"),
    RUSSIAN_FEDERATION("RUSSIAN FEDERATION", false, 183, "ru", "ru"),
    RWANDA("RWANDA", false, 184, "rw", "rw"),
    SAINT_KITTS_AND_NEVIS("SAINT KITTS AND NEVIS", false, 188, "kn", "kn"),
    SAINT_LUCIA("SAINT LUCIA", false, 189, "lc", "lc"),
    SAINT_PIERRE_AND_MIQUELON("SAINT PIERRE AND MIQUELON", false, 192, "pm", "pm"),
    SAINT_VINCENT_AND_THE_GRENADINES("SAINT VINCENT AND THE GRENADINES", false, 193, "vc", "vc"),
    SAN_MARINO("SAN MARINO", false, 197, "sm", "sm"),
    SAO_TOME_AND_PRINCIPE("SAO TOME AND PRINCIPE", false, 198, "st", "st"),
    SAUDI_ARABIA("SAUDI ARABIA", false, 199, "sa", "sa"),
    SENEGAL("SENEGAL", false, 201, "sn", "sn"),
    SERBIA_AND_MONTENEGRO("SERBIA AND MONTENEGRO", false, 202), //FIXME Serbia, Montenegro are 2 dif countries
    SEYCHELLES("SEYCHELLES", false, 203, "sc", "sc"),
    SIERRA_LEONE("SIERRA LEONE", false, 204, "sl", "sl"),
    SINGAPORE("SINGAPORE", false, 205, "sg", "sg"),
    SLOVAKIA("SLOVAKIA", true, 206, "sk", "sk"),
    SLOVENIA("SLOVENIA", true, 207, "si", "si"),
    SOLOMON_ISLANDS("SOLOMON ISLANDS", false, 208, "sb", "sb"),
    SOMALIA("SOMALIA", false, 209, "so", "so"),
    SOUTH_AFRICA("SOUTH AFRICA", false, 210, "za", "za"),
    SPAIN("SPAIN", true, 212, "es", "es"),
    SRI_LANKA("SRI LANKA", false, 213, "lk", "lk"),
    SUDAN("SUDAN", false, 214, "sd", "sd"),
    SURINAME("SURINAME", false, 215, "sr", "sr"),
    SWAZILAND("SWAZILAND", false, 216, "cz", "cz"),
    SWEDEN("SWEDEN", true, 217, "se", "se"),
    SWITZERLAND("SWITZERLAND", false, 218, "ch", "ch"),
    SYRIAN_ARAB_REPUBLIC("SYRIAN ARAB REPUBLIC", false, 219, "sy", "sy"),
    TAIWAN("TAIWAN", false, 221, "tw", "tw"),
    TAJIKISTAN("TAJIKISTAN", false, 222, "tj", "tj"),
    TANZANIA("TANZANIA, UNITED REPUBLIC OF", false, 223, "tz", "tz"),
    THAILAND("THAILAND", false, 224, "th", "th"),
    TOGO("TOGO", false, 227, "tg", "tg"),
    TONGA("TONGA", false, 228, "to", "to"),
    TRINIDAD_AND_TOBAGO("TRINIDAD AND TOBAGO", false, 229, "tt", "tt"),
    TUNISIA("TUNISIA", false, 230, "tn", "tn"),
    TURKEY("TURKEY", false, 231, "tr", "tr"),
    TURKMENISTAN("TURKMENISTAN", false, 232, "tm", "tm"),
    TURKS_AND_CAICOS_ISLANDS("TURKS AND CAICOS ISLANDS", false, 233, "tc", "tc"),
    TUVALU("TUVALU", false, 234, "tv", "tv"),
    UGANDA("UGANDA", false, 235, "ug", "ug"),
    UKRAINE("UKRAINE", false, 236, "ua", "ua"),
    UNITED_ARAB_EMIRATES("UNITED ARAB EMIRATES", false, 237, "ae", "ae"),
    UNITED_KINGDOM("UNITED KINGDOM", true, 238, "uk", "uk"),
    UNITED_STATES("UNITED STATES", false, 241, "us", "us"),
    URUGUAY("URUGUAY", false, 240, "uy", "uy"),
    UZBEKISTAN("UZBEKISTAN", false, 242, "uz", "uz"),
    VANUATU("VANUATU", false, 243, "vu", "vu"),
    VATICAN("VATICAN CITY STATE (HOLY SEE)", false, 244, "va", "va"),
    VENEZUELA("VENEZUELA", false, 245, "ve", "ve"),
    VIETNAM("VIETNAM", false, 246, "vn", "vn"),
    WALLIS_AND_FUTUNA_ISLANDS("WALLIS AND FUTUNA ISLANDS", false, 248, "wf", "wf"),
    YEMEN("YEMEN", false, 252, "ye", "ye"),
    ZAMBIA("ZAMBIA", false, 253, "zm", "zm"),
    ZIMBABWE("ZIMBABWE", false, 254, "zw", "zw")
	;

	private String label;
	private boolean europeanUnion;
	private int id;
	private String domain;
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

	private Countries(String label, boolean europeanUnion, int id, String lang, String domain){
		this.label = label;
		this.europeanUnion = europeanUnion;
		this.id = id;
		this.domain = domain;
		this.lang = lang;
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
