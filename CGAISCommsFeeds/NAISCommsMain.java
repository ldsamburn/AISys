
/***************************************************************
 Main Java runtime for NAIS Radio Comms
 
 original coding: Anthony C. Amburn
 date: 09/23/2006
 
 Change History"
 
 date                      author               reason
 
 ***********************************************************//

package CGAISCommsFeeds;

import CGAISCommsConfig.NAISClientSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import mil.uscg.fileMonitor.FileMonitor;
import mil.uscg.util.Logging;
import mil.uscg.util.PropertyUtil;

public class NAISCommsMain {

    private final BlockingQueue<String[]> AISqueue = new LinkedBlockingQueue<String[]>();
    private final BlockingQueue<String[]> SafetyTOIqueue = new LinkedBlockingQueue<String[]>();
    private OutputStream osTrkSvrClient = null;
    private Socket trSocket = null;
    private int trPort = 0;
    private String trIP = null;

    public static void main(String[] args) {

        final NAISCommsMain NAISMain = new NAISCommsMain();
        Thread aisReportThread = null;
        Thread bftReportThread = null;
        Thread toiReportThread = null;
        Thread saftyMsgReportThread = null;
        Thread aisbftConsumerThread = null;

        PropertyUtil.loadProps("NAISCommsFeeds.properties");
        FileMonitor monitor = new FileMonitor(10000, "NAISCommsFeeds.properties");

        String logPath = "";
        if (PropertyUtil.getProps().containsKey("logPath")) {
            logPath = PropertyUtil.getProps().getProperty("logPath");
        }

        if (PropertyUtil.getProps().containsKey("TrkSvrIP")) {
            NAISMain.trIP = PropertyUtil.getProps().getProperty("TrkSvrIP");
        }

        if (PropertyUtil.getProps().containsKey("TrkSvrPort")) {
            NAISMain.trPort = Integer.parseInt(PropertyUtil.getProps().getProperty("TrkSvrPort"));
        }


        if (PropertyUtil.getProps().containsKey("logName")) {
            Logging.initLogger(logPath + PropertyUtil.getProps().getProperty("logName"));
        }

        while (true) {

            if (NAISMain.trSocket == null) {
                NAISMain.connectToTrackingServer();
            }

            if ((aisReportThread == null) || (aisReportThread.getState() == Thread.State.TERMINATED)) {
                aisReportThread = new Thread(new Runnable() {

                    NAISClientSocket aisClient = new NAISClientSocket(NAISMain.AISqueue,
                            PropertyUtil.getProps().getProperty("NAISCommsURL"),
                            Integer.parseInt(PropertyUtil.getProps().getProperty("NAISCommsALLAISreports")),
                            "AIS All Message");

                    public void run() {
                        aisClient.readAISCommData();

                    }
                });

                aisReportThread.setName("NAISCommsALLAISreports");
                aisReportThread.start();
                aisReportThread.setPriority(Thread.NORM_PRIORITY);
                System.out.println("Thread For All AIS Messages started..");
                Logging.info("|Thread For AIS Messages started..");
            }

            if ((bftReportThread == null) || (bftReportThread.getState() == Thread.State.TERMINATED)) {
                bftReportThread = new Thread(new Runnable() {

                    NAISClientSocket aisClient = new NAISClientSocket(NAISMain.AISqueue,
                            PropertyUtil.getProps().getProperty("NAISCommsURL"),
                            Integer.parseInt(PropertyUtil.getProps().getProperty("NAISCommsBFTPort")),
                            "BFT Message");

                    public void run() {
                        aisClient.readAISCommData();

                    }
                });

                bftReportThread.setName("NAISCommsBFTPort");
                bftReportThread.start();
                bftReportThread.setPriority(Thread.NORM_PRIORITY);
                System.out.println("Thread for BFT Messages started..");
                Logging.info("|Thread For BFT Messages started..");
            }

            if ((toiReportThread == null) || (toiReportThread.getState() == Thread.State.TERMINATED)) {
                toiReportThread = new Thread(new Runnable() {

                    NAISClientSocket aisClient = new NAISClientSocket(NAISMain.SafetyTOIqueue,
                            PropertyUtil.getProps().getProperty("NAISCommsURL"),
                            Integer.parseInt(PropertyUtil.getProps().getProperty("NAISCommsTOIPort")),
                            "TOI Message");

                    public void run() {
                        aisClient.readAISCommData();

                    }
                });

                toiReportThread.setName("NAISCommsTOLPort");
                toiReportThread.start();
                toiReportThread.setPriority(Thread.MIN_PRIORITY);
                System.out.println("Thread For TOL Messages started..");
                Logging.info("|Thread For TOL Messages started..");
            }

            if ((saftyMsgReportThread == null) || (saftyMsgReportThread.getState() == Thread.State.TERMINATED)) {
                saftyMsgReportThread = new Thread(new Runnable() {

                    NAISClientSocket aisClient = new NAISClientSocket(NAISMain.SafetyTOIqueue,
                            PropertyUtil.getProps().getProperty("NAISCommsURL"),
                            Integer.parseInt(PropertyUtil.getProps().getProperty("NAISCommsSaftyMsg")),
                            "Safty Message");

                    public void run() {
                        aisClient.readAISCommData();

                    }
                });

                saftyMsgReportThread.setName("NAISCommsSaftyMsg");
                saftyMsgReportThread.start();
                saftyMsgReportThread.setPriority(Thread.MIN_PRIORITY);
                System.out.println("Thread For Safty Messages started..");
                Logging.info("|Thread For Safty Messages started..");
            }

            if ((aisbftConsumerThread == null) || (aisbftConsumerThread.getState() == Thread.State.TERMINATED)) {
                aisbftConsumerThread = new Thread(new Runnable() {

                    NAISCommsMessageConsumers aisConsumer = new NAISCommsMessageConsumers(NAISMain,
                            Integer.parseInt(PropertyUtil.getProps().getProperty("NAISConsumers")));

                    public void run() {
                        aisConsumer.consumeAISMessages();

                    }
                });

                aisbftConsumerThread.setName("NAISConsumer");
                aisbftConsumerThread.start();
                aisbftConsumerThread.setPriority(Thread.MAX_PRIORITY);
                System.out.println("Thread For Consumer started..");
                Logging.info("|Thread For Safty Consumer started..");
            }

            try {
                Thread.sleep(Integer.parseInt(PropertyUtil.getProps().getProperty("NAISreportTime")));
                Logging.warn("Total Messages In Queue: " + NAISMain.AISqueue.size());
            } catch (InterruptedException iex) {
                Logging.severe("EXCEPTION: " + iex.getMessage());
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public OutputStream getosClient() {

        return osTrkSvrClient;
    }

    public void reSetosClient() {
        trSocket = null;
        osTrkSvrClient = null;
    }

    public BlockingQueue<String[]> getAISqueue() {
        return AISqueue;
    }

    protected void connectToTrackingServer() {

        try {
            trSocket = new Socket(trIP, trPort);
            osTrkSvrClient = trSocket.getOutputStream();
        } catch (Exception e) {
            Logging.severe("Cannot Connect to Tracking Server");
            osTrkSvrClient = null;
        }
    }

    public void sendToTackingserver(String xmlData) {
    }
}

