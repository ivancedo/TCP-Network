package src.util;

/**
 * Interface representing a simulated network for TCP communication.
 */
public interface SimNet {

    /**
     * Sends a TCP segment over the simulated network.
     *
     * @param seg TCPSegment to be sent over the network.
     */
    void send(TCPSegment seg);

    /**
     * Receives a TCP segment from the simulated network.
     *
     * @return TCPSegment received from the network.
     */
    TCPSegment receive();

    /**
     * Retrieves the Maximum Transmission Unit (MTU) of the simulated network.
     * MTU refers to the maximum packet size that the link layer can support.
     *
     * @return Maximum Transmission Unit (MTU) of the simulated network.
     */
    int getMTU();
}