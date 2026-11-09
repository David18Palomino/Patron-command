package com.building.automation.after.receiver;

/**
 * Receiver: Dispositivo de iluminación inteligente.
 * Contiene la lógica de negocio real para encender, apagar y reportar el estado de la luz.
 */
public class Light {
    private final String location;
    private boolean on;

    public Light(String location) {
        this.location = location;
        this.on = false;
    }

    public void turnOn() {
        this.on = true;
        System.out.println("    [Luz - " + location + "] Encendida (ON).");
    }

    public void turnOff() {
        this.on = false;
        System.out.println("    [Luz - " + location + "] Apagada (OFF).");
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean state) {
        if (state) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return location + ": " + (on ? "ENCENDIDA (ON)" : "APAGADA (OFF)");
    }

    @Override
    public String toString() {
        return getStatus();
    }
}

