package src.util;

/**
 * Class representing a sender thread for data transmission.
 */
public class Sender extends Thread {

    protected TSocketBase output;
    protected int sendNum, sendSize, sendInterval;
    public static int numBytes, numSenders;
    private Log log;

    /**
     * Constructs a sender with specified socket, number of segments to send, segment size, and send interval.
     *
     * @param sc           TSocketBase instance for sending data.
     * @param sendNum      Number of segments to send.
     * @param sendSize     Size of each segment to send.
     * @param sendInterval Interval between sending segments.
     */
    public Sender(TSocketBase sc, int sendNum, int sendSize, int sendInterval) {
        this.output = sc;
        this.sendNum = sendNum;
        this.sendSize = sendSize;
        this.sendInterval = sendInterval;
        numBytes = sendNum * sendSize;
        numSenders++;
        log = Log.getLog();
    }

    /**
     * Constructs a sender with specified socket, using default parameters for number of segments, segment size, and send interval.
     *
     * @param sc TSocketBase instance for sending data.
     */
    public Sender(TSocketBase sc) {
        this(sc, Const.SND_NUM, Const.SND_SIZE, Const.SND_INTERVAL);
    }

    /**
     * Runs the sender thread, repeatedly sending segments until the specified number is sent.
     */
    @Override
    public void run() {
        try {
            byte stamp = 0;
            byte[] buf = new byte[sendSize];
            for (int i = 0; i < sendNum; i++) {
                Thread.sleep(sendInterval);
                for (int j = 0; j < sendSize; j++) {
                    buf[j] = stamp;
                    stamp = (byte) (stamp + 1);
                }
                output.sendData(buf, 0, buf.length);
            }
            log.printGreen("Sender: transmission finished");
            numSenders--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}