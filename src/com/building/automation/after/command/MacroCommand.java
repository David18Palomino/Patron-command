package com.building.automation.after.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete Command (MacroCommand): Agrupa múltiples comandos para ser ejecutados como una sola transacción lógica.
 * 
 * Regla de Oro en Command:
 * - En execute(): Se ejecutan los comandos secuencialmente en orden directo (0 a N-1).
 * - En undo(): Se revierten en orden inverso (N-1 hacia 0) para preservar la integridad de dependencias de estado.
 */
public class MacroCommand implements Command {
    private final String name;
    private final List<Command> commands;

    public MacroCommand(String name) {
        this.name = name;
        this.commands = new ArrayList<>();
    }

    public MacroCommand(String name, List<Command> commands) {
        this.name = name;
        this.commands = new ArrayList<>(commands);
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public void execute() {
        System.out.println("  >>> [MacroCommand: " + name + "] Iniciando ejecución en lote (" + commands.size() + " sub-comandos)...");
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            System.out.println("    (" + (i + 1) + "/" + commands.size() + ") Ejecutando: " + cmd.getDescription());
            cmd.execute();
        }
        System.out.println("  >>> [MacroCommand: " + name + "] Ejecución completada con éxito.");
    }

    @Override
    public void undo() {
        System.out.println("  <<< [MacroCommand: " + name + "] DESHACIENDO en orden INVERSO (" + commands.size() + " sub-comandos)...");
        for (int i = commands.size() - 1; i >= 0; i--) {
            Command cmd = commands.get(i);
            System.out.println("    (" + (commands.size() - i) + "/" + commands.size() + ") Revertir: " + cmd.getDescription());
            cmd.undo();
        }
        System.out.println("  <<< [MacroCommand: " + name + "] Reversión completada con éxito.");
    }

    @Override
    public String getDescription() {
        return "Macro: " + name + " (" + commands.size() + " acciones)";
    }

    public String getName() {
        return name;
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}

