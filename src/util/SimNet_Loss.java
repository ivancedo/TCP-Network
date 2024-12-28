package src.util;

import java.util.Random;

/**
 * Simulated network with lossy behavior extending SimNet_Monitor.
 */
public class SimNet_Loss extends SimNetMonitor {

    private double lossRate;
    private Random rand;
    private Log log;

    /**
     * Constructs a lossy simulated network with specified loss rate.
     *
     * @param lossRate The rate of packet loss (0.0 to 1.0).
     */
    public SimNet_Loss(double lossRate) {
        this.lossRate = lossRate;
        rand = new Random(1L); // Fixed seed for reproducibility
        log = Log.getLog();
    }

    /**
     * Overrides send method to simulate packet loss.
     *
     * @param seg TCPSegment to send.
     */
    @Override
    public void send(TCPSegment seg) {
        if (rand.nextDouble() < lossRate) {
            log.printRed("\t\t +++++++++ SEGMENT LOST: " + seg + " +++++++++\n");
        } else {
            super.send(seg);
        }
    }

    /**
     * Retrieves the MTU (Maximum Transmission Unit) of the network.
     *
     * @return MTU of the network.
     */
    @Override
    public int getMTU() {
        return Const.MTU_ETHERNET;
    }
}