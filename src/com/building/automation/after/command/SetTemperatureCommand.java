package com.building.automation.after.command;

import com.building.automation.after.receiver.AirConditioner;

/**
 * Concrete Command: Configura la temperatura del aire acondicionado.
 * Registra la temperatura previa y el estado de encendido para poder deshacer la operación con fidelidad total.
 */
public class SetTemperatureCommand implements Command {
    private final AirConditioner airConditioner;
    private final int targetTemperature;
    private int previousTemperature;
    private boolean previousPowerState;

    public SetTemperatureCommand(AirConditioner airConditioner, int targetTemperature) {
        this.airConditioner = airConditioner;
        this.targetTemperature = targetTemperature;
    }

    @Override
    public void execute() {
        this.previousTemperature = airConditioner.getTemperature();
        this.previousPowerState = airConditioner.isOn();
        airConditioner.setTemperature(targetTemperature);
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando temperatura previa (" + airConditioner.getLocation() 
                + " -> " + previousTemperature + "°C, Encendido: " + previousPowerState + ")...");
        airConditioner.setTemperature(previousTemperature);
        if (!previousPowerState) {
            airConditioner.turnOff();
        }
    }

    @Override
    public String getDescription() {
        return "Ajustar Temperatura a " + targetTemperature + "°C (" + airConditioner.getLocation() + ")";
    }

    public AirConditioner getAirConditioner() {
        return airConditioner;
    }

    public int getTargetTemperature() {
        return targetTemperature;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

