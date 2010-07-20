package sk.seges.acris.recorder.rpc.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "sessionLog")
//@SequenceGenerator(name = "seqSessionLog", sequenceName = "seq_sessionlog", initialValue = 1)
public class SessionLog implements /*IUserDetail, */IDomainObject<Long> {
	
	private static final long serialVersionUID = 1207916678862996316L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GeneratedValue(generator = "seqSessionLog")
	private Long id;

	public static final String SESSION_ID_ATTRIBUTE = "sessionId";
	public static final String USER_ATTRIBUTE = "user";

	@Column
	private String sessionId;
	
	@Column
	private String initialUrl;
	@Column
	private int version;

	@ManyToOne
	private GenericUser user;

	@org.hibernate.annotations.CollectionOfElements
	private List<String> tokensTracker;
	@Column
	private String browserName;
	@Column
	private String browserVersion;
	@Column
	private String userAgent;
	@Column
	private String osName;
	@Column
	private int screenWidth;
	@Column
	private int screenHeight;
	@Column
	private int browserWidth;
	@Column
	private int browserHeight;
	@Column
	private int colorDepth;
	@Column
	private boolean javaEnabled;
	@Column
	private boolean antialiasingFonts;
	@Column
	private String ipAddress;
	@Column
	private String hostName;
	@Column
	private String language;
	@Column
	private String referrer;
	@Column
	private Date sessionTime;
	@Column
	private String city;	//http://www.maxmind.com/app/geolitecountry
	@Column
	private String country;
	@Column
	private String state;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	public void setInitialUrl(String initialUrl) {
		this.initialUrl = initialUrl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<String> getTokensTracker() {
		return tokensTracker;
	}

	public void setTokensTracker(List<String> tokensTracker) {
		this.tokensTracker = tokensTracker;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getBrowserWidth() {
		return browserWidth;
	}

	public void setBrowserWidth(int browserWidth) {
		this.browserWidth = browserWidth;
	}

	public int getBrowserHeight() {
		return browserHeight;
	}

	public void setBrowserHeight(int browserHeight) {
		this.browserHeight = browserHeight;
	}

	public int getColorDepth() {
		return colorDepth;
	}

	public void setColorDepth(int colorDepth) {
		this.colorDepth = colorDepth;
	}

	public boolean isJavaEnabled() {
		return javaEnabled;
	}

	public void setJavaEnabled(boolean javaEnabled) {
		this.javaEnabled = javaEnabled;
	}

	public boolean isAntialiasingFonts() {
		return antialiasingFonts;
	}

	public void setAntialiasingFonts(boolean antialiasingFonts) {
		this.antialiasingFonts = antialiasingFonts;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public Date getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(Date sessionTime) {
		this.sessionTime = sessionTime;
	}

	public Long getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (antialiasingFonts ? 1231 : 1237);
		result = prime * result + browserHeight;
		result = prime * result
				+ ((browserName == null) ? 0 : browserName.hashCode());
		result = prime * result
				+ ((browserVersion == null) ? 0 : browserVersion.hashCode());
		result = prime * result + browserWidth;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + colorDepth;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result
				+ ((initialUrl == null) ? 0 : initialUrl.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + (javaEnabled ? 1231 : 1237);
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((osName == null) ? 0 : osName.hashCode());
		result = prime * result
				+ ((referrer == null) ? 0 : referrer.hashCode());
		result = prime * result + screenHeight;
		result = prime * result + screenWidth;
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result
				+ ((sessionTime == null) ? 0 : sessionTime.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());

		if (tokensTracker != null) {
			int tokensHashcode = 0;
			for (String token: tokensTracker) {
				tokensHashcode += token.hashCode();
			}
			result = prime * result + tokensHashcode;
		}
		
		result = prime * result
				+ ((userAgent == null) ? 0 : userAgent.hashCode());
		result = prime * result
			+ ((user == null) ? 0 : user.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionLog other = (SessionLog) obj;
		if (antialiasingFonts != other.antialiasingFonts)
			return false;
		if (browserHeight != other.browserHeight)
			return false;
		if (browserName == null) {
			if (other.browserName != null)
				return false;
		} else if (!browserName.equals(other.browserName))
			return false;
		if (browserVersion == null) {
			if (other.browserVersion != null)
				return false;
		} else if (!browserVersion.equals(other.browserVersion))
			return false;
		if (browserWidth != other.browserWidth)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (colorDepth != other.colorDepth)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (initialUrl == null) {
			if (other.initialUrl != null)
				return false;
		} else if (!initialUrl.equals(other.initialUrl))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (javaEnabled != other.javaEnabled)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (osName == null) {
			if (other.osName != null)
				return false;
		} else if (!osName.equals(other.osName))
			return false;
		if (referrer == null) {
			if (other.referrer != null)
				return false;
		} else if (!referrer.equals(other.referrer))
			return false;
		if (screenHeight != other.screenHeight)
			return false;
		if (screenWidth != other.screenWidth)
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (sessionTime == null) {
			if (other.sessionTime != null)
				return false;
		} else if (!sessionTime.equals(other.sessionTime))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (tokensTracker == null) {
			if (other.tokensTracker != null)
				return false;
		} else {
			Long hashCode = 0L;
			
			for (String token: tokensTracker)
				hashCode += token.hashCode();
			for (String token: other.tokensTracker)
				hashCode -= token.hashCode();

			if (hashCode != 0)
				return false;
		}
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		if (version != other.version)
			return false;
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (user.getId() == null) {
			if (other.user.getId() != null) {
				return false;
			}
		} else if (!user.getId().equals(other.user.getId())) {
			return false;
		}
		return true;
	}

	public GenericUser getUser() {
		return user;
	}

	public void setUser(GenericUser user) {
		this.user = user;
	}
}