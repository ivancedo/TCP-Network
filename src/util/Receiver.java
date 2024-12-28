package src.util;

/**
 * Class representing a receiver thread for data reception.
 */
public class Receiver extends Thread {

    protected TSocketBase input;
    protected int recvBuf, recvInterval;
    public static int numReceivers;
    private Log log;

    /**
     * Constructs a receiver with specified socket, receive buffer size, and receive interval.
     *
     * @param sc          TSocketBase instance for receiving data.
     * @param recvBuf     Size of the receive buffer.
     * @param recvInterval Interval between receiving data.
     */
    public Receiver(TSocketBase sc, int recvBuf, int recvInterval) {
        this.input = sc;
        this.recvBuf = recvBuf;
        this.recvInterval = recvInterval;
        numReceivers++;
        log = Log.getLog();
    }

    /**
     * Constructs a receiver with specified socket, using default receive buffer size and interval.
     *
     * @param sc TSocketBase instance for receiving data.
     */
    public Receiver(TSocketBase sc) {
        this(sc, Const.RCV_SIZE, Const.RCV_INTERVAL);
    }

    /**
     * Runs the receiver thread, continuously receiving data until all expected data is received.
     */
    @Override
    public void run() {
        try {
            byte expected = 0;
            int total = 0;
            byte[] buf = new byte[recvBuf];
            Thread.sleep(200);
            while (total < Sender.numBytes) {
                int received = input.receiveData(buf, 0, buf.length);
                total += received;
                for (int j = 0; j < received; j++) {
                    if (buf[j] != expected) {
                        log.printRed("Receiver: RECEIVED DATA IS CORRUPTED!!!");
                        System.exit(0);
                    }
                    expected++;
                }
                log.printBlue("Receiver: received " + received + " bytes");
                Thread.sleep(recvInterval);
            }
            log.printGreen("Receiver: reception finished");
            numReceivers--;
            if (Sender.numSenders == 0 && numReceivers == 0) {
                Thread.sleep(1000);
                System.exit(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}