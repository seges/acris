/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.gen2.table.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractRowView;
import com.google.gwt.gen2.table.shared.ColumnFilterInfo;
import com.google.gwt.gen2.table.shared.ColumnFilterList;
import com.google.gwt.gen2.table.shared.ColumnSortInfo;
import com.google.gwt.gen2.table.shared.ColumnSortList;
import com.google.gwt.gen2.table.shared.Request;
import com.google.gwt.gen2.table.shared.SerializableResponse;
import com.google.gwt.gen2.table.shared.TreeRequest;
import com.google.gwt.gen2.table.shared.TreeTableItem;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.resources.client.CssResource.NotStrict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@link PagingScrollTable} that hides child rows to emulate TreeTable
 *
 * @param <RowType> the data type of the row values
 */
public class TreeTable<RowType extends TreeTableItem> extends
    PagingScrollTable<RowType> {
  static class ClientTreeTableModel<RowType extends TreeTableItem> extends
      TreeTableModel<RowType> {
    class TreeItemComparator implements Comparator<TreeItem<RowType>> {
      private final ColumnDefinition<RowType, ?> columnDefinition;
      private final boolean ascending;

      public TreeItemComparator(ColumnDefinition<RowType, ?> columnDefinition,
          boolean ascending) {
        this.columnDefinition = columnDefinition;
        this.ascending = ascending;
      }

      public int compare(TreeItem<RowType> o1, TreeItem<RowType> o2) {
        return ((Comparable) columnDefinition.getCellValue(o1.getTreeTableItem())).compareTo(columnDefinition.getCellValue(o2.getTreeTableItem()))
            * (ascending ? 1 : -1);
      }
    }

    private List<TreeItem<RowType>> rootItems = new ArrayList<TreeItem<RowType>>(),
        flattenedItems = new ArrayList<TreeItem<RowType>>();
    private final TableDefinition<RowType> tableDefinition;
    private boolean filterTopLevel = true;

    public ClientTreeTableModel(TableDefinition<RowType> tableDefinition,
        List<TreeItem<RowType>> rootItems, boolean filterTopLevel) {
      this.tableDefinition = tableDefinition;
      this.rootItems = rootItems;
      flattenItems(rootItems);
      this.filterTopLevel = filterTopLevel;
    }

    private void flattenItems(List<TreeItem<RowType>> items) {
      for (TreeItem<RowType> item : items) {
        flattenedItems.add(item);
        if (item.hasChildren()) {
          flattenItems(item.getChildren());
        }
      }
    }

    // if (getRowIndex() == 0) {
    // TreeTableItem treeTableItem = treeTable.getRowValue(0);
    // if (treeTableItem.getParent() != null) {
    // // Create path
    // TreeTableItem parent = treeTableItem.getParent();
    // HorizontalPanel pathWidget = new HorizontalPanel();
    // pathWidget.clear();
    // Label pathLabel = new Label();
    // Css css = treeTable.getResources().getStyle().css();
    // TreeTableMessages messages = treeTable.getResources().getConstants();
    // while (parent != null) {
    // HTML nodeIndicator = new HTML();
    //nodeIndicator.setTitle(messages.closeTreeNodeTooltip(parent.getDisplayName
    // ()));
    // nodeIndicator.setStyleName(css.treeTableOpenNode());
    // pathWidget.add(nodeIndicator);
    // pathLabel.setText(parent.getDisplayName() + "/"
    // + pathLabel.getText());
    // parent = parent.getParent();
    // }
    // pathWidget.add(pathLabel);
    // treeTable.getDataTable().insertRow(0);
    // treeTable.getDataTable().setWidget(0, 0, pathWidget);
    // Element element =
    // treeTable.getDataTable().getCellFormatter().getElement(0, 0);
    // DOM.setElementPropertyInt(element, "colSpan", 4);
    // }

    @Override
    public void requestTreeItems(TreeRequest request, Callback<RowType> callback) {
      boolean open = request.isOpen();
      boolean flattened = request.isFlattened();
      Set<String> invertedNodes = request.getInvertedNodes();
      int numRows = request.getNumRows();
      int startRow = request.getStartRow();
      int currentRow = 0;
      ColumnSortList columnSortList = request.getColumnSortList();
      ColumnSortInfo columnSortInfo = columnSortList.getPrimaryColumnSortInfo();
      TreeItemComparator treeItemComparator = null;
      if (columnSortInfo != null) {
        List<ColumnDefinition<RowType, ?>> visibleColumnDefinitions = tableDefinition.getVisibleColumnDefinitions();
        ColumnDefinition<RowType, ?> sortableColumnDefinition = null;
        for (ColumnDefinition<RowType, ?> columnDefinition : visibleColumnDefinitions) {
          if (columnDefinition.getColumnFilter() != null
              && columnSortInfo != null) {
            if (columnDefinition.getColumnFilter().getColumn() == columnSortInfo.getColumn()) {
              sortableColumnDefinition = columnDefinition;
            }
          }
        }
        if (sortableColumnDefinition != null) {
          treeItemComparator = new TreeItemComparator(sortableColumnDefinition,
              columnSortInfo.isAscending());
          if (flattened) {
            Collections.sort(flattenedItems, treeItemComparator);
          } else {
            Collections.sort(rootItems, treeItemComparator);
          }
        }
      }
      List<RowType> visibleItems = new ArrayList<RowType>();
      List<TreeItem<RowType>> items;
      if (flattened) {
        items = flattenedItems;
      } else {
        items = rootItems;
      }
      for (TreeItem<RowType> rootItem : items) {
        currentRow = addTreeItem(request, visibleItems, rootItem, open,
            invertedNodes, flattened, treeItemComparator, currentRow, startRow,
            numRows, 0);
      }
      callback.onRowsReady(request, new SerializableResponse<RowType>(
          visibleItems, currentRow));
    }

    private int addTreeItem(TreeRequest request, List<RowType> visibleFiles,
        TreeItem<RowType> treeItem, boolean open, Set<String> invertedNodes,
        boolean flattened, TreeItemComparator comparator, int currentRow,
        int startRow, int numRows, int level) {
      // Check if filters are matching...
      ColumnFilterList columnFilterList = request.getColumnFilterList();
      boolean filterMatching = false;
      if ((level == 0 && filterTopLevel) || flattened) {
        filterMatching = isFilterMatching(treeItem, columnFilterList);
      } else {
        filterMatching = true;
      }
      if (filterMatching) {
        treeItem.getTreeTableItem().setRow(currentRow);
        currentRow++;
        if (currentRow > startRow && (currentRow - startRow) <= numRows) {
          visibleFiles.add(treeItem.getTreeTableItem());
        }
      }
      if ((filterMatching || !filterTopLevel) && !flattened) {
        if (treeItem.hasChildren()
            && (open
                && !invertedNodes.contains(treeItem.getTreeTableItem().getId()) || !open
                && invertedNodes.contains(treeItem.getTreeTableItem().getId()))) {
          List<TreeItem<RowType>> children = treeItem.getChildren();
          if (comparator != null && !flattened) {
            Collections.sort(children, comparator);
          }
          for (TreeItem<RowType> child : children) {
            currentRow = addTreeItem(request, visibleFiles, child, open,
                invertedNodes, flattened, comparator, currentRow, startRow,
                numRows, level + 1);
          }
        }
      }
      return currentRow;
    }

    private boolean isFilterMatching(TreeItem<RowType> treeItem,
        ColumnFilterList columnFilterList) {
      for (ColumnFilterInfo columnFilterInfo : columnFilterList) {
        List<ColumnDefinition<RowType, ?>> visibleColumnDefinitions = tableDefinition.getVisibleColumnDefinitions();
        for (ColumnDefinition<RowType, ?> columnDefinition : visibleColumnDefinitions) {
          if (columnDefinition.getColumnFilter() != null && columnFilterInfo.getColumn() == columnDefinition.getColumnFilter().getColumn()) {
            if (!columnFilterInfo.isFilterMatching(columnDefinition.getCellValue(treeItem.getTreeTableItem()))) {
              return false;
            }
          }
        }
      }
      return true;
    }
  }

  /**
   * Interface used to allow the widget access to css style names. <p/> The
   * class names indicate the default gwt names for these styles. <br>
   *
   */
  public static interface Css extends AbstractScrollTable.Css {
    /**
     * Widget style name.
     *
     * @return the widget's style name
     */
    @ClassName("gwt-TreeTable")
    String defaultStyleName();

    /**
     * Tree node wrapper
     */
    String treeNodeWrapper();

    /**
     * Indentation style
     */
    String treeTableIndent();

    /**
     * Indentation style
     */
    String treeTableIndentUp();

    /**
     * Open tree node
     */
    String treeTableOpenNode();

    /**
     * Open tree node
     */
    String treeTableClosedNode();
  }

  public interface TreeTableResources extends ScrollTableResources {
    TreeTableStyle getStyle();

    TreeTableMessages getMessages();
  }

  public interface TreeTableMessages extends ScrollTableMessages {
    @DefaultMessage("Click to open node {0}")
    String openTreeNodeTooltip(String displayName);

    @DefaultMessage("Click to close node {0}")
    String closeTreeNodeTooltip(String displayName);

    @DefaultMessage("Jump to {0}")
    String jumpTo(String displayName);
  }

  /**
   * Resources used.
   */
  public interface TreeTableStyle extends ScrollTableStyle {
    /**
     * The css file.
     */
    @Source("com/google/gwt/gen2/widgetbase/public/TreeTable.css")
    @NotStrict
    Css css();

    @Source("treeClosed.gif")
    ImageResource treeClosed();

    @Source("treeOpen.gif")
    ImageResource treeOpen();

    @Source("treeIndent.gif")
    ImageResource treeIndent();

    @Source("treeIndentUp.gif")
    ImageResource treeIndentUp();

    @Source("headerBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }

  protected static class DefaultTreeTableResources implements
      TreeTableResources {
    private TreeTableStyle style;
    private TreeTableMessages constants;

    public TreeTableStyle getStyle() {
      if (style == null) {
        style = ((TreeTableStyle) GWT.create(TreeTableStyle.class));
      }
      return style;
    }

    public TreeTableMessages getMessages() {
      if (constants == null) {
        constants = ((TreeTableMessages) GWT.create(TreeTableMessages.class));
      }
      return constants;
    }
  }

  protected static class TreeTableCellView<RowType extends TreeTableItem>
      extends PagingScrollTableCellView<RowType> {
    private TreeTable<RowType> treeTable;

    public TreeTableCellView(TreeTable<RowType> table) {
      super(table);
      this.treeTable = table;
    }

    @Override
    public void setText(String text) {
      if (getCellIndex() == 0 && !treeTable.isFlattened()) {
        ComplexPanel treeNode = renderTreeNode(getRowIndex());
        if (text != null && text.length() > 0) {
          treeNode.add(new Label(text));
        }
        String treeNodeWrapper = treeTable.getResources().getStyle().css().treeNodeWrapper();
        treeNode.setStyleName(treeNodeWrapper);
        treeTable.getDataTable().setWidget(getRowIndex(), getCellIndex(),
            treeNode);
      } else {
        super.setText(text);
      }
    }

    @Override
    public void setHTML(String html) {
      if (getCellIndex() == 0 && !treeTable.isFlattened()) {
        ComplexPanel treeNode = renderTreeNode(getRowIndex());
        String treeNodeWrapper = treeTable.getResources().getStyle().css().treeNodeWrapper();
        treeNode.setStyleName(treeNodeWrapper);
        if (html != null && html.length() > 0) {
          treeNode.add(new HTML(html));
        }
        treeTable.getDataTable().setWidget(getRowIndex(), getCellIndex(),
            treeNode);
      } else {
        super.setHTML(html);
      }
    }

    @Override
    public void setWidget(Widget widget) {
      String treeNodeWrapper = treeTable.getResources().getStyle().css().treeNodeWrapper();
      if (getCellIndex() == 0 && !treeTable.isFlattened()) {
        HorizontalPanel treeNode = renderTreeNode(getRowIndex());
        treeNode.add(widget);
        treeNode.setCellWidth(widget, "100%");
        treeNode.setStyleName(treeNodeWrapper);
        treeTable.getDataTable().setWidget(getRowIndex(), getCellIndex(),
            treeNode);
      } else {
        widget.setStyleName(treeNodeWrapper);
        super.setWidget(widget);
      }
    }

    protected HorizontalPanel renderTreeNode(int rowIndex) {
      final RowType treeTableItem = treeTable.getRowValue(getRowIndex());
      final Set<String> invertedNodes = treeTable.getInvertedNodes();
      Css css = treeTable.getResources().getStyle().css();
      TreeTableMessages messages = treeTable.getResources().getMessages();
      boolean open = treeTable.isTreeOpen();
      HorizontalPanel treeNode = new HorizontalPanel();
      treeNode.setSpacing(0);
      treeNode.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
      indent(treeNode, treeTableItem);
      if (treeTableItem.hasChildren() && invertedNodes.contains(treeTableItem.getId())) {
        HTML nodeIndicator = new HTML();
        if (treeTable.isTreeOpen()) {
          nodeIndicator.setTitle(messages.openTreeNodeTooltip(treeTableItem.getDisplayName()));
          nodeIndicator.setStyleName(css.treeTableClosedNode());
        } else {
          nodeIndicator.setTitle(messages.closeTreeNodeTooltip(treeTableItem.getDisplayName()));
          nodeIndicator.setStyleName(css.treeTableOpenNode());
        }
        nodeIndicator.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            invertedNodes.remove(treeTableItem.getId());
            treeTable.reloadPage();
          }
        });
        treeNode.add(nodeIndicator);
      } else if (treeTableItem.hasChildren()) {
        HTML nodeIndicator = new HTML();
        if (open) {
          nodeIndicator.setTitle(messages.closeTreeNodeTooltip(treeTableItem.getDisplayName()));
          nodeIndicator.setStyleName(css.treeTableOpenNode());
        } else {
          nodeIndicator.setTitle(messages.openTreeNodeTooltip(treeTableItem.getDisplayName()));
          nodeIndicator.setStyleName(css.treeTableClosedNode());
        }
        nodeIndicator.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
            invertedNodes.add(treeTableItem.getId());
            treeTable.reloadPage();
          }
        });
        treeNode.add(nodeIndicator);
      }
      return treeNode;
    }

    protected Widget createSpacer(TreeTableItem treeTableItem) {
      HTML spacer = new HTML();
      final TreeTableItem parent = treeTableItem.getParent();
      spacer.setTitle(treeTable.getResources().getMessages().jumpTo(
          parent.getDisplayName()));
      spacer.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          int row = parent.getRow();
          treeTable.gotoPage(row / treeTable.getPageSize(), false);
          treeTable.getDataTable().selectRow(row % treeTable.getPageSize(),
              true);
        }
      });
      return spacer;
    }

    protected void indent(HorizontalPanel widget, TreeTableItem item) {
      boolean first = true;
      while (item.getParent() != null) {
        Widget spacer = createSpacer(item);
        if (first) {
          spacer.setStylePrimaryName(treeTable.getResources().getStyle().css().treeTableIndent());
          first = false;
        } else {
          spacer.setStylePrimaryName(treeTable.getResources().getStyle().css().treeTableIndentUp());
        }
        widget.insert(spacer, 0);
        item = item.getParent();
      }
    }
  }

  /**
   * A custom {@link AbstractRowView} used by the {@link TreeTable}.
   *
   * @param <RowType> the type of the row values
   */
  protected static class TreeTableRowView<RowType extends TreeTableItem>
      extends AbstractRowView<RowType> {
    private TreeTable<RowType> table;

    public TreeTableRowView(TreeTableCellView<RowType> cellView,
        TreeTable<RowType> table) {
      super(cellView);
      this.table = table;
    }

    @Override
    public void setStyleAttribute(String attr, String value) {
      table.getDataTable().getFixedWidthGridRowFormatter().getRawElement(
          getRowIndex()).getStyle().setProperty(attr, value);
    }

    @Override
    public void setStyleName(String stylename) {
      table.getDataTable().getRowFormatter().setStyleName(getRowIndex(),
          stylename);
    }

    @Override
    public void removeStyleName(String stylename) {
      table.getDataTable().getRowFormatter().removeStyleName(getRowIndex(),
          stylename);
    }
  }

  protected boolean open = false;
  protected boolean flattened = false;
  protected Set<String> invertedNodes = new HashSet<String>();
  protected TreeTableResources resources;
  protected TreeTableRowView<RowType> rowView;

  public TreeTable(TableDefinition<RowType> tableDefinition,
      List<TreeItem<RowType>> rootItems) {
    this(new ClientTreeTableModel<RowType>(tableDefinition, rootItems, true),
        new FixedWidthGrid(), new FixedWidthFlexTable(), tableDefinition, false);
  }

  public TreeTable(TableDefinition<RowType> tableDefinition,
      List<TreeItem<RowType>> rootItems, boolean open) {
    this(new ClientTreeTableModel<RowType>(tableDefinition, rootItems, true),
        new FixedWidthGrid(), new FixedWidthFlexTable(), tableDefinition, open);
  }

  public TreeTable(TreeTableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition, boolean open) {
    this(tableModel, dataTable, headerTable, tableDefinition, open,
        new DefaultTreeTableResources());
  }

  public TreeTable(TreeTableModel<RowType> tableModel,
      FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
      TableDefinition<RowType> tableDefinition, boolean open,
      TreeTableResources resources) {
    super(tableModel, dataTable, headerTable, tableDefinition, resources);
    // Setup the empty table widget wrapper
    this.resources = resources;
    this.open = open;
    rowView = new TreeTableRowView<RowType>(
        new TreeTableCellView<RowType>(this), this);
    setCellPadding(0);
    setCellSpacing(0);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.google.gwt.gen2.table.client.PagingScrollTable#gotoPage(int,
   * boolean)
   */
  public void gotoPage(int page, boolean forced) {
    super.gotoPage(page, forced);
    redraw();
  }

  /**
   * Return a set of nodes containing all tree nodes that differ from the tree
   * state
   *
   * @return the list of inverted nodes
   */
  public Set<String> getInvertedNodes() {
    return invertedNodes;
  }

  public boolean isFlattened() {
    return flattened;
  }

  /**
   * @return the default state of the tree nodes. true is all nodes are
   *         displayed open by default, false if all nodes are closed
   */
  public boolean isTreeOpen() {
    return open;
  }

  public TreeTableResources getResources() {
    return resources;
  }

  /**
   * Opens all tree nodes
   */
  public void openTree() {
    setTreeOpen(true);
  }

  /**
   * Closes all tree nodes
   */
  public void closeTree() {
    setTreeOpen(false);
  }

  public void setFlattened(boolean flattened) {
    this.flattened = flattened;
    reloadPage();
  }

  /**
   * Opens the given tree item if children are available
   *
   * @param item the tree node to open
   */
  public void openTreeNode(TreeTableItem item) {
    setTreeNodeOpen(item, true);
  }

  /**
   * Closes the given tree node
   *
   * @param item the tree node to open
   */
  public void closeTreeNode(TreeTableItem item) {
    setTreeNodeOpen(item, false);
  }

  protected Request createRequest(int startRow, int pageSize,
      ColumnSortList columnSortList, ColumnFilterList columnFilterList) {
    return new TreeRequest(startRow, pageSize, columnSortList,
        columnFilterList, open, invertedNodes, flattened);
  }

  @Override
  protected AbstractRowView<RowType> getRowView() {
    return rowView;
  }

  protected void setTreeOpen(boolean open) {
    this.open = open;
    invertedNodes.clear();
    gotoPage(0, true);
  }

  protected void setTreeNodeOpen(TreeTableItem item, boolean open) {
    if (item.hasChildren()) {
      if (isTreeOpen() && open) {
        invertedNodes.remove(item.getId());
      } else {
        invertedNodes.add(item.getId());
      }
    }
  }
}