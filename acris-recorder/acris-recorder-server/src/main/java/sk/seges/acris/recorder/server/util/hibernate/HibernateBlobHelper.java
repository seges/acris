package sk.seges.acris.recorder.server.util.hibernate;

import org.hibernate.Session;
import sk.seges.acris.recorder.server.util.BlobHelper;

import javax.persistence.EntityManager;
import java.sql.Blob;

/**
 * Created by PeterSimun on 13.4.2014.
 */
public class HibernateBlobHelper implements BlobHelper {

    private final EntityManager entityManager;

    public HibernateBlobHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Blob createBlob(byte[] bytes) {
        return ((Session) entityManager.getDelegate()).getLobHelper().createBlob(bytes);
    }
}