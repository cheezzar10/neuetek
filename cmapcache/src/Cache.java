import java.util.Map;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import java.util.concurrent.atomic.AtomicInteger;

// it's not a cache but TX specific objects storage
public class Cache {
	private final ConcurrentMap<String, CacheValue<Integer>> cache = new ConcurrentHashMap<>();
	// miltuple TX service requests detector
	private final ConcurrentMap<String, AtomicInteger> detector = new ConcurrentHashMap<>();

	public Integer get(String txId) {
		CacheValue<Integer> value = cache.get(txId);
		if (value == null) {
			value = new CacheValue<>(new FutureTask<>(() -> getObject(txId)));
			CacheValue<Integer> prlValue = cache.putIfAbsent(txId, value);
			if (prlValue == null) {
				value.getLoader().run();
			} else {
				value = prlValue;
			}
		}
		
		try {
			return value.getLoader().get();
		} catch (InterruptedException interruptedEx) {
			throw new IllegalStateException("loading was interrupted", interruptedEx);
		} catch (Exception ex) {
			boolean removed = cache.remove(txId, value);
			if (removed) {
				// System.out.printf("%s failed loading activity for key %s removed%n", Thread.currentThread().getName(), txId);
			}
			throw transformException(ex);
		}
	}
	
	public Integer get(String key, boolean check) {
		CacheValue<Integer> value = cache.get(key);
		if (value != null && check && !value.isValid()) {
			cache.remove(key, value);
		}
		
		return get(key);
	}
	
	public void invalidate(String key) {
		System.out.printf("invalidating: %s%n", key);
		CacheValue<Integer> value = cache.get(key);
		if (value != null) {
			boolean invalidated = cache.replace(key, value, new CacheValue<>(false, value.getLoader()));
			if (invalidated) {
				System.out.printf("%s invalidated%n", key);
				AtomicInteger count = detector.get(key);
				if (count != null) {
					count.decrementAndGet();
				}
			}
		}
	}
	
	private RuntimeException transformException(Exception ex) {
		if (ex instanceof ExecutionException) {
			return new RuntimeException(((ExecutionException)ex).getCause());
		} else if (ex instanceof CancellationException) {
			return new RuntimeException("loading cancelled");
		} else {
			throw new RuntimeException(ex);
		}
	}

	private Integer getObject(String txId) {
		Integer result = Integer.parseInt(txId);
		imitateFailure(result);
		
		AtomicInteger count = detector.putIfAbsent(txId, new AtomicInteger());
		if (count != null) {
			count.incrementAndGet();
		}
		try {
			Thread.sleep(2);
		} catch (InterruptedException interuptedEx) {
			Thread.currentThread().interrupt();
		}
		
		return result;
	}
	
	private void imitateFailure(int val) {
		if (System.currentTimeMillis() % 2 == 0) {
			throw new IllegalArgumentException("loading failed");
		}
	}

	public void detect() {
		// checking that each key was loaded only onces
		boolean concurrentLoadingDetected = false;
		for (Map.Entry<String, AtomicInteger> count : detector.entrySet()) {
			if (count.getValue().get() > 1) {
				System.out.printf("tx object %s returned %d times%n", count.getKey(), count.getValue().get());
				concurrentLoadingDetected = true;
			}
		}
		
		if (concurrentLoadingDetected) {
			throw new IllegalStateException("concurrent loading detected");
		}
		
		// check that cache contains only successfully loaded entries
		cache.forEach((k, v) -> {
			try {
				v.getLoader().get();
			} catch (Exception ex) {
				throw new IllegalStateException("cache contains failed activities");
			}
		});
	}
}