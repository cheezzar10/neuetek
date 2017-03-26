package com.odin.rnd.rxjtech;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

// TODO rename to NotificationChannelLink
public class NotificationChannelObserver implements Observer<Notification> {
	private Disposable disp;

	@Override
	public void onComplete() {
		Logger.log("onComplete()");
	}

	@Override
	public void onError(Throwable error) {
		Logger.log("onError()");
	}

	@Override
	public void onNext(Notification notification) {
		Logger.log("onNext(" + notification + ")");
	}

	@Override
	public void onSubscribe(Disposable disp) {
		Logger.log("onSubscribe(" + disp.getClass().getName() + "@" + disp.hashCode() + ")");
		this.disp = disp;
	}

	public void disconnect() {
		disp.dispose();
	}
}
