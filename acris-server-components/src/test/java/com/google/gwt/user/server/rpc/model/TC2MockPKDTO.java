package com.google.gwt.user.server.rpc.model;

/**
* Created by PeterSimun on 1.12.2014.
*/
public class TC2MockPKDTO {

    private Long part2Id;
    private Long id;

    public TC2MockPKDTO() {}

    public TC2MockPKDTO(Long id, Long part2Id) {
        this.id = id;
        this.part2Id = part2Id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPart2Id() {
        return part2Id;
    }

    public void setPart2Id(Long part2Id) {
        this.part2Id = part2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TC2MockPKDTO that = (TC2MockPKDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (part2Id != null ? !part2Id.equals(that.part2Id) : that.part2Id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = part2Id != null ? part2Id.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}