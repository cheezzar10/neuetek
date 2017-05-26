import static java.lang.System.out;

public class Main {
	public static void main(String[] args) {
		out.println("started");
		fillUsingNThreads(12);
	}

	private static void fillUsingNThreads(int threadCount) {
		out.printf("populating cache using %d threads%n", threadCount);
		Cache cache = new Cache();
		Thread[] fillThreads = new Thread[threadCount];
		for (int i=0;i<threadCount;i++) {
			fillThreads[i] = new Thread(new Filler(cache, 1048576), "client-" + i);
		}

		for (int i=0;i<threadCount;i++) {
			fillThreads[i].start();
		}

		for (int i=0;i<threadCount;i++) {
			try {
				fillThreads[i].join();
			} catch (InterruptedException threadInterrupted) {
				Thread.currentThread().interrupt();
			}
		}

		cache.detect();
	}
}