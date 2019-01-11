package es.upm.android.iot.jdelatorre.rdajila.appdispenser.util;

public final class Constant
{
    public static final String _PATH_SERVICE = "https://dispenserservice.azurewebsites.net";

    public static final int _MIN_PROGRESS = 0;

    public static final int _PROGRESS_BAR_WIDTH = 15;

    public static final int _BPROGRESS_BAR_WIDTH = 25;

    //public static final int _COLOR_RED = -2937298;
    public static final int _COLOR_RED = (255 & 0xff) << 24 | (203 & 0xff) << 16 | (67 & 0xff) << 8 | (53 & 0xff);;

    public static final int _COLOR_BLUE = (255 & 0xff) << 24 | (9 & 0xff) << 16 | (135 & 0xff) << 8 | (186 & 0xff);

    public static final String _UNIT_FOOD = "g";

    public static final String _UNIT_WATER = "ml";

    public static final int DELAY_REQUEST = 5000;

    public static final String _ACTION_TYPE = "MANUAL";

    public static final int _REQUEST_CONFIGURATION = 1;

    public static final int _ERROR_CONFIGURATION = 2;

}