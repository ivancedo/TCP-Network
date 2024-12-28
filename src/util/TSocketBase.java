package src.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Base class representing a socket abstraction.
 */
public class TSocketBase {

    /** Simulated network interface */
    public SimNet network;

    /** Lock for synchronization */
    protected Lock lock;

    /** Condition variable for application waiting */
    protected Condition appCV;

    /** Local port number */
    public int localPort;

    /** Remote port number */
    public int remotePort;

    /** Timer service for retransmission timeout */
    protected Timer timerService;

    /** Timer task for retransmission */
    protected TimerTask sndRtTimer;

    /** Logger instance */
    protected Log log;

    /**
     * Constructor with network interface.
     *
     * @param network the simulated network interface
     */
    protected TSocketBase(SimNet network) {
        this.network = network;
        lock = new ReentrantLock();
        appCV = lock.newCondition();
        timerService = new Timer();
        log = Log.getLog();
    }

    /**
     * Retrieves the local port number.
     *
     * @return the local port number
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Sets the local port number.
     *
     * @param localPort the local port number to set
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * Retrieves the remote port number.
     *
     * @return the remote port number
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Sets the remote port number.
     *
     * @param remotePort the remote port number to set
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * Placeholder method for listening for incoming connections.
     *
     * @throws RuntimeException indicating method not supported
     */
    public void listen() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for accepting incoming connections.
     *
     * @return the accepted socket
     * @throws RuntimeException indicating method not supported
     */
    public TSocketBase accept() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for establishing a connection.
     *
     * @throws RuntimeException indicating method not supported
     */
    public void connect() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for closing the socket.
     *
     * @throws RuntimeException indicating method not supported
     */
    public void close() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for sending data.
     *
     * @param data   the data to send
     * @param offset the offset in the data array
     * @param length the length of data to send
     * @throws RuntimeException indicating method not supported
     */
    public void sendData(byte[] data, int offset, int length) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for receiving data.
     *
     * @param data   the buffer to store received data
     * @param offset the offset in the buffer
     * @param length the maximum number of bytes to receive
     * @return the actual number of bytes received
     * @throws RuntimeException indicating method not supported
     */
    public int receiveData(byte[] data, int offset, int length) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for processing a received TCP segment.
     *
     * @param rseg the received TCP segment
     * @throws RuntimeException indicating method not supported
     */
    public void processReceivedSegment(TCPSegment rseg) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Placeholder method for handling retransmission timeout.
     *
     * @throws RuntimeException indicating method not supported
     */
    protected void timeout() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Starts the retransmission timer.
     */
    protected void startRTO() {
        if (sndRtTimer != null) {
            sndRtTimer.cancel();
        }
        sndRtTimer = new TimerTask() {
            public void run() {
                timeout();
            }
        };
        timerService.schedule(sndRtTimer, Const.SND_RTO);
    }

    /**
     * Stops the retransmission timer.
     */
    protected void stopRTO() {
        if (sndRtTimer != null) {
            sndRtTimer.cancel();
        }
        sndRtTimer = null;
    }

    /**
     * Prints the received TCP segment for debugging.
     *
     * @param rseg the received TCP segment
     */
    protected void printRcvSeg(TCPSegment rseg) {
        if (rseg.isPsh()) {
            log.printBlue("\t\t\t\t\t\t\t\treceived: " + rseg);
        }
        if (rseg.isAck()) {
            log.printBlue("  received: " + rseg);
        }
    }

    /**
     * Prints the sent TCP segment for debugging.
     *
     * @param rseg the sent TCP segment
     */
    protected void printSndSeg(TCPSegment rseg) {
        if (rseg.isPsh()) {
            log.printPurple("  sent: " + rseg);
        }
        if (rseg.isAck()) {
            log.printPurple("\t\t\t\t\t\t\t\tsent: " + rseg);
        }
    }
}