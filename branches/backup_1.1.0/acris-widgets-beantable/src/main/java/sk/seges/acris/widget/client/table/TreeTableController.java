package sk.seges.acris.widget.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.TreeTable;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.CssResource.ClassName;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class TreeTableController extends TreeTablePagingOptions {

	public static interface Css extends TreeTablePagingOptions.Css {
		/**
		 * Widget style name.
		 * 
		 * @return the widget's style name
		 */
		@ClassName("gwt-TreeTableController")
		String defaultStyleName();
	}

	public interface TreeTableControllerResources extends PagingOptionsResources {
		TreeTableControllerStyle getStyle();

		TreeTableControllerMessages getMessages();
	}

	public interface TreeTableControllerMessages extends PagingOptionsMessages {
		@DefaultMessage("Open all tree nodes")
		String openTree();

		@DefaultMessage("Close all tree nodes")
		String closeTree();

		@DefaultMessage("Switch to table view")
		String tableView();

		@DefaultMessage("Switch to tree view")
		String treeView();
	}

	/**
	 * Resources used.
	 */
	public interface TreeTableControllerStyle extends PagingOptionsStyle {
		@Source("com/google/gwt/gen2/widgetbase/public/TreeTableController.css")
		Css css();

		@Source("closeTree.png")
		ImageResource closeTree();

		@Source("openTree.png")
		ImageResource openTree();

		@Source("treeView.png")
		ImageResource treeView();

		@Source("tableView.png")
		ImageResource tableView();

		@Source("headerBackground.png")
		@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
		ImageResource pagingBackground();
	}

	protected static class DefaultTreeTableControllerResources implements TreeTableControllerResources {
		private TreeTableControllerStyle style;
		private TreeTableControllerMessages messages;

		public TreeTableControllerStyle getStyle() {
			if (style == null) {
				style = ((TreeTableControllerStyle) GWT.create(TreeTableControllerStyle.class));
			}
			return style;
		}

		public TreeTableControllerMessages getMessages() {
			if (messages == null) {
				messages = ((TreeTableControllerMessages) GWT.create(TreeTableControllerMessages.class));
			}
			return messages;
		}
	}

	private TreeTable<?> treeTable;
	private Image toggleTreeImage, flattenTreeImage;
	private PushButton toggleTreeButton, flattenTreeButton;
	private TreeTableControllerResources resources;

	public TreeTableController(TreeTable<?> treeTable, boolean pageCountAvailable) {
		this(treeTable, new DefaultTreeTableControllerResources(), pageCountAvailable);
	}

	public TreeTableController(TreeTable<?> treeTable, TreeTableControllerResources resources,
			boolean pageCountAvailable) {
		super(treeTable, resources, pageCountAvailable);
		this.treeTable = treeTable;
		this.resources = resources;
		flattenTreeImage = createImage(resources.getStyle().tableView());
		flattenTreeButton = new PushButton(flattenTreeImage);
		getButtonPanel().insert(flattenTreeButton, 0);
		toggleTreeImage = createImage(resources.getStyle().closeTree());
		toggleTreeButton = new PushButton(toggleTreeImage);
		getButtonPanel().insert(toggleTreeButton, 0);
		ClickListener listener = new ClickListener() {
			public void onClick(Widget sender) {
				if (sender == toggleTreeButton) {
					if (!TreeTableController.this.treeTable.isTreeOpen()) {
						TreeTableController.this.treeTable.openTree();
					} else {
						TreeTableController.this.treeTable.closeTree();
					}
					
				} else if (sender == flattenTreeButton) {
					TreeTableController.this.treeTable.setFlattened(!TreeTableController.this.treeTable.isFlattened());
					updateButtonState();
				}
			}
		};
		toggleTreeButton.addClickListener(listener);
		flattenTreeButton.addClickListener(listener);
		updateButtonState();
	}

	@Override
	protected void updateButtonState() {
		super.updateButtonState();
		if (treeTable.isFlattened()) {
			applyImage(flattenTreeImage, resources.getStyle().treeView());
			flattenTreeButton.setTitle(resources.getMessages().treeView());
		} else {
			applyImage(flattenTreeImage, resources.getStyle().tableView());
			flattenTreeButton.setTitle(resources.getMessages().tableView());
		}
		toggleTreeButton.setEnabled(!treeTable.isFlattened());
		if (treeTable.isTreeOpen()) {
			applyImage(toggleTreeImage, resources.getStyle().closeTree());
			toggleTreeButton.setTitle(resources.getMessages().closeTree());
		} else {
			applyImage(toggleTreeImage, resources.getStyle().openTree());
			toggleTreeButton.setTitle(resources.getMessages().openTree());
		}
	}
}
