package sk.seges.acris.player.client.objects.utils;

import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.player.client.objects.common.EventProperties;
import sk.seges.acris.recorder.client.event.MouseEvent;

public class PositionUtils {

	public static int calculateDuration(EventProperties properties, int speed) {
		double time = calculateDistance(properties)/speed;
		time *= 1000;
		return (int)(time+0.5);
	}

	public static double calculateDistance(EventProperties properties) {
		MouseEvent mouseEvent = (MouseEvent)properties.getEvent();

		AnimationObject animationObject = (AnimationObject) properties.getEventMirror();

		return calculateDistance(animationObject.getObjectPositionX(), animationObject.getObjectPositionY(),
				mouseEvent.getClientX(), mouseEvent.getClientY());
	}

	public static double calculateDistance(int x, int y, int tx, int ty) {
		double dy = ty - y;
		double dx = tx - x;

		return Math.sqrt(dy * dy + dx * dx);
	}

}
