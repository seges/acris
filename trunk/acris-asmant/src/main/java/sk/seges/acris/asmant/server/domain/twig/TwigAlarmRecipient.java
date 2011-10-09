package sk.seges.acris.asmant.server.domain.twig;

import com.vercer.engine.persist.annotation.Key;

/**
 * @author ladislav.gazo
 */
public class TwigAlarmRecipient {
	@Key
	private String email;
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TwigAlarmRecipients [email=" + email + ", name=" + name + "]";
	}
}
