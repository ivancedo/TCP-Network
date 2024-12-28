package src.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simulated network monitor using a circular queue for segment buffering.
 */
public class SimNetMonitor implements SimNet {

    protected CircularQueue<TCPSegment> queue;
    protected ReentrantLock mon;
    protected Condition qEmpty, qFull;

    /**
     * Constructs a simulated network monitor with a circular queue.
     */
    public SimNetMonitor() {
        queue = new CircularQueue<>(Const.SIMNET_QUEUE_SIZE);
        mon = new ReentrantLock();
        qEmpty = mon.newCondition();
        qFull = mon.newCondition();
    }

    /**
     * Sends a TCP segment to the simulated network.
     *
     * @param seg TCPSegment to send.
     */
    @Override
    public void send(TCPSegment seg) {
        mon.lock();
        try {
            while (queue.full()) {
                qFull.await(); // Wait if the queue is full
            }
            queue.put(seg); // Place the segment in the queue
            qEmpty.signalAll(); // Signal that the queue is no longer empty
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mon.unlock();
        }
    }

    /**
     * Receives a TCP segment from the simulated network.
     *
     * @return TCPSegment received.
     */
    @Override
    public TCPSegment receive() {
        mon.lock();
        try {
            while (queue.empty()) {
                qEmpty.await(); // Wait if the queue is empty
            }
            TCPSegment res = queue.get(); // Retrieve the segment from the queue
            qFull.signalAll(); // Signal that the queue is no longer full
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mon.unlock();
        }
    }

    /**
     * Retrieves the Maximum Transmission Unit (MTU) of the simulated network.
     *
     * @return MTU of the network.
     */
    @Override
    public int getMTU() {
        throw new UnsupportedOperationException("Not supported yet. Implementation pending for practice 3...");
    }
}