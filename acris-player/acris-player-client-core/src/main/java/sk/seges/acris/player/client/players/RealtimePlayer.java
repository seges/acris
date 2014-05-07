package sk.seges.acris.player.client.players;

import sk.seges.acris.player.client.objects.common.AnimationObject;

public class RealtimePlayer extends Player {

	public RealtimePlayer(boolean viewMode) {
		super(viewMode, AnimationObject.REALTIME_OBJECT_SPEED);
	}
}
