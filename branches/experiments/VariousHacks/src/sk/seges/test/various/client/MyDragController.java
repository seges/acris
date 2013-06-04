package sk.seges.test.various.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MyDragController extends PickupDragController {
	public MyDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dragStart() {
		GWT.log("start", null);
		super.dragStart();
	}
	
	@Override
	public void dragMove() {
		GWT.log("move", null);
		super.dragMove();
	}
	
	@Override
	public void dragEnd() {
		GWT.log("end", null);
		super.dragEnd();
	}
}
