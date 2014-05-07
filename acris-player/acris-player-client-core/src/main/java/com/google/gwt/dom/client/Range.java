package com.google.gwt.dom.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
* Created by PeterSimun on 20.4.2014.
 * A Range object represents a contiguous part of the document.
 *
 * If you want to align a Range object to an element or to its contents, use the selectNode and selectNodeContents
 * methods.
 *
 * The Range object is supported in Internet Explorer from version 9.
 * //TODO add IE <9 support using createTextRange
 */
public class Range extends JavaScriptObject {

    protected Range() {}

    public enum Unit {
        WORD("word"), CHARACTER("character"), SENTENSE("sentense");

        private final String val;

        private Unit(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }
    /**
     * Creates an empty Range object.
     */
    public final static native Range createRange(Node node) /*-{
        return node.ownerDocument.createRange();
    }-*/;

    /**
     * Sets the Range to contain the contents of a Node.
     *
     * The parent Node of the start and end of the Range will be the reference node.
     * The startOffset is 0, and the endOffset is the number of child Nodes or number of characters contained in the reference node.
     *
     * @param referenceNode The Node whose contents will be selected within a Range.
     */
    public final native void selectNodeContents(Node referenceNode) /*-{
        this.selectNodeContents(referenceNode);
    }-*/;

    public final native void selectNode(Node referenceNode) /*-{
        this.selectNode(referenceNode);
    }-*/;

    /**
     * @return a number representing where in the Range.endContainer the Range ends.
     * If the endContainer is a Node of type Text, Comment, or CDATASEction, then the offset is the number of characters
     * from the start of the endContainer to the boundary point of the Range.
     *
     * For other Node types, the endOffset is the number of child nodes between the start of the endContainer and the
     * boundary point of the Range.
     * To change the endOffset of a Range, use one of the setEnd methods.
     */
    public final native int getEndOffset() /*-{
        return this.endOffset;
    }-*/;

    /**
     * @return a number representing where in the startContainer the Range starts.
     * If the startContainer is a Node of type Text, Comment, or CDATASection, then the offset is the number of characters
     * from the start of the startContainer to the boundary point of the Range.
     *
     * For other Node types, the startOffset is the number of child nodes between the start of the startContainer and the
     * boundary point of the Range.
     * To change the startOffset of a Range, use the setStart method.
     */
    public final native int getStartOffset() /*-{
        return this.startOffset;
    }-*/;

    /**
     * Sets the start position of a Range. If the startNode is a Node of type Text, Comment, or CDATASection, then
     * startOffset is the number of characters from the start of startNode.
     * For other Node types, startOffset is the number of child nodes between the start of the startNode.
     *
     * Setting the start point below (further down in the document) than the end point will throw an ERROR_ILLEGAL_VALUE DOMException.
     *
     * @param node The Node where the Range should start.
     * @param startOffset An integer greater than or equal to zero representing the offset for the start of the Range from the start of startNode.
     */
    public final native void setStart(Node node, int startOffset) /*-{
        this.setStart(node, startOffset);
    }-*/;

    /**
     * Sets the end position of a Range. If the endNode is a Node of type Text, Comment, or CDATASection, then
     * endOffset is the number of characters from the start of endNode.
     * For other Node types, endOffset is the number of child nodes between the start of the endNode.
     *
     * Setting the end point above (higher in the document) the start point will throw an ERROR_ILLEGAL_VALUE DOMException.
     *
     * @param node The Node where the Range should end.
     * @param endOffset An integer greater than or equal to zero representing the offset for the end of the Range from the start of endNode.
     */
    public final native void setEnd(Node node, int endOffset) /*-{
        this.setEnd(node, endOffset);
    }-*/;

    /**
     * @return a ClientRect object that bounds the contents of the range; this a rectangle enclosing the union of the
     * bounding rectangles for all the elements in the range.
     */
    public final native ClientRect getBoundingClientRect() /*-{
        return this.getBoundingClientRect();
    }-*/;

    /**
     * Expands the contents of the TextRange from the insertion point by a character, sentence or word.
     * If you need to move only the start or end point of a TextRange, use the moveStart and moveEnd methods
     *
     * @param unit character, sentence or word
     *
     * @return Boolean. One of the following values:
     *      false	The range was not expanded.
     *      true	The range was successfully expanded.
     */
    public final native void expand(Unit unit) /*-{
        this.expand(unit.toString());
    }-*/;

    /**
     * Method is a stringifier returning the text of the Range.
     *
     * Alerting the contents of a Range makes an implicit toString() call, so comparing range and text through an alert dialog is ineffective
     */
    public final native String stringify() /*-{
        return this.toString();
    }-*/;

    /**
     * Method releases a Range from use.
     * This lets the browser choose to release resources associated with this Range.
     * Subsequent attempts to use the detached range will result in a DOMException being thrown with an error code of INVALID_STATE_ERR.
     */
    public final native String detach() /*-{
        return this.detach();
    }-*/;
}