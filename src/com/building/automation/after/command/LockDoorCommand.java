package com.building.automation.after.command;

import com.building.automation.after.receiver.SmartLock;

/**
 * Concrete Command: Controla el bloqueo y aseguramiento de cerraduras electrónicas.
 * Guarda el estado previo de la cerradura para revertirla en undo().
 */
public class LockDoorCommand implements Command {
    private final SmartLock lock;
    private final boolean targetLocked;
    private boolean previousLocked;

    public LockDoorCommand(SmartLock lock) {
        this(lock, true);
    }

    public LockDoorCommand(SmartLock lock, boolean targetLocked) {
        this.lock = lock;
        this.targetLocked = targetLocked;
    }

    @Override
    public void execute() {
        this.previousLocked = lock.isLocked();
        lock.setLocked(targetLocked);
    }

    @Override
    public void undo() {
        System.out.println("    [DESHACIENDO] Restaurando estado previo de la cerradura (" 
                + lock.getDoorName() + " -> " + (previousLocked ? "BLOQUEADA" : "DESBLOQUEADA") + ")...");
        lock.setLocked(previousLocked);
    }

    @Override
    public String getDescription() {
        return (targetLocked ? "Bloquear Cerradura" : "Desbloquear Cerradura") + " (" + lock.getDoorName() + ")";
    }

    public SmartLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

