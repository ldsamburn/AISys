
package NAISCommsDBProcesses;

import java.sql.*;
import mil.uscg.util.Logging;
import mil.uscg.util.PropertyUtil;

/**
 *
 * @author jpwallace
 */
public class SQLConnectionFactory {
    Connection con;
	
    public Connection getCWSSdbconn() {
        try {
            Class.forName( "oracle.jdbc.driver.OracleDriver" );
	} catch( ClassNotFoundException ex ) {
    //        Logging.severe("EXCEPTION: getCWSSdbconn: " + ex.getMessage());
	}

	boolean done = false;

	while( !done ) {
            try {
             //   Logging.debug("Attempting new database connection.."  + "jdbc:oracle:thin:@" +
             //                   PropertyUtil.getProps().getProperty("databaseIP") + ":" +
              //                  PropertyUtil.getProps().getProperty("databasePort") + ":" +
              //                  PropertyUtil.getProps().getProperty("databaseSID") + ", " +
              //                  PropertyUtil.getProps().getProperty("databaseUser") );
		con = DriverManager.getConnection( "jdbc:oracle:thin:@" +
                                PropertyUtil.getProps().getProperty("databaseIP") + ":" +
                                PropertyUtil.getProps().getProperty("databasePort") + ":" +
                                PropertyUtil.getProps().getProperty("databaseSID"),
                                PropertyUtil.getProps().getProperty("databaseUser"),
                                PropertyUtil.getProps().getProperty("databasePass"));
		System.out.println( "Established database connection.." );
              //  Logging.debug("Established database connection..");
		if( con != null ) {
                    done = true;
		}
            } catch( SQLException sqle ) {
                System.out.println();
		System.out.println( "Error creating connection to database.." );
		System.out.println();
	//	Logging.severe("EXCEPTION: getCWSSdbconn: " + sqle.getMessage());
		try {
                    Thread.sleep( 5000 );
		} catch( InterruptedException iex ) {
         //           Logging.severe("EXCEPTION: getCWSSdbconn: " + iex.getMessage());
		}

            }
	}
	return con;
    }

    public static SQLConnectionFactory ref = new SQLConnectionFactory();

    public static void close( ResultSet rs ) {
        try {
            rs.close();
	} catch( Exception ignored ) {
     //       Logging.severe("EXCEPTION: " + ignored.getMessage());
	}
    }

    public static void close( Statement stmt ) {
        try {
            stmt.close();
	} catch( Exception ignored ) {
      //      Logging.severe("EXCEPTION: " + ignored.getMessage());
	}
    }

    public static void close( Connection conn ) {
        try {
            conn.close();
	} catch( Exception ignored ) {
     //       Logging.severe("EXCEPTION: " + ignored.getMessage());
	}
    }
}

