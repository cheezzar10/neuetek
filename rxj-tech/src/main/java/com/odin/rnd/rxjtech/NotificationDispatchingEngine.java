package com.odin.rnd.rxjtech;

import io.reactivex.Observable;

public class NotificationDispatchingEngine {
	public static void main(String[] args) {
		Logger.log("engine started");
		
		NotificationChannelLink link = new NotificationChannelLink();
		Observable<Notification> observable = Observable.create(link);
		
		ClientNotificationChannel clientChannel = new ClientNotificationChannel();
		NotificationDispatcher dispatcher = new NotificationDispatcher(clientChannel);
		dispatcher.start();
		
		Retarder.pause(2_000);
		NotificationChannelObserver observer = new NotificationChannelObserver();
		clientChannel.connect(observer);
		
		Retarder.pause(5_000);
		clientChannel.disconnect();
		
		Retarder.pause(5_000);
		dispatcher.stop();
		
		Logger.log("engine stopped");
	}
}