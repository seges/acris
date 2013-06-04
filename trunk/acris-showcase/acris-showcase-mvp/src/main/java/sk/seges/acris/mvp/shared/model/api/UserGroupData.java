package sk.seges.acris.mvp.shared.model.api;

import java.io.Serializable;
import java.util.Set;

public interface UserGroupData extends Serializable {

	Set<GroupData> getGroups();

	void setGroups(Set<GroupData> groups);
}