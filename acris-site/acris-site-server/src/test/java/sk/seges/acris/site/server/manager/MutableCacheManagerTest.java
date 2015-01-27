package sk.seges.acris.site.server.manager;

import junit.framework.Assert;
import org.junit.Test;
import sk.seges.acris.site.server.cache.MutableCacheManager;

/**
 * Created by PeterSimun on 1.12.2014.
 */
public class MutableCacheManagerTest {

    @Test
    public void stripResponseTest() {
        String encodedResult = "//OK[___REMOVE_ME__,sk.seges.acris.site.server.cache.model.MockDomainEntity/20___REMOVE_ME__,mock_content]";

        Assert.assertEquals("Response for client is not properly parsed",
                "//OK[mock_content]",
                MutableCacheManager.getResponseForClient(encodedResult));

        Assert.assertEquals("Response for cache is not properly parsed",
                "sk.seges.acris.site.server.cache.model.MockDomainEntity/20",
                MutableCacheManager.getResponseForCache(encodedResult));

    }


}
