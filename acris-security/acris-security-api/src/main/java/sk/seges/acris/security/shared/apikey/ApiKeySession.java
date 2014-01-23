package sk.seges.acris.security.shared.apikey;

import java.io.Serializable;

public class ApiKeySession implements Serializable {
	private static final long serialVersionUID = -4442015893706554036L;
	
	private Long userId;
	private Long expirationTimestamp;
	
	public ApiKeySession() {}
	
	public ApiKeySession(Long userId, Long expirationTimestamp) {
		this.userId = userId;
		this.expirationTimestamp = expirationTimestamp;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getExpirationTimestamp() {
		return expirationTimestamp;
	}

	public void setExpirationTimestamp(Long expirationTimestamp) {
		this.expirationTimestamp = expirationTimestamp;
	}
}
