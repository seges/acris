package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface TransportPrice extends IMutableDomainObject<Long> {

	String country();

	Double dhlPrice();

	Double upsPrice();

	Double fedExPrice();

	Double dpdPrice();

	Double tntPrice();

	Double emsPrice();

}