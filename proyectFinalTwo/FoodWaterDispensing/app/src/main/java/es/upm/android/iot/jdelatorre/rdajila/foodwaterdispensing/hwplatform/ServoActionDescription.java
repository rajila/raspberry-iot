package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.hwplatform;

import java.io.UnsupportedEncodingException;

public class ServoActionDescription
{
    private int ang;
    private int sec;
    public int getAng() {
        return ang;
    }
    public void setAng(int ang)
    {
        //byte []aux = ang.;
        //try {
            //this.l1 = new String( aux, "UTF-8");
            this.ang = ang;
        //} catch (UnsupportedEncodingException e) {
        //    throw new IllegalStateException(e.getMessage());
        //}
    }
    public int getSec() {
        return sec;
    }
    public void setSec(int sec)
    {
        /*byte []aux = l2.getBytes();
        try {
            this.l2 = new String( aux, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }*/
        this.sec = sec;
    }

    public ServoActionDescription(int ang, int sec) {
        super();
        /*byte []aux = l1.getBytes();
        try {
            this.l1 = new String( aux, "UTF-8");
            aux = l2.getBytes();
            this.l2 = new String( aux, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }*/
        this.ang = ang;
        this.sec = sec;
    }
}