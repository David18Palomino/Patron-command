package com.building.automation.after.command;

import com.building.automation.after.receiver.Light;

/**
 * Concrete Command: Comando específico para apagar una luz.
 * Extiende o delega la operación asegurando la reversibilidad completa en undo().
 */
public class TurnOffLightCommand implements Command {
    private final Light light;
    private boolean previousState;

    public TurnOffLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        this.previousState = light.isOn();
        light.turnOff();
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la luz (" + light.getLocation() + " -> " + (previousState ? "ON" : "OFF") + ")...");
        light.setOn(previousState);
    }

    @Override
    public String getDescription() {
        return "Apagar Luz (" + light.getLocation() + ")";
    }

    public Light getLight() {
        return light;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

