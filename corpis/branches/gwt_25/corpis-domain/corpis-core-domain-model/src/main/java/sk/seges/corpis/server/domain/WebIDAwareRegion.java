package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
public interface WebIDAwareRegion extends RegionCore {

	String webId();

}