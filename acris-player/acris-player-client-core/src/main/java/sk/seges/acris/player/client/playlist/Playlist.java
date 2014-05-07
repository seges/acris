package sk.seges.acris.player.client.playlist;

import sk.seges.acris.recorder.client.event.generic.AbstractGenericEvent;
import sk.seges.sesam.handler.ValueChangeHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Playlist {

    private List<AbstractGenericEvent> listEvent = new LinkedList<AbstractGenericEvent>();
    private List<ValueChangeHandler<Integer>> valueChangeHandlers = new ArrayList<ValueChangeHandler<Integer>>();

    private int current = 0;

    public void addEvent(AbstractGenericEvent e) {
        listEvent.add(e);
    }

    public int getEventsCount() {
        return listEvent.size();
    }

    public AbstractGenericEvent getEvent(int i) {
        return listEvent.get(i);
    }

    public void clear() {
        listEvent.clear();
    }

    public void addValueChangeHandler(ValueChangeHandler<Integer> handler) {
        valueChangeHandlers.add(handler);
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void selectNextEvent() {
        for (ValueChangeHandler handler: valueChangeHandlers) {
            handler.onValueChanged(current, current+1);
        }
        current++;
    }
}