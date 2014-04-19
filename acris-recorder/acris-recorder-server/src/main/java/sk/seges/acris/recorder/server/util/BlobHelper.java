package sk.seges.acris.recorder.server.util;

import java.sql.Blob;

/**
 * Created by PeterSimun on 13.4.2014.
 */
public interface BlobHelper {

    Blob createBlob(byte[] bytes);

}
