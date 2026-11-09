package com.building.automation.after.command;

import com.building.automation.after.receiver.SmartLock;

/**
 * Concrete Command: Comando específico para desbloquear (abrir) una cerradura.
 */
public class UnlockDoorCommand implements Command {
    private final SmartLock lock;
    private boolean previousLocked;

    public UnlockDoorCommand(SmartLock lock) {
        this.lock = lock;
    }

    @Override
    public void execute() {
        this.previousLocked = lock.isLocked();
        lock.unlock();
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la cerradura (" 
                + lock.getDoorName() + " -> " + (previousLocked ? "BLOQUEADA" : "DESBLOQUEADA") + ")...");
        lock.setLocked(previousLocked);
    }

    @Override
    public String getDescription() {
        return "Desbloquear Cerradura (" + lock.getDoorName() + ")";
    }

    public SmartLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

