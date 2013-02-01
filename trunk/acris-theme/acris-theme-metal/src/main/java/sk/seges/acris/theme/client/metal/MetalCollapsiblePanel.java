package sk.seges.acris.theme.client.metal;


//@ThemeSupport(widgetClass = CollapsiblePanel.class, elementName = "collapsiblePanel", themeName = MetalTheme.NAME)
public interface MetalCollapsiblePanel {}
/*
	private MetalThemeSimplePanel themeSimplePanel;

	public MetalThemeCollapsiblePanel() {
		super();
		setTimeToSlide(15000);
		
		themeSimplePanel = (MetalThemeSimplePanel) GWT.create(SimplePanel.class);
		ToggleButton toggler = GWT.create(ToggleButton.class);

		toggler.getDownFace().setText("<<");
		toggler.getUpFace().setText(">>");
		
		toggler.addStyleName("acris-collapsible-panel-toggle-button");
		
		FlowPanel titlePanelWrapper = new FlowPanel();
		
		FlowPanel titlePanel = new FlowPanel();
		titlePanel.setStyleName("acris-theme-helix-panel-title-text");
		
		HTML titleText = new HTML("title");
		titleText.addStyleName("acris-theme-collapsible-panel-title-text");

		titlePanel.add(titleText);
		titlePanel.add(toggler);
		
		titlePanelWrapper.add(titlePanel);
		titlePanelWrapper.add(new Cleaner());
		
		themeSimplePanel.setTitleWidget(titlePanelWrapper);
		hookupControlToggle(toggler);
		super.add(themeSimplePanel);
		
		//todo
		//setHoverBarContents(Widget bar)
	}


	@Override
	public void add(Widget w) {
		
		themeSimplePanel.add(w);
	}
	
	@Override
	public boolean remove(Widget w) {
		return themeSimplePanel.remove(w);
	}
}*/