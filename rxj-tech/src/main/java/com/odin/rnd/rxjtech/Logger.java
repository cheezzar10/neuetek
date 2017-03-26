package com.odin.rnd.rxjtech;

public class Logger {
	public static final void log(String message) {
		System.out.printf("[%s] - %s%n", Thread.currentThread().getName(), message);
	}
}
