package sk.seges.corpis.appscaffold.configuration;

import sk.seges.corpis.appscaffold.model.MockDto;
import sk.seges.corpis.appscaffold.model.MockJpaDomain;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping (domainClass = MockJpaDomain.class, dtoClass = MockDto.class)
public class MockConfiguration {

}
