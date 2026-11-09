package com.building.automation.after.invoker;

import com.building.automation.after.command.Command;

/**
 * Invoker: Panel de Control Central en la versión CON Patrón Command.
 * 
 * BENEFICIO DE DISEÑO:
 * - Desacoplamiento total: Desconoce completamente qué dispositivos existen (Light, AirConditioner, SmartLock, AlarmSystem).
 * - Principio de Responsabilidad Única (SRP): Su único rol es despachar comandos y coordinar el historial.
 * - Abierto a la Extensión (OCP): Se pueden crear cientos de nuevos comandos (persianas, cámaras, aspersores)
 *   sin tocar ni una sola línea de código de esta clase.
 * - Soporte nativo para Deshacer (Undo) e Historial.
 */
public class ControlPanel {
    private final CommandHistory history;

    public ControlPanel() {
        this.history = new CommandHistory();
    }

    public ControlPanel(CommandHistory history) {
        this.history = history;
    }

    /**
     * Ejecuta cualquier comando polimórfico y lo registra en el historial.
     */
    public void executeCommand(Command command) {
        if (command == null) {
            System.out.println("[ControlPanel - Invoker] Advertencia: Intento de ejecutar comando nulo.");
            return;
        }

        System.out.println("[ControlPanel - Invoker] Despachando comando: " + command.getDescription());
        command.execute();
        history.push(command);
    }

    /**
     * Deshace la última operación ejecutada extrayéndola de la pila de historial.
     */
    public boolean undoLastCommand() {
        if (history.isEmpty()) {
            System.out.println("[ControlPanel - Invoker] No hay operaciones para deshacer en el historial.");
            return false;
        }

        Command lastCommand = history.pop();
        System.out.println("\n[ControlPanel - Invoker] >>> SOLICITUD DE DESHACER (UNDO) <<<");
        System.out.println("  Revirtiendo: " + lastCommand.getDescription());
        lastCommand.undo();
        System.out.println("[ControlPanel - Invoker] Reversión finalizada con éxito.\n");
        return true;
    }

    public CommandHistory getHistory() {
        return history;
    }
}

