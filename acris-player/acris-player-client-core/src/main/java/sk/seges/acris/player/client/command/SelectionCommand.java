package sk.seges.acris.player.client.command;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import sk.seges.acris.player.client.exception.ReplayException;
import sk.seges.acris.player.client.objects.utils.ElementResolver;
import sk.seges.acris.player.client.objects.utils.SelectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeterSimun on 21.4.2014.
 */
public class SelectionCommand implements Command {

    private final SelectionUtils.SelectionHolder selectionStart;

    private final SelectionUtils.SelectionHolder selectionEnd;

    public SelectionCommand(SelectionUtils.SelectionHolder selectionStart, SelectionUtils.SelectionHolder selectionEnd) {
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    }

    protected int getTextPosition(Node ancestorNode, Node node) {
        return getTextPosition(ancestorNode, node, 0);
    }

    private int getTextPosition(Node ancestorNode, Node node, int currentIndex) {

        if (ancestorNode == node) {
            return currentIndex;
        }

        Node parentNode = node.getParentNode();

        while ((node = node.getPreviousSibling()) != null) {
            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
                currentIndex += ((Element)node.cast()).getInnerText().length();
            }
        }

        return getTextPosition(ancestorNode, parentNode, currentIndex);
    }

    @Override
    public void execute() {
        if (selectionEnd != null) {
            if (selectionStart.node != selectionEnd.node && !ElementResolver.isInput(selectionStart.node)) {
                //selection over multiple nodes
                Node ancestor = getCommonAncestor(selectionStart.node, selectionEnd.node);

                if (ancestor == null) {
                    throw new ReplayException("Unable to select text over multiple node. Common ancestor was not found!", null);
                }

                if (selectionStart.point.y < selectionEnd.point.y) {
                    //selecting from up to down
                    int start = getTextPosition(ancestor, selectionStart.node) + selectionStart.start;
                    int end = getTextPosition(ancestor, selectionEnd.node) + selectionEnd.end;
                    new SelectionUtils(ancestor).selectText(start, end);
                } else {
                    //selecting from down to up
                    int start = getTextPosition(ancestor, selectionEnd.node) + selectionEnd.end;
                    int end = getTextPosition(ancestor, selectionStart.node) + selectionStart.start;
                    new SelectionUtils(ancestor).selectText(start, end);
                }
            } else {
                //selection in the single node
                if (selectionEnd.end < selectionStart.start) {
                    new SelectionUtils(selectionStart.node).selectText(selectionStart.end, selectionEnd.start);
                } else {
                    new SelectionUtils(selectionStart.node).selectText(selectionStart.start, selectionEnd.end);
                }
            }
        }
    }

    private List<Node> getParents(Node node) {
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(node);

        for (; node != null; node = node.getParentNode()) {
            nodes.add(0, node);
        }

        return nodes;
    }

    private Node getCommonAncestor(Node node1, Node node2) {
        List<Node> parents1 = getParents(node1);
        List<Node> parents2 = getParents(node2);

        if (parents1.get(0) != parents2.get(0)) {
            return null;
        }

        int i = 0;
        for (; i < Math.min(parents1.size(), parents2.size()); i++) {
            if (parents1.get(i) != parents2.get(i)) {
                return parents1.get(i - 1);
            }
        }

        //this looks that the nodes are same
        return parents1.get(i - 1);
    }
}