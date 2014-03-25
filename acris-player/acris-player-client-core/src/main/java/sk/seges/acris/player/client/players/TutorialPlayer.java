package sk.seges.acris.player.client.players;

import sk.seges.acris.player.client.objects.AnimationObject;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class TutorialPlayer extends Player {

	public TutorialPlayer(CacheMap cacheMap) {
		super(AnimationObject.PRESENTATION_OBJECT_SPEED, cacheMap);
	}
}
