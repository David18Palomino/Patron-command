package com.building.automation.after.command;

import com.building.automation.after.receiver.AlarmSystem;

/**
 * Concrete Command: Comando específico para desactivar la alarma de seguridad.
 */
public class DeactivateAlarmCommand implements Command {
    private final AlarmSystem alarm;
    private boolean previousArmed;

    public DeactivateAlarmCommand(AlarmSystem alarm) {
        this.alarm = alarm;
    }

    @Override
    public void execute() {
        this.previousArmed = alarm.isArmed();
        alarm.deactivate();
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la alarma (" 
                + alarm.getZone() + " -> " + (previousArmed ? "ARMADA" : "DESARMADA") + ")...");
        alarm.setArmed(previousArmed);
    }

    @Override
    public String getDescription() {
        return "Desactivar Alarma (" + alarm.getZone() + ")";
    }

    public AlarmSystem getAlarm() {
        return alarm;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

