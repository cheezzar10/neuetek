package com.imc.rnd.procrun;

public class InfiniteLoop {
	public static void main(String[] args) {
		System.out.println("started");
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("exiting");
		}));

		try {
			System.out.println("sleeping");
			int lc = 0;
			while (true) {
				if (lc == 100000) {
					if (Thread.currentThread().isInterrupted()) {
						System.out.println("interruption detected");
						break;
					}
					lc = 0;
				} else {
					lc++;
				}
			}
			Thread.sleep(1);
			System.out.println("awakened");
		} catch (InterruptedException intrEx) {
			System.out.println("interrupted");
			System.exit(33);
		}
		System.out.println("exited");
	}
}
