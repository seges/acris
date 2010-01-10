package sk.seges.acris.etc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Countries {
    AFGHANISTAN("AFGHANISTAN", false, 5, new Locale("af"), "af"),
    ALBANIA("ALBANIA", false, 6, new Locale("al"), "al"),
    ALGERIA("ALGERIA", false, 7, new Locale("dz"), "dz"),
    AMERICAN_SAMOA("AMERICAN SAMOA", false, 8, new Locale("as"), "as"),
    ANDORRA("ANDORRA", false, 9, new Locale("ad"), "ad"),
    ANGOLA("ANGOLA", false, 10, new Locale("ao"), "ao"),
    ANGUILLA("ANGUILLA", false, 11, new Locale("ai"), "ai"),
    ANTIGUA_AND_BARBUDA("ANTIGUA AND BARBUDA", false, 12, new Locale("ag"), "ag"),
    ARGENTINA("ARGENTINA", false, 13, new Locale("ar"), "ar"),
    ARMENIA("ARMENIA", false, 14, new Locale("am"), "am"),
    ARUBA("ARUBA", false, 15, new Locale("aw"), "aw"),
    AUSTRALIA("AUSTRALIA", false, 16, new Locale("au"), "au"),
    AUSTRIA("AUSTRIA", true, 17, new Locale("at"), "at"),
    AZERBAIJAN("AZERBAIJAN", false, 18, new Locale("az"), "az"),
    BAHAMAS("BAHAMAS", false, 20, new Locale("bs"), "bs"),
    BAHRAIN("BAHRAIN", false, 21, new Locale("bh"), "bh"),
    BANGLADESH("BANGLADESH", false, 22, new Locale("db"), "bd"),
    BARBADOS("BARBADOS", false, 23, new Locale("bb"), "bb"),
    BELARUS("BELARUS", false, 26, new Locale("by"), "by"),
    BELGIUM("BELGIUM", true, 24, new Locale("be"), "be"),
    BELIZE("BELIZE", false, 25, new Locale("bz"), "bz"),
    BENIN("BENIN", false, 27, new Locale("bj"), "bj"),
    BERMUDA("BERMUDA", false, 28, new Locale("bm"), "bm"),
    BHUTAN("BHUTAN", false, 29, new Locale("bt"), "bt"),
    BOLIVIA("BOLIVIA", false, 30, new Locale("bo"), "bo"),
    BOSNIA_AND_HERZEGOWINA("BOSNIA AND HERZEGOWINA", false, 32, new Locale("ba"), "ba"),
    BOTSWANA("BOTSWANA", false, 33, new Locale("bw"), "bw"),
    BRAZIL("BRAZIL", false, 34, new Locale("br"), "br"),
    BRUNEI_DARUSSALAM("BRUNEI DARUSSALAM", false, 36, new Locale("bn"), "bn"),
    BULGARIA("BULGARIA", true, 37, new Locale("bg"), "bg"),
    BURKINA_FASO("BURKINA FASO", false, 38, new Locale("bf"), "bf"),
    BURUNDI("BURUNDI", false, 39, new Locale("bi"), "bi"),
    CAMBODIA("CAMBODIA", false, 40, new Locale("kh"), "kh"),
    CAMEROON("CAMEROON", false, 41, new Locale("cm"), "cm"),
    CANADA("CANADA", false, 42, new Locale("ca"), "ca"),
    CAPE_VERDE("CAPE VERDE", false, 44, new Locale("cv"), "cv"),
    CAYMAN_ISLANDS("CAYMAN ISLANDS", false, 45, new Locale("ky"), "ky"),
    CENTRAL_AFRICAN_REPUBLIC("CENTRAL AFRICAN REPUBLIC", false, 46, new Locale("cf"), "cf"),
    CHAD("CHAD", false, 96, new Locale("td"), "td"),
    CHILE("CHILE", false, 97, new Locale("cl"), "cl"),
    CHINA("CHINA", false, 98, new Locale("cn"), "cn"),
    COLOMBIA("COLOMBIA", false, 47, new Locale("co"), "co"),
    COMOROS("COMOROS", false, 48, new Locale("km"), "km"),
    CONGO("CONGO, Democratic Republic of (was Zaire)", false, 49, new Locale("cg"), "cg"),
    COOK_ISLANDS("COOK ISLANDS", false, 50, new Locale("ck"), "ck"),
    COSTA_RICA("COSTA RICA", false, 51, new Locale("cr"), "cr"),
    CROATIA("CROATIA", false, 52, new Locale("hr"), "hr"),
    CUBA("CUBA", false, 53, new Locale("cu"), "cu"),
    CYPRUS("CYPRUS", true, 56, new Locale("cy"), "cy"),
    CZECH_REPUBLIC("CZECH REPUBLIC", true, 57, new Locale("cz"), "cz"),
    DENMARK("DENMARK", true, 58, new Locale("dk"), "dk"),
    DJIBOUTI("DJIBOUTI", false, 59, new Locale("dj"), "dj"),
    DOMINICA("DOMINICA", false, 60, new Locale("dm"), "dm"),
    DOMINICAN_REPUBLIC("DOMINICAN REPUBLIC", false, 61, new Locale("do"), "do"),
    ECUADOR("ECUADOR", false, 62, new Locale("ec"), "ec"),
    EGYPT("EGYPT", false, 63, new Locale("eg"), "eg"),
    EL_SALVADOR("EL SALVADOR", false, 64, new Locale("sv"), "sv"),
    ERITREA("ERITREA", false, 65, new Locale("er"), "er"),
    ESTONIA("ESTONIA", true, 66, new Locale("ee"), "ee"),
    ETHIOPIA("ETHIOPIA", false, 67, new Locale("et"), "et"),
    FALKLAND_ISLANDS("FALKLAND ISLANDS (MALVINAS)", false, 68, new Locale("fk"), "fk"),
    FAROE_ISLANDS("FAROE ISLANDS", false, 69, new Locale("fo"), "fo"),
    FIJI("FIJI", false, 70, new Locale("fj"), "fj"),
    FINLAND("FINLAND", true, 71, new Locale("fi"), "fi"),
    FRANCE("FRANCE", true, 72, new Locale("fr"), "fr"),
    FRENCH_GUIANA("FRENCH GUIANA", false, 73, new Locale("gf"), "gf"),
    FRENCH_POLYNESIA("FRENCH POLYNESIA", false, 74, new Locale("pf"), "pf"),
    GABON("GABON", false, 75, new Locale("ga"), "ga"),
    GAMBIA("GAMBIA", false, 225, new Locale("gm"), "gm"),
    GEORGIA("GEORGIA", false, 77, new Locale("ge"), "ge"),
    GERMANY("GERMANY", true, 78, new Locale("de"), "de"),
    GHANA("GHANA", false, 79, new Locale("gh"), "gh"),
    GIBRALTAR("GIBRALTAR", false, 80, new Locale("gi"), "gi"),
    GREECE("GREECE", true, 81, new Locale("gr"), "gr"),
    GREENLAND("GREENLAND", false, 82, new Locale("gl"), "gl"),
    GRENADA("GRENADA", false, 83, new Locale("gd"), "gd"),
    GUADELOUPE("GUADELOUPE", false, 84, new Locale("gp"), "gp"),
    GUAM("GUAM", false, 85, new Locale("gu"), "gu"),
    GUATEMALA("GUATEMALA", false, 86, new Locale("gt"), "gt"),
    GUINEA("GUINEA", false, 88, new Locale("gn"), "gn"),
    GUINEA_BISSAU("GUINEA-BISSAU", false, 89, new Locale("gw"), "gw"),
    GUYANA("GUYANA", false, 91, new Locale("gy"), "gy"),
    HAITI("HAITI", false, 92, new Locale("ht"), "ht"),
    HONDURAS("HONDURAS", false, 93, new Locale("hn"), "hn"),
    HONG_KONG("HONG KONG", false, 94, new Locale("hk"), "hk"),
    HUNGARY("HUNGARY", true, 95, new Locale("hu"), "hu"),
    ICELAND("ICELAND", false, 99, new Locale("is"), "is"),
    INDIA("INDIA", false, 100, new Locale("in"), "in"),
    INDONESIA("INDONESIA", false, 101, new Locale("id"), "id"),
    IRAN("IRAN (ISLAMIC REPUBLIC OF)", false, 102, new Locale("ir"), "ir"),
    IRAQ("IRAQ", false, 103, new Locale("iq"), "iq"),
    IRELAND("IRELAND", true, 104, new Locale("ie"), "ie"),
    ISRAEL("ISRAEL", false, 105, new Locale("il"), "il"),
    ITALY("ITALY", true, 106, new Locale("it"), "it"),
    JAMAICA("JAMAICA", false, 108, new Locale("jm"), "jm"),
    JAPAN("JAPAN", false, 109, new Locale("jp"), "jp"),
    JORDAN("JORDAN", false, 111, new Locale("jo"), "jo"),
    KAZAKHSTAN("KAZAKHSTAN", false, 112, new Locale("kz"), "kz"),
    KENYA("KENYA", false, 113, new Locale("ke"), "ke"),
    KIRIBATI("KIRIBATI", false, 114, new Locale("ki"), "ki"),
    KOREA("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", false, 115, new Locale("kp"), "kp"),
    KUWAIT("KUWAIT", false, 118, new Locale("kw"), "kw"),
    KYRGYZSTAN("KYRGYZSTAN", false, 119, new Locale("kg"), "kg"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("LAO PEOPLE'S DEMOCRATIC REPUBLIC", false, 120, new Locale("la"), "la"),
    LATVIA("LATVIA", true, 121, new Locale("lv"), "lv"),
    LEBANON("LEBANON", false, 122, new Locale("lb"), "lb"),
    LESOTHO("LESOTHO", false, 123, new Locale("ls"), "ls"),
    LIBERIA("LIBERIA", false, 124, new Locale("lr"), "lr"),
    LIBYAN_ARAB_JAMAHIRIYA("LIBYAN ARAB JAMAHIRIYA", false, 125, new Locale("ly"), "ly"),
    LIECHTENSTEIN("LIECHTENSTEIN", false, 126, new Locale("li"), "li"),
    LITHUANIA("LITHUANIA", true, 127, new Locale("lt"), "lt"),
    LUXEMBOURG("LUXEMBOURG", true, 128, new Locale("lu"), "lu"),
    MACAU("MACAU", false, 129, new Locale("mo"), "mo"),
    MACEDONIA("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", false, 130, new Locale("mk"), "mk"),
    MADAGASCAR("MADAGASCAR", false, 131, new Locale("mg"), "mg"),
    MALAWI("MALAWI", false, 133, new Locale("mw"), "mw"),
    MALAYSIA("MALAYSIA", false, 134, new Locale("my"), "my"),
    MALDIVES("MALDIVES", false, 135, new Locale("mv"), "mv"),
    MALI("MALI", false, 136, new Locale("ml"), "ml"),
    MALTA("MALTA", true, 137, new Locale("mt"), "mt"),
    MARSHALL_ISLANDS("MARSHALL ISLANDS", false, 138, new Locale("mh"), "mh"),
    MARTINIQUE("MARTINIQUE", false, 139, new Locale("mq"), "mq"),
    MAURITANIA("MAURITANIA", false, 140, new Locale("mr"), "mr"),
    MAURITIUS("MAURITIUS", false, 141, new Locale("mu"), "mu"),
    MAYOTTE("MAYOTTE", false, 142, new Locale("yt"), "yt"),
    MEXICO("MEXICO", false, 144, new Locale("mx"), "mx"),
    MICRONESIA("MICRONESIA, FEDERATED STATES OF", false, 145, new Locale("fm"), "fm"),
    MOLDOVA("MOLDOVA, REPUBLIC OF", false, 146, new Locale("md"), "md"),
    MONACO("MONACO", false, 147, new Locale("mc"), "mc"),
    MONGOLIA("MONGOLIA", false, 148, new Locale("mn"), "mn"),
    MONTSERRAT("MONTSERRAT", false, 150, new Locale("ms"), "ms"),
    MOROCCO("MOROCCO", false, 151, new Locale("ma"), "ma"),
    MOZAMBIQUE("MOZAMBIQUE", false, 152, new Locale("mz"), "mz"),
    MYANMAR("MYANMAR", false, 153, new Locale("mm"), "mm"),
    NAMIBIA("NAMIBIA", false, 154, new Locale("na"), "na"),
    NAURU("NAURU", false, 155, new Locale("nr"), "nr"),
    NEPAL("NEPAL", false, 156, new Locale("np"), "np"),
    NETHERLANDS("NETHERLANDS", true, 157, new Locale("nl"), "nl"),
    NETHERLANDS_ANTILLES("NETHERLANDS ANTILLES", false, 158, new Locale("an"), "an"),
    NEW_CALEDONIA("NEW CALEDONIA", false, 160, new Locale("nc"), "nc"),
    NEW_ZEALAND("NEW ZEALAND", false, 161, new Locale("nz"), "nz"),
    NICARAGUA("NICARAGUA", false, 162, new Locale("ni"), "ni"),
    NIGER("NIGER", false, 163, new Locale("ne"), "ne"),
    NIGERIA("NIGERIA", false, 164, new Locale("ng"), "ng"),
    NIUE("NIUE", false, 165, new Locale("nu"), "nu"),
    NORTHERN_MARIANA_ISLANDS("NORTHERN MARIANA ISLANDS", false, 194, new Locale("mp"), "mp"),
    NORWAY("NORWAY", false, 166, new Locale("no"), "no"),
    OMAN("OMAN", false, 167, new Locale("om"), "om"),
    PAKISTAN("PAKISTAN", false, 168, new Locale("pk"), "pk"),
    PALAU("PALAU", false, 169, new Locale("pw"), "pw"),
    PALESTINIAN("PALESTINIAN TERRITORY, Occupied", false, 170, new Locale("ps"), "ps"),
    PANAMA("PANAMA", false, 171, new Locale("pa"), "pa"),
    PAPUA_NEW_GUINEA("PAPUA NEW GUINEA", false, 172, new Locale("pg"), "pg"),
    PARAGUAY("PARAGUAY", false, 173, new Locale("py"), "py"),
    PERU("PERU", false, 174, new Locale("pe"), "pe"),
    PHILIPPINES("PHILIPPINES", false, 175, new Locale("ph"), "ph"),
    POLAND("POLAND", true, 176, new Locale("pl"), "pl"),
    PORTUGAL("PORTUGAL", true, 177, new Locale("pt"), "pt"),
    PUERTO_RICO("PUERTO RICO", false, 178, new Locale("pr"), "pr"),
    QATAR("QATAR", false, 179, new Locale("qa"), "qa"),
    REUNION("REUNION", false, 180, new Locale("re"), "re"),
    ROMANIA("ROMANIA", true, 181, new Locale("ro"), "ro"),
    RUSSIAN_FEDERATION("RUSSIAN FEDERATION", false, 183, new Locale("ru"), "ru"),
    RWANDA("RWANDA", false, 184, new Locale("rw"), "rw"),
    SAINT_KITTS_AND_NEVIS("SAINT KITTS AND NEVIS", false, 188, new Locale("kn"), "kn"),
    SAINT_LUCIA("SAINT LUCIA", false, 189, new Locale("lc"), "lc"),
    SAINT_PIERRE_AND_MIQUELON("SAINT PIERRE AND MIQUELON", false, 192, new Locale("pm"), "pm"),
    SAINT_VINCENT_AND_THE_GRENADINES("SAINT VINCENT AND THE GRENADINES", false, 193, new Locale("vc"), "vc"),
    SAN_MARINO("SAN MARINO", false, 197, new Locale("sm"), "sm"),
    SAO_TOME_AND_PRINCIPE("SAO TOME AND PRINCIPE", false, 198, new Locale("st"), "st"),
    SAUDI_ARABIA("SAUDI ARABIA", false, 199, new Locale("sa"), "sa"),
    SENEGAL("SENEGAL", false, 201, new Locale("sn"), "sn"),
    SERBIA_AND_MONTENEGRO("SERBIA AND MONTENEGRO", false, 202), //FIXME Serbia, Montenegro are 2 dif countries
    SEYCHELLES("SEYCHELLES", false, 203, new Locale("sc"), "sc"),
    SIERRA_LEONE("SIERRA LEONE", false, 204, new Locale("sl"), "sl"),
    SINGAPORE("SINGAPORE", false, 205, new Locale("sg"), "sg"),
    SLOVAKIA("SLOVAKIA", true, 206, new Locale("sk"), "sk"),
    SLOVENIA("SLOVENIA", true, 207, new Locale("si"), "si"),
    SOLOMON_ISLANDS("SOLOMON ISLANDS", false, 208, new Locale("sb"), "sb"),
    SOMALIA("SOMALIA", false, 209, new Locale("so"), "so"),
    SOUTH_AFRICA("SOUTH AFRICA", false, 210, new Locale("za"), "za"),
    SPAIN("SPAIN", true, 212, new Locale("es"), "es"),
    SRI_LANKA("SRI LANKA", false, 213, new Locale("lk"), "lk"),
    SUDAN("SUDAN", false, 214, new Locale("sd"), "sd"),
    SURINAME("SURINAME", false, 215, new Locale("sr"), "sr"),
    SWAZILAND("SWAZILAND", false, 216, new Locale("cz"), "cz"),
    SWEDEN("SWEDEN", true, 217, new Locale("se"), "se"),
    SWITZERLAND("SWITZERLAND", false, 218, new Locale("ch"), "ch"),
    SYRIAN_ARAB_REPUBLIC("SYRIAN ARAB REPUBLIC", false, 219, new Locale("sy"), "sy"),
    TAIWAN("TAIWAN", false, 221, new Locale("tw"), "tw"),
    TAJIKISTAN("TAJIKISTAN", false, 222, new Locale("tj"), "tj"),
    TANZANIA("TANZANIA, UNITED REPUBLIC OF", false, 223, new Locale("tz"), "tz"),
    THAILAND("THAILAND", false, 224, new Locale("th"), "th"),
    TOGO("TOGO", false, 227, new Locale("tg"), "tg"),
    TONGA("TONGA", false, 228, new Locale("to"), "to"),
    TRINIDAD_AND_TOBAGO("TRINIDAD AND TOBAGO", false, 229, new Locale("tt"), "tt"),
    TUNISIA("TUNISIA", false, 230, new Locale("tn"), "tn"),
    TURKEY("TURKEY", false, 231, new Locale("tr"), "tr"),
    TURKMENISTAN("TURKMENISTAN", false, 232, new Locale("tm"), "tm"),
    TURKS_AND_CAICOS_ISLANDS("TURKS AND CAICOS ISLANDS", false, 233, new Locale("tc"), "tc"),
    TUVALU("TUVALU", false, 234, new Locale("tv"), "tv"),
    UGANDA("UGANDA", false, 235, new Locale("ug"), "ug"),
    UKRAINE("UKRAINE", false, 236, new Locale("ua"), "ua"),
    UNITED_ARAB_EMIRATES("UNITED ARAB EMIRATES", false, 237, new Locale("ae"), "ae"),
    UNITED_KINGDOM("UNITED KINGDOM", true, 238, new Locale("uk"), "uk"),
    UNITED_STATES("UNITED STATES", false, 241, new Locale("us"), "us"),
    URUGUAY("URUGUAY", false, 240, new Locale("uy"), "uy"),
    UZBEKISTAN("UZBEKISTAN", false, 242, new Locale("uz"), "uz"),
    VANUATU("VANUATU", false, 243, new Locale("vu"), "vu"),
    VATICAN("VATICAN CITY STATE (HOLY SEE)", false, 244, new Locale("va"), "va"),
    VENEZUELA("VENEZUELA", false, 245, new Locale("ve"), "ve"),
    VIETNAM("VIETNAM", false, 246, new Locale("vn"), "vn"),
    WALLIS_AND_FUTUNA_ISLANDS("WALLIS AND FUTUNA ISLANDS", false, 248, new Locale("wf"), "wf"),
    YEMEN("YEMEN", false, 252, new Locale("ye"), "ye"),
    ZAMBIA("ZAMBIA", false, 253, new Locale("zm"), "zm"),
    ZIMBABWE("ZIMBABWE", false, 254, new Locale("zw"), "zw")
	;

	private String name;
	private boolean europeanUnion;
	private int id;
	private String domain;
	private Locale locale;
	
	private static Map<String, Countries> countryMap = new HashMap<String, Countries>();

	static {
	    for(Countries country : Countries.values()) {
	        countryMap.put(country.name(), country);
	    }
	}

	@Deprecated
	private Countries(String name, boolean europeanUnion, int id){
		this.name = name;
		this.europeanUnion = europeanUnion;
		this.id = id;
	}

	private Countries(String name, boolean europeanUnion, int id, Locale locale, String domain){
		this.name = name;
		this.europeanUnion = europeanUnion;
		this.id = id;
		this.domain = domain;
		this.locale = locale;
	}

	public static Countries ofValue(String countryName) {
	    return countryMap.get(countryName);
	}

	public String getDomain() {
		return domain;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	@Override
	public String toString() {
		return name;
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
}
