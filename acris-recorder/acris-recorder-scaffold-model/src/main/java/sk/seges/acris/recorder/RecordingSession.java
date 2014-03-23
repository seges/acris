package sk.seges.acris.recorder;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.corpis.shared.domain.HasLanguage;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

import java.util.Date;
import java.util.List;

@DomainInterface
@BaseObject
public interface RecordingSession extends IMutableDomainObject<Long>, HasWebId, HasLanguage {

	Date sessionTime();
	String language();

	String sessionInfo();
	List<RecordingLog> auditLogs();
	UserData user();
}
