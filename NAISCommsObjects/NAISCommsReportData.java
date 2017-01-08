package NAISCommsObjects;

public class NAISCommsReportData {

    int msgType;
    long mmsi;
    double sog;
    double longitude;
    double latitude;
    double cog;
    double heading;
    double alt;
    long seconds;
    int navStatus;
    double rotAIS;
    int typeOfNavaid;
    String name;
    String callsign;
    short lenA;
    short lenB;
    short lenC;
    short lenD;
    int imo_number;
    int type_of_ship_and_cargo;
    int electronic_fixing_device;
    int eta;
    float max;
    String destination;
    int offPositionIndicator;
    String recvMMSI;
    long recvTime;


    public void setmsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setMMSI(long mmsi) {
        this.mmsi = mmsi;
    }

    public void setSOG(double sog) {
        this.sog = sog;
    }

    public void setLon(double longitude) {
        this.longitude = longitude;
    }

    public void setLat(double latitude) {
        this.latitude = latitude;
    }

    public void setCOG(double cog) {

        this.cog = cog;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setalt(double alt) {
        this.alt = alt;
    }

    public void setseconds(long seconds) {
        this.seconds = seconds;
    }

    public void setNavStatus(int navStatus) {
        this.navStatus = navStatus;
    }

    public void setRot(double rotAIS) {
        this.rotAIS = rotAIS;
    }

    public void setNavType(int typeOfNavaid) {
        this.typeOfNavaid = typeOfNavaid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCallSign(String callsign) {
        this.callsign = callsign;
    }

    public void setLenA(short lenA) {
        this.lenA = lenA;
    }

    public void setLenB(short lenB) {
        this.lenA = lenB;
    }

    public void setLenC(short lenC) {
        this.lenA = lenC;
    }

    public void setLenD(short lenD) {
        this.lenA = lenD;
    }

    public void setIMO(int imo_number) {
        this.imo_number = imo_number;
    }

    public void setTypeCargo(int type_of_ship_and_cargo) {
        this.type_of_ship_and_cargo = type_of_ship_and_cargo;
    }

    public void setElectronics(int electronic_fixing_device) {
        this.electronic_fixing_device = electronic_fixing_device;
    }

    public void setETA(int eta) {
        this.eta = eta;
    }

    public void setMAX(float max) {
        this.max = max;
    }

    public void setDest(String destination) {
        this.destination = destination;
    }

    public void setPosId(int offPositionIndicator) {
        this.offPositionIndicator = offPositionIndicator;
    }

    public void setRevMMSI(String recvMMSI) {
        this.recvMMSI = recvMMSI;
    }

    public void setRecvTime(long recvTime) {
        this.recvTime = recvTime;
    }

     public int getmsgType() {
        return this.msgType;
    }

    public long getMMSI() {
        return this.mmsi;
    }

    public double getSOG() {
        return this.sog;
    }

    public double getLat() {
        return this.latitude;
    }

    public double getLon() {
        return this.longitude;
    }

    public double getCOG() {

        return this.cog;
    }

    public double getHeading() {
        return this.heading;
    }

    public double getalt() {
        return this.alt;
    }

    public long getseconds() {
        return this.seconds;
    }

    public int getNavStatus() {
        return this.navStatus;
    }

    public double getRot() {
        return this.rotAIS;
    }

    public int getNavType() {
        return this.typeOfNavaid;
    }

    public String getName() {
        return this.name;
    }

    public String getCallSign() {
        return this.callsign;
    }

    public short getLenA() {
        return this.lenA;
    }

    public short getLenB() {
        return this.lenA;
    }

    public short getLenC() {
        return this.lenA;
    }

    public short getLenD() {
        return this.lenA;
    }

    public int getIMO() {
        return this.imo_number;
    }

    public int getTypeCargo() {
        return this.type_of_ship_and_cargo;
    }

    public int getElectronics() {
        return this.electronic_fixing_device;
    }

    public int getETA() {
        return this.eta;
    }

    public float getMAX() {
        return this.max;
    }

    public String getDest() {
        return this.destination;
    }

    public int getPosId() {
        return this.offPositionIndicator;
    }

    public String getRevMMSI() {
        return this.recvMMSI;
    }

    public long getRecvTime() {
        return this.recvTime;
    }
}
