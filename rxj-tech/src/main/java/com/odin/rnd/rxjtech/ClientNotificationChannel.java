package com.odin.rnd.rxjtech;

import io.reactivex.subjects.ReplaySubject;

// client - server channel interconnect
public class ClientNotificationChannel {
	// server channel connector
	private final ReplaySubject<Notification> serverChannelLink;
	
	// client channel connector
	private NotificationChannelObserver clientChannelLink;
	
	public ClientNotificationChannel() {
		this.serverChannelLink = ReplaySubject.<Notification>createWithSize(64);
	}

	public void send(Notification notification) {
		Logger.log("sending via channel");
		serverChannelLink.onNext(notification);
	}

	public void connect(NotificationChannelObserver clntChannelLink) {
		Logger.log("channel connected");
		clientChannelLink = clntChannelLink;
		serverChannelLink.subscribe(clientChannelLink);
	}
	
	public void disconnect() {
		Logger.log("disconnecting channel");
		clientChannelLink.disconnect();
	}
}
