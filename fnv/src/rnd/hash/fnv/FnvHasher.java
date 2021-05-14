package rnd.hash.fnv;

public class FnvHasher {
  private static final long MULTIPLIER = 0x100000001b3L;

  private long hash;

  public FnvHasher() {
    this(-3750763034362895579l);
  }

  public FnvHasher(long hash) {
    this.hash = hash;

    System.out.printf("multiplier: %x%n", MULTIPLIER);
  }

  public long finish() {
    return hash;
  }

  public void write(byte[] bytes) {
    for (byte b : bytes) {
      hash = hash ^ (long)b;
      hash *= MULTIPLIER;
    }
  }
}
