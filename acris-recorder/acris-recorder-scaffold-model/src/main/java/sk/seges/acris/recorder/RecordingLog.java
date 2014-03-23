package sk.seges.acris.recorder;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

import java.util.Date;

@DomainInterface
@BaseObject
public interface RecordingLog extends IMutableDomainObject<Long> {

	RecordingSession session();
	Date eventTime();
	String event();
}
