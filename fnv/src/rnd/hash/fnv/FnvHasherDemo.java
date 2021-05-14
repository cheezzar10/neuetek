package rnd.hash.fnv;

public class FnvHasherDemo {
  public static void main(String[] args) {
    System.out.println("caclulating fnv hsh");

    int max = Integer.MAX_VALUE;

    System.out.printf("int max: %d%n", max);

    max += 1;

    System.out.printf("wrapped int max: %d%n", max);

    FnvHasher hasher = new FnvHasher();

    hasher.write("foo".getBytes());
    long hash = hasher.finish();

    System.out.printf("computed fnv hash: %x%n", hash);
  }
}
