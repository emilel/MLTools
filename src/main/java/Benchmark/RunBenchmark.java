package Benchmark;

/**
 * Creates a ReadAndInvert instance with a sample Matrix, sends that instance to a Benchmark that runs it and outputs
 * the time it takes to invert the Matrix.
 */
class RunBenchmark {

    /**
     * Runs the program.
     * @param args
     */
    public static void main(String[] args) {
        Runnable runnable = new ReadAndInvert("src/main/resources/m1.txt", 5000, 1000);
        Benchmark benchmark = new Benchmark(runnable);
        benchmark.runBenchmark();
    }
}
