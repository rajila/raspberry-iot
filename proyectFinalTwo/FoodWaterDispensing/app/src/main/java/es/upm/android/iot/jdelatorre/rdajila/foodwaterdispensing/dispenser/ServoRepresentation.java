package es.upm.android.iot.jdelatorre.rdajila.foodwaterdispensing.dispenser;

import es.upm.dte.iot.infomodel.ThingActuation;

public class ServoRepresentation
{
    private ThingActuation dispenser;

    /**
     *
     * @param dispenser
     */
    public ServoRepresentation(ThingActuation dispenser) {
        this.dispenser = dispenser;
    }

    public ThingActuation getDispenser() {
        return this.dispenser;
    }

    /**
     *
     * @param dispenser
     */
    public void setDispenser(ThingActuation dispenser) {
        this.dispenser = dispenser;
    }

    public ServoRepresentation() {
    }
}