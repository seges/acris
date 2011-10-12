package sk.seges.acris.binding.client.samples.form;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;

abstract class AbstractFormBase extends Composite {

    private final DockPanel dock = new DockPanel();
    
    private final VerticalPanel vp;

    protected Grid grid = null;
    
    protected AbstractFormBase() {
    	vp = getContainer();
    	initWidget(dock);
    }
        
    protected VerticalPanel getContainer() {
    	return new VerticalPanel();
    }
    
    @Override
    protected void onLoad() {
    	super.onLoad();
    	dock.clear();
    	initPanel();
    }

	protected void initPanel() {
        grid = new Grid();
        grid.setWidth("100%");

        dock.setHeight("100%");
        dock.setWidth("100%");

        dock.add(vp, DockPanel.CENTER);

        vp.setWidth("100%");
        vp.add(grid);
        
        grid.setCellSpacing(10);
	}
}