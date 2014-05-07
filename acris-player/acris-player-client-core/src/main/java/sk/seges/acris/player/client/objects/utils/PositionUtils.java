package sk.seges.acris.player.client.objects.utils;

import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.MouseEvent;

public class PositionUtils {

	public static int calculateDuration(EventProperties properties, int speed) {
        if (speed == AnimationObject.NO_SPEED) {
            return 0;
        }
		double time = calculateDistance(properties) / speed;
		return (int)(time * 1000);
	}

	public static double calculateDistance(EventProperties properties) {
		MouseEvent mouseEvent = (MouseEvent)properties.getEvent();

		AnimationObject animationObject = (AnimationObject) properties.getEventMirror();

		return calculateDistance(animationObject.getObjectPositionX(), animationObject.getObjectPositionY(),
				mouseEvent.getAbsoluteClientX(), mouseEvent.getAbsoluteClientY());
	}

	public static double calculateDistance(int x, int y, int tx, int ty) {
		double dy = ty - y;
		double dx = tx - x;

		return Math.sqrt(dy * dy + dx * dx);
	}

}
