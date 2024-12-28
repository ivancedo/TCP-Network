package src.util;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe utility class for colored console logging.
 * Implements the singleton pattern to ensure a single logging instance.
 * Uses ANSI escape codes for text coloring and formatting.
 */
public class Log {
    /** Reset all text formatting */
    public static final String RESET = "\033[0m";

    /** Standard color codes for console text */
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    /** Lock for thread-safe logging */
    private final ReentrantLock lock;

    /** Singleton instance */
    private static volatile Log instance;

    /** Application start time for timestamping */
    private static long startTime;

    /**
     * Private constructor enforces singleton pattern.
     * Initializes the lock and start time.
     */
    private Log() {
        this.lock = new ReentrantLock();
        startTime = System.currentTimeMillis();
    }

    /**
     * Returns the singleton instance of the logger.
     * Uses double-checked locking for thread safety.
     *
     * @return the singleton Log instance
     */
    public static Log getLog() {
        if (instance == null) {
            synchronized (Log.class) {
                if (instance == null) {
                    instance = new Log();
                }
            }
        }
        return instance;
    }

    /**
     * Prints a message in red color.
     *
     * @param message the message to print
     */
    public void printRed(String message) {
        out(RED + message + RESET);
    }

    /**
     * Prints a message in black color.
     *
     * @param message the message to print
     */
    public void printBlack(String message) {
        out(BLACK + message + RESET);
    }

    /**
     * Prints a message in blue color.
     *
     * @param message the message to print
     */
    public void printBlue(String message) {
        out(BLUE + message + RESET);
    }

    /**
     * Prints a message in purple color.
     *
     * @param message the message to print
     */
    public void printPurple(String message) {
        out(PURPLE + message + RESET);
    }

    /**
     * Prints a message in green color.
     *
     * @param message the message to print
     */
    public void printGreen(String message) {
        out(GREEN + message + RESET);
    }

    /**
     * Prints a message in white color.
     *
     * @param message the message to print
     */
    public void printWhite(String message) {
        out(WHITE + message + RESET);
    }

    /**
     * Thread-safe implementation of console output.
     * Adds a small delay to prevent timestamp collisions.
     *
     * @param message the formatted message to print
     */
    private void out(String message) {
        lock.lock();
        try {
            Thread.sleep(1);
            System.out.println(message);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.err.println("Logging interrupted: " + ex.getMessage());
        } finally {
            lock.unlock();
        }
    }
}