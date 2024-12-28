package src.net;

import src.util.CircularQueue;
import src.util.Const;
import src.util.TCPSegment;
import src.util.TSocketBase;

/**
 * Represents a server socket extending the base socket class.
 */
public class TServerSocket extends TSocketBase {

    /** Protocol instance */
    protected Protocol proto;

    /** FSM states */
    protected int state;

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
     * Constructor initializing with protocol and local port.
     *
     * @param p         the protocol instance
     * @param localPort the local port number
     */
    protected TServerSocket(Protocol p, int localPort) {
        super(p.getNetwork());
        proto = p;
        this.localPort = localPort;
        state = CLOSED;
        p.addListenTSocket(this);
        listen();
    }

    /**
     * Starts listening for incoming connections.
     */
    @Override
    public void listen() {
        lock.lock();
        try {
            if (state == CLOSED) {
                acceptQueue = new CircularQueue<>(Const.LISTEN_QUEUE_SIZE);
                state = LISTEN;
                proto.addListenTSocket(this);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Accepts an incoming connection.
     *
     * @return the accepted socket
     */
    @Override
    public TSocket accept() {
        lock.lock();
        try {
            while (acceptQueue.empty()) {
                appCV.awaitUninterruptibly();
            }
            return acceptQueue.get();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Processes a received TCP segment.
     *
     * @param rseg the received TCP segment
     */
    public void processReceivedSegment(TCPSegment rseg) {
        lock.lock();
        try {
            printRcvSeg(rseg);
            switch (state) {
                case LISTEN:
                    if (rseg.isSyn() && !acceptQueue.full()) {
                        TSocket sc = new TSocket(proto, localPort, rseg.getSourcePort());
                        sc.state = ESTABLISHED;
                        proto.addActiveTSocket(sc);
                        acceptQueue.put(sc);
                        appCV.signal();
                        sc.sendSyn(false);
                    }
                    break;
                case SYN_SENT:
                case ESTABLISHED:
                case FIN_WAIT:
                case CLOSE_WAIT:
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Prints the received TCP segment for debugging.
     *
     * @param rseg the received TCP segment
     */
    protected void printRcvSeg(TCPSegment rseg) {
        log.printWhite("\t\t\t\t\t\t\t\t rcvd: " + rseg);
    }

    /**
     * Prints the sent TCP segment for debugging.
     *
     * @param rseg the sent TCP segment
     */
    protected void printSndSeg(TCPSegment rseg) {
        log.printWhite("\t\t\t\t\t\t\t\t sent: " + rseg);
    }
}