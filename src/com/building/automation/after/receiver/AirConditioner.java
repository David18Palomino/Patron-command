package com.building.automation.after.receiver;

/**
 * Receiver: Dispositivo de climatización y aire acondicionado.
 * Contiene la lógica real para encender, apagar y regular la temperatura.
 */
public class AirConditioner {
    private final String location;
    private boolean on;
    private int temperature; // En grados Celsius

    public AirConditioner(String location, int defaultTemperature) {
        this.location = location;
        this.temperature = defaultTemperature;
        this.on = false;
    }

    public void turnOn() {
        this.on = true;
        System.out.println("    [Aire Acondicionado - " + location + "] Encendido a " + temperature + "°C.");
    }

    public void turnOff() {
        this.on = false;
        System.out.println("    [Aire Acondicionado - " + location + "] Apagado.");
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        if (!this.on) {
            this.on = true;
        }
        System.out.println("    [Aire Acondicionado - " + location + "] Temperatura ajustada a " + temperature + "°C.");
    }

    public void setPower(boolean state) {
        if (state) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isOn() {
        return on;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return location + ": " + (on ? "ENCENDIDO (" + temperature + "°C)" : "APAGADO (" + temperature + "°C objetivo)");
    }

    @Override
    public String toString() {
        return getStatus();
    }
}

