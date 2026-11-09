package com.building.automation.after.command;

/**
 * Interfaz Command (Patrón de diseño Command).
 * Declara los métodos obligatorios para la ejecución y reversión de solicitudes.
 */
public interface Command {
    /**
     * Ejecuta la operación encapsulada sobre el Receiver correspondiente.
     */
    void execute();

    /**
     * Revierte el efecto de la operación, restaurando el Receiver a su estado previo.
     */
    void undo();

    /**
     * Devuelve una descripción legible del comando para trazabilidad e historial.
     */
    String getDescription();
}

