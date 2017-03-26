package com.odin.rnd.rxjtech;

public class Retarder {
	public static void pause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}
}
