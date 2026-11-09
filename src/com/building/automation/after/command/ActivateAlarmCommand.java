package com.building.automation.after.command;

import com.building.automation.after.receiver.AlarmSystem;

/**
 * Concrete Command: Activa el sistema de alarma de seguridad.
 * Guarda el estado previo para poder desarmar o restaurar el estado en undo().
 */
public class ActivateAlarmCommand implements Command {
    private final AlarmSystem alarm;
    private final boolean targetArmed;
    private boolean previousArmed;

    public ActivateAlarmCommand(AlarmSystem alarm) {
        this(alarm, true);
    }

    public ActivateAlarmCommand(AlarmSystem alarm, boolean targetArmed) {
        this.alarm = alarm;
        this.targetArmed = targetArmed;
    }

    @Override
    public void execute() {
        this.previousArmed = alarm.isArmed();
        alarm.setArmed(targetArmed);
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la alarma (" 
                + alarm.getZone() + " -> " + (previousArmed ? "ARMADA" : "DESARMADA") + ")...");
        alarm.setArmed(previousArmed);
    }

    @Override
    public String getDescription() {
        return (targetArmed ? "Activar Alarma" : "Desactivar Alarma") + " (" + alarm.getZone() + ")";
    }

    public AlarmSystem getAlarm() {
        return alarm;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

