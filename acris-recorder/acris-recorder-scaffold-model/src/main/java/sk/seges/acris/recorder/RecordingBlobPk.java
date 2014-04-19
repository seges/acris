package sk.seges.acris.recorder;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

import java.io.Serializable;

/**
 * Created by PeterSimun on 13.4.2014.
 */
@DomainInterface
@BaseObject
public interface RecordingBlobPk extends Serializable {

    Long sessionId();
    Long blobId();
}
