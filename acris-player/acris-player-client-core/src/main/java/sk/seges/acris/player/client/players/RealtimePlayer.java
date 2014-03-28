package sk.seges.acris.player.client.players;

import sk.seges.acris.player.client.objects.common.AnimationObject;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class RealtimePlayer extends Player {

	public RealtimePlayer(CacheMap cacheMap) {
		super(AnimationObject.REALTIME_OBJECT_SPEED, cacheMap);
	}
}
