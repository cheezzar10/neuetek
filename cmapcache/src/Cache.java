import java.util.Map;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import java.util.concurrent.atomic.AtomicInteger;

// it's not a cache but TX specific objects storage
public class Cache {
	private final ConcurrentMap<String, FutureTask<Integer>> cache = new ConcurrentHashMap<>();
	// miltuple TX service requests detector
	private final ConcurrentMap<String, AtomicInteger> detector = new ConcurrentHashMap<>();

	public Integer get(final String txId) {
		FutureTask<Integer> loader = cache.get(txId);
		if (loader == null) {
			loader = new FutureTask<>(() -> getObject(txId));
			FutureTask<Integer> concurrentLoader = cache.putIfAbsent(txId, loader);
			if (concurrentLoader == null) {
				loader.run();
			} else {
				loader = concurrentLoader;
			}
		}
		
		try {
			return loader.get();
		} catch (InterruptedException interruptedEx) {
			throw new IllegalStateException("loading was interrupted", interruptedEx);
		} catch (Exception ex) {
			boolean removed = cache.remove(txId, loader);
			if (removed) {
				// System.out.printf("%s failed loading activity for key %s removed%n", Thread.currentThread().getName(), txId);
			}
			throw transformException(ex);
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
				Integer i = v.get();
			} catch (Exception ex) {
				throw new IllegalStateException("cache contains failed activities");
			}
		});
	}
}