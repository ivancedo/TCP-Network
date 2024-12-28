package src.util;

/**
 * Represents a TCP segment.
 */
public class TCPSegment {

    private boolean syn, psh, ack, fin;
    private int sourcePort, destinationPort;
    private int seqNum, ackNum, sackNum = -1; // sack = selective-ack
    private int wnd;
    private byte[] data;

    /** Controls whether to display data in toString method. */
    public static boolean SHOW_DATA = false;

    /**
     * Sets the SYN flag of the TCP segment.
     *
     * @param syn true if SYN flag is set, false otherwise
     */
    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    /**
     * Checks if the SYN flag is set in the TCP segment.
     *
     * @return true if SYN flag is set, false otherwise
     */
    public boolean isSyn() {
        return syn;
    }

    /**
     * Sets the PSH flag of the TCP segment.
     *
     * @param psh true if PSH flag is set, false otherwise
     */
    public void setPsh(boolean psh) {
        this.psh = psh;
    }

    /**
     * Checks if the PSH flag is set in the TCP segment.
     *
     * @return true if PSH flag is set, false otherwise
     */
    public boolean isPsh() {
        return psh;
    }

    /**
     * Sets the ACK flag of the TCP segment.
     *
     * @param ack true if ACK flag is set, false otherwise
     */
    public void setAck(boolean ack) {
        this.ack = ack;
    }

    /**
     * Checks if the ACK flag is set in the TCP segment.
     *
     * @return true if ACK flag is set, false otherwise
     */
    public boolean isAck() {
        return ack;
    }

    /**
     * Sets the FIN flag of the TCP segment.
     *
     * @param fin true if FIN flag is set, false otherwise
     */
    public void setFin(boolean fin) {
        this.fin = fin;
    }

    /**
     * Checks if the FIN flag is set in the TCP segment.
     *
     * @return true if FIN flag is set, false otherwise
     */
    public boolean isFin() {
        return fin;
    }

    /**
     * Sets the source port of the TCP segment.
     *
     * @param sourcePort the source port number
     */
    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * Retrieves the source port of the TCP segment.
     *
     * @return the source port number
     */
    public int getSourcePort() {
        return sourcePort;
    }

    /**
     * Sets the destination port of the TCP segment.
     *
     * @param destinationPort the destination port number
     */
    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    /**
     * Retrieves the destination port of the TCP segment.
     *
     * @return the destination port number
     */
    public int getDestinationPort() {
        return destinationPort;
    }

    /**
     * Sets the sequence number of the TCP segment.
     *
     * @param seqNum the sequence number
     */
    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    /**
     * Retrieves the sequence number of the TCP segment.
     *
     * @return the sequence number
     */
    public int getSeqNum() {
        return seqNum;
    }

    /**
     * Sets the acknowledgment number of the TCP segment.
     *
     * @param ackNum the acknowledgment number
     */
    public void setAckNum(int ackNum) {
        this.ackNum = ackNum;
    }

    /**
     * Retrieves the acknowledgment number of the TCP segment.
     *
     * @return the acknowledgment number
     */
    public int getAckNum() {
        return ackNum;
    }

    /**
     * Sets the selective acknowledgment number (SACK) of the TCP segment.
     *
     * @param sackNum the SACK number
     */
    public void setSackNum(int sackNum) {
        this.sackNum = sackNum;
    }

    /**
     * Retrieves the selective acknowledgment number (SACK) of the TCP segment.
     *
     * @return the SACK number
     */
    public int getSackNum() {
        return sackNum;
    }

    /**
     * Sets the window size of the TCP segment.
     *
     * @param wnd the window size
     */
    public void setWnd(int wnd) {
        this.wnd = wnd;
    }

    /**
     * Retrieves the window size of the TCP segment.
     *
     * @return the window size
     */
    public int getWnd() {
        return wnd;
    }

    /**
     * Sets the data payload of the TCP segment.
     *
     * @param d byte array containing the data payload
     */
    public void setData(byte[] d) {
        data = new byte[d.length];
        System.arraycopy(d, 0, data, 0, d.length);
    }

    /**
     * Sets the data payload of the TCP segment from a specified offset and length.
     *
     * @param d      byte array containing the data payload
     * @param offset the starting offset in the byte array
     * @param len    the length of data to copy
     */
    public void setData(byte[] d, int offset, int len) {
        data = new byte[len];
        System.arraycopy(d, offset, data, 0, len);
    }

    /**
     * Retrieves the data payload of the TCP segment.
     *
     * @return byte array containing the data payload
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Retrieves the length of the data payload.
     *
     * @return length of the data payload
     */
    public int getDataLength() {
        return (data == null) ? 0 : data.length;
    }

    /**
     * Generates a string representation of the TCP segment.
     *
     * @return string representation of the TCP segment
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        if (syn) {
            str.append("SYN")
                    .append(", src = ").append(sourcePort)
                    .append(", dst = ").append(destinationPort)
                    .append(", seqNum = ").append(seqNum);
        } else if (fin) {
            str.append("FIN")
                    .append(", src = ").append(sourcePort)
                    .append(", dst = ").append(destinationPort)
                    .append(", seqNum = ").append(seqNum);
        } else if (psh) {
            str.append("PSH")
                    .append(", src = ").append(sourcePort)
                    .append(", dst = ").append(destinationPort)
                    .append(", seqNum = ").append(seqNum);
            if (data != null && SHOW_DATA) {
                str.append(", data = {");
                for (int i = 0; i < data.length - 1; i++) {
                    str.append(data[i]).append(",");
                }
                str.append(data[data.length - 1]).append("}");
            } else if (data != null) {
                str.append(", payload = ").append(data.length);
            }
        } else if (ack) {
            str.append("ACK")
                    .append(", src = ").append(sourcePort)
                    .append(", dst = ").append(destinationPort)
                    .append(", ackNum = ").append(ackNum)
                    .append(", wnd = ").append(wnd);
            if (sackNum != -1) {
                str.append(", sackNum = ").append(sackNum);
            }
        }
        str.append("]");
        return str.toString();
    }
}