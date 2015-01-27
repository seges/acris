package sk.seges.acris.site.server.cache.model;

import sk.seges.sesam.domain.IDomainObject;

/**
 * Created by PeterSimun on 1.1.2015.
 */
public class MockDomainEntity implements IDomainObject<Long> {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MockDomainEntity that = (MockDomainEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
