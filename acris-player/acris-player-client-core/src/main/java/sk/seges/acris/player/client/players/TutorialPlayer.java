package sk.seges.acris.player.client.players;

import sk.seges.acris.player.client.objects.common.AnimationObject;

public class TutorialPlayer extends Player {

	public TutorialPlayer(boolean viewMode) {
		super(viewMode, AnimationObject.PRESENTATION_OBJECT_SPEED);
	}
}
