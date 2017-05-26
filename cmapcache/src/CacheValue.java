import java.util.concurrent.FutureTask;

public class CacheValue<T> {
	private final boolean valid;
	private final FutureTask<T> loader;
	
	public CacheValue(FutureTask<T> loader) {
		this(true, loader);
	}
	
	public CacheValue(boolean valid, FutureTask<T> loader) {
		this.valid = valid;
		this.loader = loader;
	}

	public boolean isValid() {
		return valid;
	}

	public FutureTask<T> getLoader() {
		return loader;
	}
}
