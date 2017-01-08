
package NAISCommsObjects;

import CGAISCommsFeeds.NAISCommsMain;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import mil.uscg.util.Logging;

public class NAISdbCommsStoredProcedure implements Runnable{

    String[] aisData = null;
    Connection con = null;

    public NAISdbCommsStoredProcedure(String[] aisData, Connection con) {

        this.aisData = aisData;
        this.con = con;
    }

    public void run() {

        try {
            CallableStatement insertNAISComms = con.prepareCall("{call PKG_ais_FEED.usp_AddReport(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            insertNAISComms.setString(1, aisData[0]);
            insertNAISComms.setString(2, aisData[1]);
            insertNAISComms.setString(3, aisData[2]);
            insertNAISComms.setString(4, aisData[3]);
            insertNAISComms.setString(5, aisData[4]);
            insertNAISComms.setString(6, aisData[5]);
            insertNAISComms.setString(7, aisData[6]);
            insertNAISComms.setString(8, aisData[7]);
            insertNAISComms.setString(9, aisData[8]);
            insertNAISComms.setString(10, aisData[9]);
            insertNAISComms.setString(11, aisData[10]);
            insertNAISComms.setString(12, aisData[11]);
            insertNAISComms.setString(13, aisData[12]);
            insertNAISComms.setString(14, aisData[13]);
            insertNAISComms.setString(15, aisData[14]);
            insertNAISComms.setString(16, aisData[15]);
            insertNAISComms.setString(17, aisData[16]);
            insertNAISComms.setString(18, aisData[17]);
            insertNAISComms.setString(19, aisData[18]);
            insertNAISComms.setString(20, aisData[19]);
            insertNAISComms.setString(21, aisData[20]);
            insertNAISComms.setString(22, aisData[21]);
            insertNAISComms.setString(23, aisData[22]);
            insertNAISComms.setString(24, aisData[23]);
            insertNAISComms.setString(25, aisData[24]);
            insertNAISComms.setString(26, aisData[25]);
            insertNAISComms.setString(27, aisData[26]);
            insertNAISComms.execute();
            insertNAISComms.close();
            insertNAISComms = null;
        } catch (SQLException sex) {
            Logging.severe(NAISCommsMain.getStackTrace(sex));
        }
    }
}
