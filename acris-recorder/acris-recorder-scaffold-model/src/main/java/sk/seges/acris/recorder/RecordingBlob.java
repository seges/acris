package sk.seges.acris.recorder;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

import java.sql.Blob;

/**
 * Created by PeterSimun on 13.4.2014.
 */
@DomainInterface
@BaseObject
public interface RecordingBlob extends IMutableDomainObject<RecordingBlobPk> {

    Blob content();

}