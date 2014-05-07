package sk.seges.acris.player.client.objects.utils;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.Element;

/**
 * Created by PeterSimun on 19.4.2014.
 */
public class SelectionUtils {

    private final Node node;

    public SelectionUtils(Node node) {
        this.node = node;
    }

    private native void replaceWithLess(int start, int len, JsArray range, Element el) /*-{
        if (typeof range[0] === 'number' && range[0] < len) {
            range[0] = {
                el: el,
                count: range[0] - start
            };
        }
        if (typeof range[1] === 'number' && range[1] <= len) {
            range[1] = {
                el: el,
                count: range[1] - start
            };
        }
    }-*/;

    private native int getCharElement(JsArray elems, JsArray range, int len) /*-{
        var elem, start;

        len = len || 0;
        for (var i = 0; elems[i]; i++) {
            elem = elems[i];

            if (elem.nodeType === 3 || elem.nodeType === 4) {
                start = len
                len += elem.nodeValue.length;

                this.@sk.seges.acris.player.client.objects.utils.SelectionUtils::replaceWithLess(IILcom/google/gwt/core/client/JsArray;Lcom/google/gwt/user/client/Element;)(start, len, range, elem)

            } else if (elem.nodeType !== 8) {
                len = this.@sk.seges.acris.player.client.objects.utils.SelectionUtils::getCharElement(Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;I)(elem.childNodes, range, len);
            }
        }
        return len;
    }-*/;

    public void selectText(int start, int end) {
        selectText(node, start, end);
    }

    private native void selectText(Node el, int start, int end) /*-{

        var win = el ? el.ownerDocument.defaultView || el.ownerDocument.parentWindow : $wnd

        if (el.setSelectionRange) {
            if (end === undefined) {
                el.focus();
                el.setSelectionRange(start, start);
            } else {
                el.select();
                el.selectionStart = start;
                el.selectionEnd = end;
            }
        } else if (el.createTextRange) {
            var r = el.createTextRange();
            r.moveStart('character', start);
            end = end || start;
            r.moveEnd('character', end - el.value.length);

            r.select();
        } else if (win.getSelection) {
            var doc = win.document,
                sel = win.getSelection(),
                range = doc.createRange(),
                ranges = [start, end !== undefined ? end : start];
            this.@sk.seges.acris.player.client.objects.utils.SelectionUtils::getCharElement(Lcom/google/gwt/core/client/JsArray;Lcom/google/gwt/core/client/JsArray;I)([el], ranges, 0);
            range.setStart(ranges[0].el, ranges[0].count);
            range.setEnd(ranges[1].el, ranges[1].count);

            sel.removeAllRanges();
            sel.addRange(range);

        } else if (win.document.body.createTextRange) { //IE's weirdness
            var range = document.body.createTextRange();
            range.moveToElementText(el);
            range.collapse()
            range.moveStart('character', start)
            range.moveEnd('character', end !== undefined ? end : start)
            range.select();
        }
    }-*/;

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class SelectionHolder {
        public Node node;
        public int start;
        public int end;
        public Point point;

        public SelectionHolder(Node node, int start, int end, Point point) {
            this.node = node;
            this.start = start;
            this.end = end;
            this.point = point;
        }
    }

    private static native String getComputedStyle(com.google.gwt.dom.client.Element element, String cssRule) /*-{
        var strValue = "";
        if (element) {
            if (element.currentStyle) {
                cssRule = cssRule.replace(/\-(\w)/g, function(strMatch, p1) {
                    return p1.toUpperCase();
                });
                strValue = element.currentStyle[cssRule] + "";
            } else if ($doc.defaultView.getComputedStyle) {
                strValue = $doc.defaultView.getComputedStyle(element, null).getPropertyValue(cssRule.replace(/([A-Z])/g, "-$1").toLowerCase());
            }
        }
        return strValue;
    }-*/;

    private int getComputedIntStyle(com.google.gwt.dom.client.Element element, String cssRule) {
        String computedStyle = getComputedStyle(element, cssRule).trim();

        if (computedStyle.toLowerCase().endsWith(Style.Unit.PX.getType())) {
            computedStyle = computedStyle.replaceAll(Style.Unit.PX.getType(), "").trim();
        } else {
            //TODO add support for other units
        }

        if (computedStyle == null) {
            return 0;
        }
        return Double.valueOf(computedStyle).intValue();
    }

    public SelectionHolder getCharacterAtPoint(int x, int y) {
        return getTextAtPoint(Range.Unit.CHARACTER, x, y);
    }

    public SelectionHolder getWordAtPoint(int x, int y) {
        return getTextAtPoint(Range.Unit.WORD, x, y);
    }

    private SelectionHolder getTextAtPoint(Range.Unit unit, int x, int y) {
        if (node.getNodeName().toLowerCase().equals(InputElement.TAG)) {
            return getTextAtPoint(unit, (InputElement) node.cast(), x, y);
        }
        return getTextAtPoint(unit, node, x, y);
    }

    private SelectionHolder getTextAtPoint(Range.Unit unit, InputElement input, int x, int y) {

        int inputTop = input.getAbsoluteTop();
        int inputLeft = input.getAbsoluteLeft();

        int width = getComputedIntStyle(input, "width");
        int height = getComputedIntStyle(input, "height");

        // Styles to simulate a node in an input field
        inputTop += getComputedIntStyle(input, "paddingTop");
        inputTop += getComputedIntStyle(input, "borderTopWidth");
        inputTop--; //Seems to be necessary - at least for chrome
        inputLeft += getComputedIntStyle(input, "paddingLeft");
        inputLeft += getComputedIntStyle(input, "borderLeftWidth");

        DivElement fakeClone = Document.get().createDivElement();

        fakeClone.getStyle().setWhiteSpace(Style.WhiteSpace.PRE);
        fakeClone.getStyle().setPadding(0, Style.Unit.PX);
        fakeClone.getStyle().setMargin(0, Style.Unit.PX);
        fakeClone.getStyle().setZIndex(1000);

        String[] listOfModifiers = new String[] {
                "direction", "fontFamily", "fontSize", "fontSizeAdjust", "fontVariant", "fontWeight", "fontStyle",
                "letterSpacing", "lineHeight", "textAlign", "textIndent", "textTransform", "wordWrap", "wordSpacing"};

        for (int i = 0; i < listOfModifiers.length; i++) {
            String property = listOfModifiers[i];
            fakeClone.getStyle().setProperty(property, getComputedStyle(input, property));
        }

        String text = input.getValue();

        // Styles to position the text node at the desired position
        fakeClone.getStyle().setPosition(Style.Position.ABSOLUTE);
        fakeClone.getStyle().setLeft(inputLeft, Style.Unit.PX);
        fakeClone.getStyle().setTop(inputTop, Style.Unit.PX);
        fakeClone.getStyle().setWidth(width, Style.Unit.PX);
        fakeClone.getStyle().setHeight(height, Style.Unit.PX);

        fakeClone.setInnerText(text);
        Document.get().getBody().appendChild(fakeClone);

        SelectionHolder selectionHolder = getTextAtPoint(unit, fakeClone, x, y);

        fakeClone.removeFromParent();

        return selectionHolder;
    }

    private SelectionHolder getTextAtPoint(Range.Unit unit, Node node, int x, int y) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            Range range = Range.createRange(node);
            range.selectNodeContents(node);

            int currentPos = 0;
            int endPos = range.getEndOffset();

            while (currentPos + 1 < endPos) {
                range.setStart(node, currentPos);
                range.setEnd(node, ++currentPos);

                ClientRect boundingClientRect = range.getBoundingClientRect();

                if (boundingClientRect.getLeft() <= x && boundingClientRect.getRight()  >= x &&
                    boundingClientRect.getTop()  <= y && boundingClientRect.getBottom() >= y) {

                    range.expand(unit);
                    String ret = range.stringify();

                    int start = range.getStartOffset();
                    range.detach();

                    if (start > endPos) {
                        start = 0;
                    }

                    return new SelectionHolder(node, start, start + ret.length(), new Point(x, y)) ;
                }
            }

            //Still in the bounding rect of the DIV but not over any of the character - so after
            //last char - at the empty space
            return new SelectionHolder(node, endPos, endPos, new Point(x, y)) ;
        } else {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node item = node.getChildNodes().getItem(i);
                Range range = Range.createRange(item);
                range.selectNodeContents(item);

                ClientRect boundingClientRect = range.getBoundingClientRect();

                if (boundingClientRect.getLeft() <= x && boundingClientRect.getRight() >= x &&
                    boundingClientRect.getTop()  <= y && boundingClientRect.getBottom() >= y) {
                    range.detach();
                    return getTextAtPoint(unit, item, x, y);
                } else {
                    range.detach();
                }
            }
        }

        return null;
    }
}