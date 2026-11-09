package com.building.automation.after.invoker;

import com.building.automation.after.command.Command;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Registra y gestiona el historial de comandos ejecutados en el sistema.
 * Permite tanto la inspección para auditoría como la funcionalidad de Deshacer (Undo) tipo LIFO (Last-In-First-Out).
 */
public class CommandHistory {
    private final Deque<Command> undoStack;
    private final List<String> auditTrail;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public CommandHistory() {
        this.undoStack = new ArrayDeque<>();
        this.auditTrail = new ArrayList<>();
    }

    /**
     * Registra un comando ejecutado en la pila de deshacer y en la traza de auditoría.
     */
    public void push(Command command) {
        undoStack.push(command);
        String logEntry = "[" + LocalTime.now().format(TIME_FORMATTER) + "] [EJECUTADO] " + command.getDescription();
        auditTrail.add(logEntry);
    }

    /**
     * Extrae el último comando ejecutado para ser revertido.
     */
    public Command pop() {
        if (undoStack.isEmpty()) {
            return null;
        }
        Command undone = undoStack.pop();
        String logEntry = "[" + LocalTime.now().format(TIME_FORMATTER) + "] [DESHECHO]  " + undone.getDescription();
        auditTrail.add(logEntry);
        return undone;
    }

    public boolean isEmpty() {
        return undoStack.isEmpty();
    }

    public int size() {
        return undoStack.size();
    }

    public List<Command> getStackSnapshot() {
        return new ArrayList<>(undoStack);
    }

    public List<String> getAuditTrail() {
        return Collections.unmodifiableList(auditTrail);
    }

    /**
     * Imprime por consola el historial completo de auditoría y la pila disponible para deshacer.
     */
    public void printHistory() {
        System.out.println("================ HISTORIAL DE COMANDOS REGISTRADOS ================");
        if (auditTrail.isEmpty()) {
            System.out.println("  (Historial vacío. No se han ejecutado comandos aún)");
        } else {
            for (String log : auditTrail) {
                System.out.println("  " + log);
            }
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.println("  Comandos disponibles para Deshacer (Undo Stack): " + undoStack.size());
        int index = 1;
        for (Command cmd : undoStack) {
            System.out.println("    " + index++ + ". " + cmd.getDescription());
        }
        System.out.println("===================================================================\n");
    }

    public void clear() {
        undoStack.clear();
        auditTrail.clear();
    }
}

