package com.building.automation.after.receiver;

/**
 * Receiver: Cerradura electrónica inteligente.
 * Contiene la lógica real para bloquear (asegurar) y desbloquear (abrir) accesos.
 */
public class SmartLock {
    private final String doorName;
    private boolean locked;

    public SmartLock(String doorName) {
        this.doorName = doorName;
        this.locked = true; // Por defecto cerrada/bloqueada
    }

    public void lock() {
        this.locked = true;
        System.out.println("    [Cerradura - " + doorName + "] BLOQUEADA / CERRADA.");
    }

    public void unlock() {
        this.locked = false;
        System.out.println("    [Cerradura - " + doorName + "] DESBLOQUEADA / ABIERTA.");
    }

    public void setLocked(boolean state) {
        if (state) {
            lock();
        } else {
            unlock();
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public String getDoorName() {
        return doorName;
    }

    public String getStatus() {
        return doorName + ": " + (locked ? "BLOQUEADA (CERRADA)" : "DESBLOQUEADA (ABIERTA)");
    }

    @Override
    public String toString() {
        return getStatus();
    }
}

