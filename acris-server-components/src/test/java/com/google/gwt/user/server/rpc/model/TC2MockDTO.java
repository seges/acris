package com.google.gwt.user.server.rpc.model;

/**
* Created by PeterSimun on 1.12.2014.
*/
public class TC2MockDTO {

    private TC2MockPKDTO id;
    private String field;

    public TC2MockDTO() {}

    public TC2MockDTO(TC2MockPKDTO id, String field) {
        this.id = id;
        this.field = field;
    }

    public TC2MockPKDTO getId() {
        return id;
    }

    public void setId(TC2MockPKDTO id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
