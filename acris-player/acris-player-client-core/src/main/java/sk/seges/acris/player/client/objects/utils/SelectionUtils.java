package sk.seges.acris.player.client.objects.utils;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.user.client.Element;

/**
 * Created by PeterSimun on 19.4.2014.
 */
public class SelectionUtils {

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

    public native void selectText(Element el, int start, int end) /*-{

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

    public native JsArrayNumber getWordAtPoint(Element elem, int x, int y) /*-{
        if(elem.nodeType == elem.TEXT_NODE) {
            var range = elem.ownerDocument.createRange();
            range.selectNodeContents(elem);
            var currentPos = 0;
            var endPos = range.endOffset;
            while(currentPos+1 < endPos) {
                range.setStart(elem, currentPos);
                range.setEnd(elem, currentPos+1);
                if(range.getBoundingClientRect().left <= x && range.getBoundingClientRect().right  >= x &&
                    range.getBoundingClientRect().top  <= y && range.getBoundingClientRect().bottom >= y) {
                    range.expand("word");
                    var ret = range.toString();
                    range.detach();
                    return [ currentPos, currentPos + ret.length] ;
                }
                currentPos += 1;
            }
        } else {
            for(var i = 0; i < elem.childNodes.length; i++) {
                var range = elem.childNodes[i].ownerDocument.createRange();
                range.selectNodeContents(elem.childNodes[i]);
                if(range.getBoundingClientRect().left <= x && range.getBoundingClientRect().right  >= x &&
                    range.getBoundingClientRect().top  <= y && range.getBoundingClientRect().bottom >= y) {
                    range.detach();
                    return(this.@sk.seges.acris.player.client.objects.utils.SelectionUtils::getWordAtPoint(Lcom/google/gwt/user/client/Element;II)(elem.childNodes[i], x, y));
                } else {
                    range.detach();
                }
            }
        }
        return(null);
    }-*/;

}
