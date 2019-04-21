package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.util;

public final class Constant
{
    public static final String TALKBACK_ID = "30174";
    public static final String TALKBACK_KEY = "82VRSK83J7RYH5OF";

    public static final String PIN_SERVO_WATER = "PWM0"; //BCM18 -> PIN: 12
    public static final String PIN_SERVO_DIGITALBALANCE = "PWM1"; //BCM13 -> PIN: 33

    public static final double ANGLE_MIN = 0f;
    public static final double ANGLE_MAX = 180f;

    public static final int DELAY_REQUEST = 25000;

    public static final String THING_ID = "179";
    public static final String SERVO_DIGITAL_BALANCE_ID = "002";
    public static final String SERVO_WATER_ID = "001";

    public static final String PINRED = "BCM5"; // NUM PIN 29
    public static final String PINGREEN = "BCM6"; // NUM PIN 31
}
