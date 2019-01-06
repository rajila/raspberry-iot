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
        this.ang = ang;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec)
    {
        this.sec = sec;
    }

    public ServoActionDescription(int ang, int sec) {
        super();
        this.ang = ang;
        this.sec = sec;
    }
}