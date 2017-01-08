/***************************************************************
 AIS Comms Configuration using simple File and Buffer Writers
 
 original coding: Anthony C. Amburn
 date: 09/02/2006
 
 Change History"
 
 date                      author               reason
 
 ***********************************************************//


package CGAISCommsConfig;

import CGAISCommsFeeds.NAISCommsMain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import mil.uscg.util.Logging;
import mil.uscg.util.PropertyUtil;

public class NAISClientSocket {

    private String aisIPAddress = null;
    private int aisPort = 0;
    private BufferedReader aisIn = null;
    private Socket NAISCOMMSSocket = null;
    private BlockingQueue<String[]> naisQueue = null;
    private String typeMessage = null;
    private Boolean recData = false;
    BufferedWriter out = null;

    public NAISClientSocket(BlockingQueue<String[]> naisQueue, String aisIPAddress, int aisPort, String ReportName) {
        this.naisQueue = naisQueue;
        this.aisIPAddress = aisIPAddress;
        this.aisPort = aisPort;
        this.typeMessage = ReportName;
        if (PropertyUtil.getProps().containsKey("recordaisdata")) {
            recData = Boolean.parseBoolean(PropertyUtil.getProps().getProperty("recordaisdata"));
        }

        if (recData) {
            try {
                out = new BufferedWriter(new FileWriter("c:\\" + ReportName + ".txt"));
               // out.write("aString");
              //  out.close();
            } catch (IOException e) {
            }

        }

    }

    public NAISClientSocket() {
    }

    public void readAISCommData() {

        while (true) {

            try {
                Logging.severe("******Trying to connect to " + typeMessage + " server, location: " + aisIPAddress + ":" + aisPort);
                NAISCOMMSSocket = new Socket(aisIPAddress, aisPort);
                NAISCOMMSSocket.setSoTimeout(10000);

                Logging.severe("******Connected to " + typeMessage + " Server, Location: " + aisIPAddress + ":" + aisPort);
                aisIn = new BufferedReader(new InputStreamReader(NAISCOMMSSocket.getInputStream()));

                Logging.severe("******Created Input Stream for " + typeMessage + " Server, Location: " + aisIPAddress + ":" + aisPort);

                while (NAISCOMMSSocket != null && NAISCOMMSSocket.isConnected()) {

                    try {

                        String[] aisParsed = parseNAISSentence(aisIn.readLine());

                        if (aisParsed != null) {
                            naisQueue.put(aisParsed);
                        }

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ie) {
                        }

                        Logging.info("Added message for " + typeMessage + " Queue Size Is Now " + naisQueue.size());

                    } catch (SocketTimeoutException ste) {

                        Logging.warn("No input from " + typeMessage);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ie) {
                        }

                    } catch (EOFException eofe) {

                        Logging.severe(eofe.getMessage());
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ie) {

                            Logging.severe(NAISCommsMain.getStackTrace(ie));
                        }
                    } catch (Exception e) {

                        Logging.severe(NAISCommsMain.getStackTrace(e));
                        aisIn.close();
                        NAISCOMMSSocket.close();
                        NAISCOMMSSocket = null;
                    }
                }

            } catch (ConnectException ce) {

                Logging.severe("Connection from " + typeMessage + " " + ce.getLocalizedMessage());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {

                    Logging.severe(NAISCommsMain.getStackTrace(ie));
                }
            } catch (IOException ioe) {

                Logging.severe(ioe.getMessage());
            }
        }
    }

    private String[] parseNAISSentence(String aisSentence) {
        String[] aisArray = null;
        String delimiter = ",";

        if(recData) {
            try {
            out.write(aisSentence + "\r\n");
            out.flush();
            } catch(IOException ioe) {
                Logging.severe(ioe.getMessage());
            }
        }
        Logging.info(aisSentence);

        try {

            aisArray = aisSentence.split(delimiter);
            if (aisArray.length < 26) {
                Logging.severe("Error Parsing Sentence " + aisSentence);
            }
        } catch (NullPointerException ne) {
            return aisArray;
        }


        return aisArray;
    }
}
