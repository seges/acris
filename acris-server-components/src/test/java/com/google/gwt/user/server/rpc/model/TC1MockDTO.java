package com.google.gwt.user.server.rpc.model;

/**
* Created by PeterSimun on 1.12.2014.
*/
public class TC1MockDTO {

    private Long id;
    private String field;

    public TC1MockDTO() {}

    public TC1MockDTO(Long id, String field) {
        this.id = id;
        this.field = field;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
