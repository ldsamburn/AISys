package CGAISCommsConfig;

import CGAISCommsFeeds.NAISCommsMain;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mil.uscg.util.Logging;
import mil.uscg.util.PropertyUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class NAISToXML {

   private Element naisElement = null;
   private Document Doc;
//   private String[] sNAISData;
   private String RootAtt = " ";
  // private String formattedDate = "";

    public NAISToXML() throws Exception {

        if (PropertyUtil.getProps().containsKey("trackingserverguid")) {
            RootAtt = PropertyUtil.getProps().getProperty("trackingserverguid");
        }
    }

    public void ProcessAISXML(String[] sNAISData, Boolean debug, NAISCommsMain comMain) {

        naisElement = new Element("MESSAGE").setAttribute("ID", RootAtt);
        Doc = new Document(naisElement);


        naisElement.addContent(new Element("FIELD").addContent(sNAISData[0]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[1]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[2]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[3]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[4]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[5]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[6]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[7]));
        naisElement.addContent(new Element("FIELD").addContent(convertDate(Long.parseLong(sNAISData[8]))));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[9]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[10]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[11]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[12]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[13]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[14]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[15]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[16]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[17]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[18]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[19]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[20]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[21]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[22]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[23]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[24]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[25]));
        naisElement.addContent(new Element("FIELD").addContent(convertDate(Long.parseLong(sNAISData[26]))));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[3] + ", " + sNAISData[4]));

        sendData(comMain, debug);


    }

    public void ProcessTOLSafetyXML(String[] sNAISData, Boolean debug, NAISCommsMain comMain) {

        naisElement = new Element("MESSAGE").setAttribute("ID", RootAtt);
        Doc = new Document(naisElement);

        naisElement.addContent(new Element("FIELD").addContent(sNAISData[0]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[1]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[2]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[3]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[4]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[5]));
        naisElement.addContent(new Element("FIELD").addContent(sNAISData[6]));
        naisElement.addContent(new Element("FIELD").addContent(convertDate(Long.parseLong(sNAISData[7]))));


        sendData(comMain, debug);

    }

    private void sendData(NAISCommsMain comMain, Boolean debug) {
        try {

            XMLOutputter outp = new XMLOutputter();

            if (debug == true) {
                outp.output(Doc, System.out);
            }

            if (comMain.getosClient() != null) {
                outp.output(Doc, comMain.getosClient());
                comMain.getosClient().flush();
            }

        } catch (IOException ioe) {
            Logging.severe(ioe.getMessage());
            comMain.reSetosClient();
        }
    }

    private String convertDate(Long str) {

        Date epoch = new Date(str * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return sdf.format(epoch);
    }
}
