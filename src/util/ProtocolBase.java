package src.util;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract base class for network protocol implementation.
 * Provides thread-safe socket management and network communication handling.
 */
public abstract class ProtocolBase {

    /** Network simulation instance */
    protected final SimNet network;

    /** Lock for thread-safe socket operations */
    protected final Lock lock;

    /** List of sockets in listening state */
    protected final ArrayList<TSocketBase> listenSockets;

    /** List of established connection sockets */
    protected final ArrayList<TSocketBase> activeSockets;

    /** Logger instance */
    protected final Log log;

    /**
     * Initializes the protocol with network simulation and starts receiver thread.
     *
     * @param net Network simulation instance
     * @throws NullPointerException if net is null
     */
    protected ProtocolBase(SimNet net) {
        if (net == null) {
            throw new NullPointerException("Network instance cannot be null");
        }
        this.network = net;
        this.lock = new ReentrantLock();
        this.listenSockets = new ArrayList<>();
        this.activeSockets = new ArrayList<>();
        this.log = Log.getLog();
        new Thread(new ReceiverTask()).start();
    }

    /**
     * Processes incoming IP segments according to protocol specifications.
     *
     * @param segment TCP segment received from network layer
     */
    protected abstract void ipInput(TCPSegment segment);

    /**
     * Returns the associated network simulation instance.
     *
     * @return SimNet instance used by this protocol
     */
    public SimNet getNetwork() {
        return network;
    }

    /**
     * Adds a socket to the listening sockets list.
     *
     * @param socket Socket to add to listening list
     * @throws NullPointerException if socket is null
     */
    public void addListenTSocket(TSocketBase socket) {
        if (socket == null) {
            throw new NullPointerException("Socket cannot be null");
        }
        lock.lock();
        try {
            listenSockets.add(socket);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a socket to the active connections list.
     *
     * @param socket Socket to add to active list
     * @throws NullPointerException if socket is null
     */
    public void addActiveTSocket(TSocketBase socket) {
        if (socket == null) {
            throw new NullPointerException("Socket cannot be null");
        }
        lock.lock();
        try {
            activeSockets.add(socket);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a socket from the listening sockets list.
     *
     * @param socket Socket to remove from listening list
     */
    public void removeListenTSocket(TSocketBase socket) {
        lock.lock();
        try {
            listenSockets.remove(socket);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a socket from the active connections list.
     *
     * @param socket Socket to remove from active list
     */
    public void removeActiveTSocket(TSocketBase socket) {
        lock.lock();
        try {
            activeSockets.remove(socket);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Background task that continuously receives network segments.
     * Runs in a separate thread to handle incoming network traffic.
     */
    private class ReceiverTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                TCPSegment segment = network.receive();
                ipInput(segment);
            }
        }
    }
}