package com.building.automation.after.command;

import com.building.automation.after.receiver.Light;

/**
 * Concrete Command: Controla el encendido y apagado de una luz.
 * Almacena el estado previo para garantizar la reversibilidad exacta en undo().
 */
public class TurnOnLightCommand implements Command {
    private final Light light;
    private final boolean targetState; // true = encender, false = apagar
    private boolean previousState;

    /**
     * Constructor por defecto para encender la luz.
     */
    public TurnOnLightCommand(Light light) {
        this(light, true);
    }

    /**
     * Constructor que permite especificar si se desea encender (true) o apagar (false).
     */
    public TurnOnLightCommand(Light light, boolean targetState) {
        this.light = light;
        this.targetState = targetState;
    }

    @Override
    public void execute() {
        this.previousState = light.isOn();
        light.setOn(targetState);
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la luz (" + light.getLocation() + " -> " + (previousState ? "ON" : "OFF") + ")...");
        light.setOn(previousState);
    }

    @Override
    public String getDescription() {
        return (targetState ? "Encender Luz" : "Apagar Luz") + " (" + light.getLocation() + ")";
    }

    public Light getLight() {
        return light;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

