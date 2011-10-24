package sk.seges.acris.mvp.client.form.smartgwt.core;

import java.util.Set;

import sk.seges.acris.mvp.shared.model.api.GroupData;
import sk.seges.acris.mvp.shared.model.api.UserGroupData;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class GroupAssignmentPanel extends AbstractSmartBindingHolder<UserGroupData> {

	public class GroupsListGrid extends ListGrid {

		public GroupsListGrid() {
			setWidth(250);
			setCellHeight(24);
			setImageSize(16);
			setShowEdges(true);
			setBorder("0px");
			setBodyStyleName("normal");
			setShowHeader(false);
			setLeaveScrollbarGap(false);

			ListGridField imageField = new ListGridField("image", 24);
			imageField.setType(ListGridFieldType.IMAGE);

			ListGridField nameField = new ListGridField("name");

			setFields(imageField, nameField);
		}
	}

	private HStack hStack;

	private GroupsListGrid availableGroups;
	private GroupsListGrid selectedGroups;

	public GroupAssignmentPanel() {

		hStack = new HStack(10);
		hStack.setHeight(160);

		availableGroups = new GroupsListGrid();
		availableGroups.setCanDragRecordsOut(true);
		availableGroups.setCanAcceptDroppedRecords(true);
		availableGroups.setCanReorderFields(true);
		availableGroups.setDragDataAction(DragDataAction.MOVE);
		availableGroups.setEmptyMessage("<br><br>No more groups are available");

		hStack.addMember(availableGroups);

		selectedGroups = new GroupsListGrid();
		selectedGroups.setCanDragRecordsOut(true);
		selectedGroups.setCanAcceptDroppedRecords(true);
		selectedGroups.setCanReorderRecords(true);
		selectedGroups.setEmptyMessage("<br><br>No groups are selected");

		VStack vStack = new VStack(10);
		vStack.setWidth(32);
		vStack.setHeight(74);
		vStack.setLayoutAlign(Alignment.CENTER);

		TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);
		rightImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectedGroups.transferSelectedData(availableGroups);
			}
		});
		vStack.addMember(rightImg);

		TransferImgButton leftImg = new TransferImgButton(TransferImgButton.LEFT);
		leftImg.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				availableGroups.transferSelectedData(selectedGroups);
			}
		});
		vStack.addMember(leftImg);

		hStack.addMember(vStack);
		hStack.addMember(selectedGroups);

		setMembers(hStack);
	}

	private Set<GroupData> available;
	
	public void setAvailable(Set<GroupData> available) {
		this.available = available;
	}
	/*
	private <T> ListGridRecord[] getRecords(Set<T> source, boolean except) {

		ListGridRecord[] records;

		List<T> result;

		if (except) {
			records = new ListGridRecord[available.size() - (source != null ? source.size() : 0)];

			if (source != null) {
				int i = 0;
				
				for (GroupData groupData : available) {
					
				}
				
				for (RoleRight roleRight : RoleRight.values()) {
					boolean found = false;
					for (RoleRight input : source) {
						if (roleRight.name().equals(input.name())) {
							found = true;
							break;
						}
					}
					if (!found) {
						roles[i] = roleRight;
						i++;
					}
				}
			} else {
				roles = RoleRight.values();
			}
		} else {
			if (source == null) {
				return new ListGridRecord[0];
			}
			records = new ListGridRecord[source.size()];
			roles = new RoleRight[source.size()];

			int i = 0;
			for (RoleRight rolesRight : source) {
				roles[i] = rolesRight;
				i++;
			}
		}

		int i = 0;

		for (RoleRight roleRight : roles) {
			records[i] = new ListGridRecord();

			records[i].setAttribute("image", "person.png");
			records[i].setAttribute("name", roleRight.name());

			i++;
		}

		return records;
	}

	private Set<GroupData> getSelected() {
		ListGridRecord[] selectedRecords = selectedGroups.getRecords();

		if (selectedRecords == null) {
			return new HashSet<GroupData>();
		}

		Set<GroupData> result = new HashSet<GroupData>();

		for (ListGridRecord selectedRole : selectedRecords) {
			String name = selectedRole.getAttribute("name");
			for (RoleRight roleRight : RoleRight.values()) {
				if (roleRight.name().equals(name)) {
					result.add(roleRight);
					break;
				}
			}
		}

		return result;
	}
*/
	private UserGroupData bean;

	@Override
	public void setBean(UserGroupData bean) {
		this.bean = bean;
//		availableGroups.setRecords(getRecords(this.bean.getGroups(), true));
//		selectedGroups.setRecords(getRecords(this.bean.getGroups(), false));
	}

	@Override
	public UserGroupData getBean() {
//		bean.setGroups(getSelected());
		return bean;
	}

}
