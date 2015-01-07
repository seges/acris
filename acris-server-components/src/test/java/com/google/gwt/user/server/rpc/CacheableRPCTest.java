package com.google.gwt.user.server.rpc;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.impl.StandardSerializationPolicy;
import com.google.gwt.user.server.rpc.mock.PositiveSerializationMap;
import com.google.gwt.user.server.rpc.model.TC1MockDTO;
import com.google.gwt.user.server.rpc.model.TC2MockDTO;
import com.google.gwt.user.server.rpc.model.TC2MockPKDTO;
import com.google.gwt.user.server.rpc.service.TC1MockService;
import com.google.gwt.user.server.rpc.service.TC2MockService;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PeterSimun on 1.12.2014.
 */
public class CacheableRPCTest {

    protected SerializationPolicy getSerializationPolicy() {
        return new StandardSerializationPolicy(new PositiveSerializationMap(), new PositiveSerializationMap(), new HashMap<Class<?>, String>());
    }

    @Test
    public void testBasicSerialization() throws SerializationException {

        List<TC1MockDTO> result = new ArrayList<TC1MockDTO>();
        result.add(new TC1MockDTO(55L, "Test field1"));
        result.add(new TC1MockDTO(33L, "Test field2"));

        String encodedResult = CacheableRPC.encodeResponseForSuccess(TC1MockService.class.getMethods()[0], result, getSerializationPolicy(), 0);
        Assert.assertEquals("Encoded result is not correct!",
                "//OK[___REMOVE_ME__," + TC1MockDTO.class.getCanonicalName() + "/55," + TC1MockDTO.class.getCanonicalName() + "/33,___REMOVE_ME__,'h',4,5,2,'3',4,3,2,2,1,[\"java.util.ArrayList/4159755760\",\"" + TC1MockDTO.class.getCanonicalName() + "/4135131799\",\"Test field1\",\"java.lang.Long/4227064769\",\"Test field2\"],0,7]",
                encodedResult);
    }

    @Test
    public void testPrimaryKeySerialization() throws SerializationException {
        List<TC2MockDTO> result = new ArrayList<TC2MockDTO>();
        result.add(new TC2MockDTO(new TC2MockPKDTO(55L, 22L), "Test field1"));
        result.add(new TC2MockDTO(new TC2MockPKDTO(11L, 33L), "Test field2"));

        String encodedResult = CacheableRPC.encodeResponseForSuccess(TC2MockService.class.getMethods()[0], result, getSerializationPolicy(), 0);
        Assert.assertEquals("Encoded result is not correct!",
                "//OK[___REMOVE_ME__," + TC2MockDTO.class.getCanonicalName() + "/737," + TC2MockPKDTO.class.getCanonicalName() + "/55," +
                                         TC2MockDTO.class.getCanonicalName() + "/1034," + TC2MockPKDTO.class.getCanonicalName() + "/11,___REMOVE_ME__,'h',5,'L',5,4,6,2,'W',5,'3',5,4,3,2,2,1,[\"java.util.ArrayList/4159755760\",\"" + TC2MockDTO.class.getCanonicalName() + "/2106924948\",\"Test field1\",\"" + TC2MockPKDTO.class.getCanonicalName() + "/2690960042\",\"java.lang.Long/4227064769\",\"Test field2\"],0,7]",
                encodedResult);
    }
}
