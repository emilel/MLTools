package Benchmark;

/**
 * A class that measures the time a Runnable takes to run.
 */
public class Benchmark {
    private final Runnable runnable;

    /**
     * Constructor, which takes the Runnable that is to be measured.
     * @param runnable
     */
    public Benchmark(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Runs the Runnable, and outputs the time it took for it to execute.
     */
    public void runBenchmark() {
        System.out.println("Starting benchmark...");
        long t0 = System.currentTimeMillis();
        runnable.run();
        long t1 = System.currentTimeMillis();
        long t = t1 - t0;
        System.out.println("Benchmark finished after " + t + " ms.");
    }
}
