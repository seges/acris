package sk.seges.acris.domain.shared.domain.ftp;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface FTPWebSettings extends IMutableDomainObject<Long> {

	String ftpHost();
	String ftpUserName();
	String ftpUserPwd();
	String ftpRootDir();
}
