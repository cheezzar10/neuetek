import java.util.Random;

public class Filler implements Runnable {
	private final Cache cache;
	private final int maxWrites;
	private final Random keyGen = new Random(); 

	public Filler(Cache cache, int maxWrites) {
		this.cache = cache;
		this.maxWrites = maxWrites;
	}

	public void run() {
		System.out.printf("%s performing %d cache writes%n", Thread.currentThread().getName(), maxWrites);
		for (int i=0;i<maxWrites;i++) {
			String key = getKey();
			try {
				Integer value = cache.get(key);
			} catch (Exception ex) {
				// System.out.printf("%s loading failed for key: %s%n", Thread.currentThread().getName(), key);
			}
		}
		System.out.printf("%s cache filled%n", Thread.currentThread().getName());
	}

	private String getKey() {
		return String.valueOf(keyGen.nextInt(16));
	}
}