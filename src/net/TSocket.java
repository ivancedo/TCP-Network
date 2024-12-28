package src.net;

import src.util.CircularQueue;
import src.util.Const;
import src.util.TCPSegment;
import src.util.TSocketBase;

/**
 * Represents a client or server socket extending the base socket class.
 */
public class TSocket extends TSocketBase {

    /** Protocol instance */
    protected Protocol proto;

    /** FSM state */
    protected int state;

    /** Indicates if the socket is a client */
    protected boolean client;

    /** Queue for incoming connections */
    protected CircularQueue<TSocket> acceptQueue;

    /** FSM states */
    protected static final int CLOSED = 0,
            LISTEN = 1,
            SYN_SENT = 2,
            ESTABLISHED = 3,
            FIN_WAIT = 4,
            CLOSE_WAIT = 5;

    /**
     * Constructor initializing with protocol, local port, and remote port.
     *
     * @param p          the protocol instance
     * @param localPort  the local port number
     * @param remotePort the remote port number
     */
    protected TSocket(Protocol p, int localPort, int remotePort) {
        super(p.getNetwork());
        proto = p;
        this.localPort = localPort;
        this.remotePort = remotePort;
        state = CLOSED;
        p.addActiveTSocket(this);
    }

    /**
     * Initiates a connection to a remote socket.
     */
    @Override
    public void connect() {
        lock.lock();
        try {
            client = true;
            proto.addActiveTSocket(this);
            state = SYN_SENT;
            sendSyn(false);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Closes the socket connection.
     */
    @Override
    public void close() {
        lock.lock();
        try {
            switch (state) {
                case ESTABLISHED:
                    state = FIN_WAIT;
                    sendFin(false);
                    break;
                case CLOSE_WAIT:
                    sendFin(false);
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Processes a received TCP segment.
     *
     * @param rseg the received TCP segment
     */
    @Override
    public void processReceivedSegment(TCPSegment rseg) {
        lock.lock();
        try {
            printRcvSeg(rseg);
            switch (state) {
                case SYN_SENT:
                    if (rseg.isSyn()) {
                        state = ESTABLISHED;
                        appCV.signal();
                    }
                    break;
                case ESTABLISHED:
                    if (rseg.isFin()) {
                        state = CLOSE_WAIT;
                    }
                    break;
                case FIN_WAIT:
                    if (rseg.isFin()) {
                        state = CLOSED;
                    }
                    break;
                case CLOSE_WAIT:
                    if (rseg.isPsh()) {
                        // Handle data segment
                    }
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sends a SYN segment.
     *
     * @param ret whether the segment is a retransmission
     */
    protected void sendSyn(boolean ret) {
        TCPSegment syn = new TCPSegment();
        syn.setSyn(true);
        syn.setSourcePort(localPort);
        syn.setDestinationPort(remotePort);
        network.send(syn);
        if (ret) {
            printRetSeg(syn);
        } else {
            printSndSeg(syn);
        }
    }

    /**
     * Sends a FIN segment.
     *
     * @param ret whether the segment is a retransmission
     */
    protected void sendFin(boolean ret) {
        TCPSegment fin = new TCPSegment();
        fin.setFin(true);
        fin.setDestinationPort(remotePort);
        fin.setSourcePort(localPort);
        network.send(fin);
        if (ret) {
            printRetSeg(fin);
        } else {
            printSndSeg(fin);
        }
    }

    /**
     * Prints the received TCP segment for debugging.
     *
     * @param rseg the received TCP segment
     */
    protected void printRcvSeg(TCPSegment rseg) {
        if (client) {
            log.printWhite("    rcvd: " + rseg);
        } else {
            log.printWhite("\t\t\t\t\t\t\t    rcvd: " + rseg);
        }
    }

    /**
     * Prints the sent TCP segment for debugging.
     *
     * @param rseg the sent TCP segment
     */
    protected void printSndSeg(TCPSegment rseg) {
        if (client) {
            log.printWhite("    sent: " + rseg);
        } else {
            log.printWhite("\t\t\t\t\t\t\t    sent: " + rseg);
        }
    }

    /**
     * Prints the retransmitted TCP segment for debugging.
     *
     * @param rseg the retransmitted TCP segment
     */
    protected void printRetSeg(TCPSegment rseg) {
        if (rseg.getSourcePort() < 50) {
            log.printGreen("    sent: " + rseg);
        } else {
            log.printGreen("\t\t\t\t\t\t\t    sent: " + rseg);
        }
    }
}
