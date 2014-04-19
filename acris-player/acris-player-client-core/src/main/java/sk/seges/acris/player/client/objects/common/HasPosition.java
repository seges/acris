package sk.seges.acris.player.client.objects.common;

/**
 * Created by PeterSimun on 11.4.2014.
 */
public interface HasPosition {

    public static final int UNDEFINED = -1;

    void setPosition(int x, int y);

    int getObjectPositionX();

    int getObjectPositionY();
}
