package com.building.automation.before.device;

/**
 * Dispositivo de cerradura electrónica en la versión ANTES de aplicar Command.
 * Permite bloquear (cerrar) y desbloquear (abrir) accesos y puertas del edificio.
 */
public class SmartLock {
    private final String doorName;
    private boolean locked;

    public SmartLock(String doorName) {
        this.doorName = doorName;
        this.locked = true; // Por defecto asegurada
    }

    public void lock() {
        this.locked = true;
        System.out.println("  [Cerradura - " + doorName + "] BLOQUEADA / CERRADA (Segura).");
    }

    public void unlock() {
        this.locked = false;
        System.out.println("  [Cerradura - " + doorName + "] DESBLOQUEADA / ABIERTA.");
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

