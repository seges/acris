package sk.seges.acris.player.client.objects.utils;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.TextAreaElement;

/**
 * Created by PeterSimun on 22.4.2014.
 */
public class ElementResolver {

    public static boolean isInput(Node node) {
        return node.getNodeName().toLowerCase().equals(InputElement.TAG) ||
               node.getNodeName().toLowerCase().equals(TextAreaElement.TAG);
    }
}
