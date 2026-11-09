package com.building.automation.before.device;

/**
 * Dispositivo de iluminación en la versión ANTES de aplicar Command.
 * Representa una luz inteligente en una ubicación específica del edificio.
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
        System.out.println("  [Luz - " + location + "] Encendida (ON).");
    }

    public void turnOff() {
        this.on = false;
        System.out.println("  [Luz - " + location + "] Apagada (OFF).");
    }

    public boolean isOn() {
        return on;
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

