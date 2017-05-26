import java.util.Random;

public class Filler implements Runnable {
	private static final int INVALIDATION_INTERVAL = 16 * 1024;
	
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
			
			if (i % INVALIDATION_INTERVAL == 0) {
				cache.invalidate(key);
				continue;
			}
			
			try {
				Integer value = cache.get(key, true);
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