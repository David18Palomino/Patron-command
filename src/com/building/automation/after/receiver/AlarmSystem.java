package com.building.automation.after.receiver;

/**
 * Receiver: Sistema de seguridad y alarma perimetral.
 * Contiene la lógica real para armar y desarmar el sistema de vigilancia.
 */
public class AlarmSystem {
    private final String zone;
    private boolean armed;

    public AlarmSystem(String zone) {
        this.zone = zone;
        this.armed = false;
    }

    public void activate() {
        this.armed = true;
        System.out.println("    [Sistema de Alarma - " + zone + "] ACTIVADA / ARMADA. Monitoreo en alerta máxima.");
    }

    public void deactivate() {
        this.armed = false;
        System.out.println("    [Sistema de Alarma - " + zone + "] DESACTIVADA / DESARMADA.");
    }

    public void setArmed(boolean state) {
        if (state) {
            activate();
        } else {
            deactivate();
        }
    }

    public boolean isArmed() {
        return armed;
    }

    public String getZone() {
        return zone;
    }

    public String getStatus() {
        return zone + ": " + (armed ? "ARMADA / ACTIVADA" : "DESARMADA / INACTIVA");
    }

    @Override
    public String toString() {
        return getStatus();
    }
}

