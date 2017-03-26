package com.odin.rnd.rxjtech;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class NotificationChannelLink implements ObservableOnSubscribe<Notification> {
	private ObservableEmitter<Notification> emitter;

	@Override
	public void subscribe(ObservableEmitter<Notification> emitter) throws Exception {
		Logger.log("subscribing");
		this.emitter = emitter;
		Logger.log("subscribed");
	}
	
	public void send(Notification notification) {
		Logger.log("sending using channel link");
		emitter.onNext(notification);
	}
}
