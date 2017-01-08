
/***************************************************************
 Main Java runtime for NAIS Radio Data Consumer Tasks
 
 original coding: Anthony C. Amburn
 date: 09/25/2006
 
 Change History"
 
 date                      author               reason
 
 ***********************************************************//

package CGAISCommsFeeds;

import CGAISCommsConfig.NAISToXML;
import NAISCommsDBProcesses.SQLConnectionFactory;
import NAISCommsObjects.NAISdbCommsStoredProcedure;
import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mil.uscg.util.Logging;
import mil.uscg.util.PropertyUtil;

public class NAISCommsMessageConsumers {

    int totalConsumers = 0;
    private BlockingQueue<String[]> AISqueue = null;
    private Connection[] aisCons;
    private SQLConnectionFactory mySQL;
    private ExecutorService aisExecutor;
    private NAISToXML toXML;
    private Boolean bDisplayXML = false;
    private NAISCommsMain NAISmain;

    public NAISCommsMessageConsumers(NAISCommsMain NAISmain, int totalConsumers) {
        this.AISqueue = NAISmain.getAISqueue();
        this.NAISmain = NAISmain;
        this.totalConsumers = totalConsumers;
        aisCons = new Connection[totalConsumers];
        mySQL = new SQLConnectionFactory();
        CreateDBCons(totalConsumers);
        aisExecutor = Executors.newFixedThreadPool(totalConsumers);
        if (PropertyUtil.getProps().containsKey("displayxml")) {
            bDisplayXML = Boolean.parseBoolean(PropertyUtil.getProps().getProperty("displayxml"));
        }
    }

    public void consumeAISMessages() {
        String aisMessage[];
        int ORAIndex = 0;

        try {
            toXML = new NAISToXML();
            while (true) {
                aisMessage = AISqueue.take();

                if (!aisMessage[0].equals("10") && !aisMessage[0].equals("12") && !aisMessage[0].equals("")) {
                    aisExecutor.execute(new NAISdbCommsStoredProcedure(aisMessage, aisCons[ORAIndex]));
                    toXML.ProcessAISXML(aisMessage, bDisplayXML, NAISmain);
                }


                if (++ORAIndex < totalConsumers) {
                    ORAIndex = 0;
                }
            }
        } catch (InterruptedException ex) {
            Logging.warn("Waiting for Messages");
        } catch (Exception e) {
            NAISCommsMain.getStackTrace(e);
        }
    }

    private void CreateDBCons(int TotCons) {
        for (int idx = 0; idx < TotCons; idx++) {
            aisCons[idx] = mySQL.getCWSSdbconn();
        }
    }
}
