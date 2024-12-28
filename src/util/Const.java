package src.util;

/**
 * Network configuration constants for TCP/IP communication simulation.
 * Defines parameters for network characteristics, sender/receiver behavior,
 * and server configuration.
 */
public interface Const {

    /** Random seed for reproducible simulations */
    int SEED = 1;

    /** Maximum size for simulated network queue */
    int SIMNET_QUEUE_SIZE = 100;

    /** Probability of PSH packet loss (0.0 - 1.0) */
    double LOSS_RATE_PSH = 0.2;

    /** Probability of ACK packet loss (0.0 - 1.0) */
    double LOSS_RATE_ACK = 0.2;

    /** Ethernet Maximum Transmission Unit in bytes */
    int MTU_ETHERNET = 1500;

    /** IP header size in bytes */
    int IP_HEADER = 20;

    /** TCP header size in bytes */
    int TCP_HEADER = 20;

    /** Retransmission timeout duration (ms) */
    int SND_RTO = 500;

    /** Number of segments to transmit */
    int SND_NUM = 5;

    /** Total data size to transmit (bytes) */
    int SND_SIZE = 3000;

    /** Delay between segment transmissions (ms) */
    int SND_INTERVAL = 100;

    /** Maximum size of receiver buffer */
    int RCV_QUEUE_SIZE = 50;

    /** Expected data size to receive (bytes) */
    int RCV_SIZE = 2000;

    /** Delay between receive operations (ms) */
    int RCV_INTERVAL = 500;

    /** Maximum pending connections in server queue */
    int LISTEN_QUEUE_SIZE = 10;
}