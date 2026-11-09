package com.building.automation.test;

import com.building.automation.after.command.*;
import com.building.automation.after.invoker.ControlPanel;
import com.building.automation.after.receiver.*;

import java.util.Arrays;

/**
 * Suite de Pruebas Automatizadas para verificar el comportamiento del Patrón Command.
 * Valida de forma programática las operaciones execute, undo, MacroCommand y la pila LIFO.
 */
public class CommandPatternTest {

    private static int testsPassed = 0;
    private static int totalTests = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  EJECUTANDO SUITE DE PRUEBAS AUTOMATIZADAS: PATRÓN COMMAND");
        System.out.println("================================================================================\n");

        testIndividualLightCommand();
        testIndividualTemperatureCommand();
        testIndividualLockCommand();
        testIndividualAlarmCommand();
        testMacroCommandExecutionAndUndoOrder();
        testCommandHistoryLIFO();

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  RESULTADO DE LAS PRUEBAS: " + testsPassed + " de " + totalTests + " PASADAS (100% ÉXITO)");
        System.out.println("--------------------------------------------------------------------------------\n");

        if (testsPassed != totalTests) {
            System.exit(1);
        }
    }

    private static void assertTrue(String testName, boolean condition, String errorMsg) {
        totalTests++;
        if (condition) {
            System.out.println("  ✔ [PASÓ] " + testName);
            testsPassed++;
        } else {
            System.err.println("  ❌ [FALLÓ] " + testName + " -> " + errorMsg);
        }
    }

    /**
     * Prueba CP-01: Encendido, apagado y reversión exacta de Light.
     */
    private static void testIndividualLightCommand() {
        Light light = new Light("Zona Test");
        ControlPanel invoker = new ControlPanel();

        assertTrue("CP-01.1: Luz inicia apagada", !light.isOn(), "La luz debió iniciar apagada");

        Command turnOn = new TurnOnLightCommand(light);
        invoker.executeCommand(turnOn);
        assertTrue("CP-01.2: Encender luz vía Command", light.isOn(), "La luz debió encenderse");

        invoker.undoLastCommand();
        assertTrue("CP-01.3: Deshacer encendido restaura OFF", !light.isOn(), "La luz debió volver a apagada");
    }

    /**
     * Prueba CP-02: Configuración de clima y memoria de temperatura previa.
     */
    private static void testIndividualTemperatureCommand() {
        AirConditioner ac = new AirConditioner("Clima Test", 26);
        ControlPanel invoker = new ControlPanel();

        assertTrue("CP-02.1: AC inicia a 26°C", ac.getTemperature() == 26, "Temperatura inicial incorrecta");

        Command set21 = new SetTemperatureCommand(ac, 21);
        invoker.executeCommand(set21);
        assertTrue("CP-02.2: Ajuste a 21°C vía Command", ac.getTemperature() == 21, "Debió fijar 21°C");

        invoker.undoLastCommand();
        assertTrue("CP-02.3: Deshacer restaura los 26°C previos", ac.getTemperature() == 26, "No restauró temperatura previa");
    }

    /**
     * Prueba CP-03: Bloqueo/desbloqueo de cerraduras electrónicas.
     */
    private static void testIndividualLockCommand() {
        SmartLock lock = new SmartLock("Acceso Test");
        ControlPanel invoker = new ControlPanel();

        assertTrue("CP-03.1: Cerradura inicia bloqueada", lock.isLocked(), "Debió iniciar bloqueada");

        Command unlock = new UnlockDoorCommand(lock);
        invoker.executeCommand(unlock);
        assertTrue("CP-03.2: Desbloqueo vía Command", !lock.isLocked(), "Debió quedar desbloqueada");

        invoker.undoLastCommand();
        assertTrue("CP-03.3: Deshacer vuelve a bloquear la cerradura", lock.isLocked(), "Debió volver a bloquear");
    }

    /**
     * Prueba CP-04: Activación y desactivación de alarma.
     */
    private static void testIndividualAlarmCommand() {
        AlarmSystem alarm = new AlarmSystem("Alarma Test");
        ControlPanel invoker = new ControlPanel();

        assertTrue("CP-04.1: Alarma inicia desarmada", !alarm.isArmed(), "Debió iniciar desarmada");

        Command activate = new ActivateAlarmCommand(alarm);
        invoker.executeCommand(activate);
        assertTrue("CP-04.2: Activación vía Command", alarm.isArmed(), "Debió activarse");

        invoker.undoLastCommand();
        assertTrue("CP-04.3: Deshacer vuelve a desarmar", !alarm.isArmed(), "Debió volver a desarmar");
    }

    /**
     * Prueba CP-05: MacroCommand Modo Noche y reversión simétrica en orden inverso.
     */
    private static void testMacroCommandExecutionAndUndoOrder() {
        Light light1 = new Light("Habitación 1");
        Light light2 = new Light("Habitación 2");
        AirConditioner ac = new AirConditioner("Clima Central", 24);
        SmartLock lock = new SmartLock("Entrada");
        AlarmSystem alarm = new AlarmSystem("Perímetro");

        // Configuramos estado diurno inicial: luces ON, cerradura abierta, alarma OFF
        light1.turnOn();
        light2.turnOn();
        lock.unlock();
        ac.setTemperature(25);

        ControlPanel invoker = new ControlPanel();

        MacroCommand modoNoche = new MacroCommand("Modo Noche Test", Arrays.asList(
                new TurnOffLightCommand(light1),
                new TurnOffLightCommand(light2),
                new SetTemperatureCommand(ac, 20),
                new LockDoorCommand(lock),
                new ActivateAlarmCommand(alarm)
        ));

        invoker.executeCommand(modoNoche);

        assertTrue("CP-05.1: Modo Noche apaga luz 1", !light1.isOn(), "Luz 1 no apagó");
        assertTrue("CP-05.2: Modo Noche apaga luz 2", !light2.isOn(), "Luz 2 no apagó");
        assertTrue("CP-05.3: Modo Noche baja clima a 20°C", ac.getTemperature() == 20, "AC no bajó a 20°C");
        assertTrue("CP-05.4: Modo Noche bloquea cerradura", lock.isLocked(), "Cerradura no bloqueó");
        assertTrue("CP-05.5: Modo Noche activa alarma", alarm.isArmed(), "Alarma no activó");

        // Deshacemos el Modo Noche
        invoker.undoLastCommand();

        assertTrue("CP-05.6: Undo Modo Noche restaura luz 1 a ON", light1.isOn(), "Luz 1 no volvió a ON");
        assertTrue("CP-05.7: Undo Modo Noche restaura luz 2 a ON", light2.isOn(), "Luz 2 no volvió a ON");
        assertTrue("CP-05.8: Undo Modo Noche restaura clima a 25°C", ac.getTemperature() == 25, "AC no volvió a 25°C");
        assertTrue("CP-05.9: Undo Modo Noche restaura cerradura abierta", !lock.isLocked(), "Cerradura no volvió a desbloqueada");
        assertTrue("CP-05.10: Undo Modo Noche desarma alarma", !alarm.isArmed(), "Alarma no volvió a desarmada");
    }

    /**
     * Prueba CP-06: Estructura LIFO de CommandHistory.
     */
    private static void testCommandHistoryLIFO() {
        ControlPanel invoker = new ControlPanel();
        Light l1 = new Light("L1");
        Light l2 = new Light("L2");

        invoker.executeCommand(new TurnOnLightCommand(l1));
        invoker.executeCommand(new TurnOnLightCommand(l2));

        assertTrue("CP-06.1: Historial tiene 2 comandos", invoker.getHistory().size() == 2, "Tamaño incorrecto de pila");

        invoker.undoLastCommand();
        assertTrue("CP-06.2: Undo revierte primero el último (L2 apagada, L1 sigue encendida)", 
                !l2.isOn() && l1.isOn(), "No respetó orden LIFO");

        invoker.undoLastCommand();
        assertTrue("CP-06.3: Segundo Undo revierte el primero (L1 apagada)", !l1.isOn(), "L1 no apagó");
        assertTrue("CP-06.4: Historial queda vacío tras desapilar", invoker.getHistory().isEmpty(), "La pila debió quedar vacía");
    }
}

