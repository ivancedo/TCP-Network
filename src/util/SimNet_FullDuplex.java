package src.util;

/**
 * Simulated full-duplex network interface for TCP communication.
 */
public class SimNet_FullDuplex {

    protected SimNet_Loss instance_left, instance_right;
    protected Peer left, right;

    /**
     * Constructs a full-duplex simulated network with specified loss rates.
     *
     * @param lossPsh Loss rate for PUSH segments.
     * @param lossAck Loss rate for ACK segments.
     */
    public SimNet_FullDuplex(double lossPsh, double lossAck) {
        instance_left  = new SimNet_Loss(lossAck);
        instance_right = new SimNet_Loss(lossPsh);
        left  = new Peer();
        right = new Peer();
    }

    /**
     * Constructs a full-duplex simulated network with default loss rates (0.0).
     */
    public SimNet_FullDuplex() {
        this(0.0, 0.0);
    }

    /**
     * Retrieves the sending end of the simulated network.
     *
     * @return SimNet instance representing the sending end.
     */
    public SimNet getSndEnd() {
        return left;
    }

    /**
     * Retrieves the receiving end of the simulated network.
     *
     * @return SimNet instance representing the receiving end.
     */
    public SimNet getRcvEnd() {
        return right;
    }

    /**
     * Retrieves the client end of the simulated network.
     *
     * @return SimNet instance representing the client end.
     */
    public SimNet getCltEnd() {
        return left;
    }

    /**
     * Retrieves the server end of the simulated network.
     *
     * @return SimNet instance representing the server end.
     */
    public SimNet getSrvEnd() {
        return right;
    }

    /**
     * Nested class representing a peer in the simulated full-duplex network.
     */
    public class Peer implements SimNet {

        @Override
        public void send(TCPSegment seg) {
            if (this == left) {
                instance_right.send(seg);
            } else {
                instance_left.send(seg);
            }
        }

        @Override
        public TCPSegment receive() {
            if (this == left) {
                return instance_left.receive();
            } else {
                return instance_right.receive();
            }
        }

        @Override
        public int getMTU() {
            return Const.MTU_ETHERNET;
        }
    }
}