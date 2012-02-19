package sk.seges.acris.domain.shared.domain.ftp;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterfaceSpec;
import sk.seges.corpis.appscaffold.shared.annotation.PersistenceType;
import sk.seges.corpis.appscaffold.shared.annotation.PersistentObject;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
@DomainInterfaceSpec(generateDao = PersistenceType.HIBERNATE)
@PersistentObject(PersistenceType.JPA)
public interface FTPWebSettings extends IMutableDomainObject<Long> {

	String ftpHost();
	String ftpUserName();
	String ftpUserPwd();
	String ftpRootDir();
}
