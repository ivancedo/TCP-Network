package src.net;

import src.util.ProtocolBase;
import src.util.SimNet;
import src.util.TCPSegment;
import src.util.TSocketBase;

public class Protocol extends ProtocolBase {

    /** Constructor for Protocol */
    protected Protocol(SimNet network) {
        super(network);
    }

    /**
     * Process incoming TCP segment at IP layer.
     *
     * @param seg the TCP segment received
     */
    public void ipInput(TCPSegment seg) {
        TSocketBase socket = getMatchingTSocket(seg.getDestinationPort(), seg.getSourcePort());
        if (socket == null) {
            log.printRed("\t\t\t\t\t\t\tNo matching active TSocket.");
        } else {
            socket.processReceivedSegment(seg);
        }
    }

    /**
     * Retrieves a matching TSocket based on local and remote ports.
     *
     * @param localPort the local port number
     * @param remotePort the remote port number
     * @return the matching TSocket, or null if none found
     */
    protected TSocketBase getMatchingTSocket(int localPort, int remotePort) {
        lock.lock();
        try {
            for (TSocketBase sc : activeSockets) {
                if (sc.localPort == localPort && sc.remotePort == remotePort) {
                    return sc;
                }
            }
            for (TSocketBase sc : listenSockets) {
                if (sc.localPort == localPort) {
                    return sc;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
}