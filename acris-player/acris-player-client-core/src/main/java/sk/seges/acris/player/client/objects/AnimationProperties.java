package sk.seges.acris.player.client.objects;

import sk.seges.acris.recorder.client.event.MouseEvent;


class AnimationProperties {
	private int objectPositionX;
	private int cursorPositionY;
	private int targetPositionX;
	private int targetPositionY;
	private String targetElementXpath;
	private int waitTime;
	private AnimationObject animationObject;
	private MouseEvent event;

	public MouseEvent getEvent() {
		return event;
	}

	public void setEvent(MouseEvent event) {
		this.event = event;
	}

	public AnimationProperties(AnimationObject animationObject) {
		this.animationObject = animationObject;
	}
	
	public AnimationObject getAnimationObject() {
		return animationObject;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public String getTargetElementXpath() {
		return targetElementXpath;
	}

	public void setTargetElementXpath(String targetElementXpath) {
		this.targetElementXpath = targetElementXpath;
	}

	private EObjectActionType objectActionType;

	public int getObjectPositionX() {
		return objectPositionX;
	}

	public void setObjectPositionX(int objectPositionX) {
		this.objectPositionX = objectPositionX;
	}

	public int getObjectPositionY() {
		return cursorPositionY;
	}

	public void setObjectPositionY(int objectPositionY) {
		this.cursorPositionY = objectPositionY;
	}

	public int getTargetPositionX() {
		return targetPositionX;
	}

	public void setTargetPositionX(int targetPositionX) {
		this.targetPositionX = targetPositionX;
	}

	public int getTargetPositionY() {
		return targetPositionY;
	}

	public void setTargetPositionY(int targetPositionY) {
		this.targetPositionY = targetPositionY;
	}

	public EObjectActionType getObjectActionType() {
		return objectActionType;
	}

	public void setObjectActionType(EObjectActionType objectActionType) {
		this.objectActionType = objectActionType;
	}
}