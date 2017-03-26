package com.odin.rnd.rxjtech;

public class NotificationDispatcher {
	private ClientNotificationChannel clientChannel;
	private Thread dispatchingThread;
	
	private boolean stopped = false;

	public NotificationDispatcher(ClientNotificationChannel clientChannel) {
		this.clientChannel = clientChannel;
	}

	public void start() {
		dispatchingThread = new Thread(() -> {
			int count = 1;
			while (!stopped) {
				Retarder.pause(1_000);
				clientChannel.send(new Notification(1, "boom: " + count));
				++count;
			}
		}, "dispatcher");
		dispatchingThread.start();
		Logger.log("dispatcher started");
	}

	public void stop() {
		dispatchingThread.interrupt();
		stopped = true;
		Logger.log("dispatcher stopped");
	}
}
