package src.net;

import src.util.SimNet_FullDuplex;
import src.util.Log;
import src.util.SimNet;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        SimNet_FullDuplex net = new SimNet_FullDuplex();

        new Thread(new HostSrv(net.getSrvEnd())).start();
        new Thread(new HostClt(net.getCltEnd())).start();
    }
}

class HostSrv implements Runnable {

    /** Server port number */
    public static final int PORT = 80;

    /** Protocol instance */
    protected Protocol proto;

    /** Logger instance */
    private Log log;

    public HostSrv(SimNet net) {
        proto = new Protocol(net);
        log = Log.getLog();
    }

    @Override
    public void run() {
        log.printBlue("\t\t\t\t\t\t\t\tServer started");
        TServerSocket serverSocket = new TServerSocket(proto, HostSrv.PORT);
        for (int i = 0; i < 2; i++) {
            TSocket sc = serverSocket.accept();
            new Thread(new Worker(sc)).start();
        }
    }

    class Worker implements Runnable {

        /** Socket instance */
        TSocket sc;

        Worker(TSocket sc) {
            this.sc = sc;
        }

        public void run() {
            log.printBlue("\t\t\t\t\t\t\tWorker providing service to client with port: " + sc.remotePort);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.printBlue("\t\t\t\t\t\t\tWorker about to close to client with port: " + sc.remotePort);
            sc.close();
            log.printBlue("\t\t\t\t\t\t\tWorker closed from client with port: " + sc.remotePort);
        }
    }
}

class HostClt implements Runnable {

    /** First client port */
    public final int PORT1 = 10;

    /** Second client port */
    public final int PORT2 = 20;

    /** Protocol instance */
    protected Protocol proto;

    /** Logger instance */
    private Log log;

    public HostClt(SimNet net) {
        proto = new Protocol(net);
        log = Log.getLog();
    }

    public void run() {
        // Delay to allow server to start:
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread c1 = new Thread(new Client(PORT1));
        Thread c2 = new Thread(new Client(PORT2));
        c1.start();
        c2.start();
        try {
            c1.join();
            c2.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    class Client implements Runnable {

        /** Local port for client */
        private final int localPort;

        public Client(int port) {
            localPort = port;
        }

        public void run() {
            log.printBlue("Client started");

            TSocket sc = new TSocket(proto, localPort, HostSrv.PORT);
            sc.connect();
            log.printBlue("Client connected with localport: " + sc.localPort);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.printBlue("Client about to close from localport: " + sc.localPort);
            sc.close();
            log.printBlue("Client closed from localport: " + sc.localPort);
        }
    }
}