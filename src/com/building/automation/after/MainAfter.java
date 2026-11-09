package com.building.automation.after;

import com.building.automation.after.command.*;
import com.building.automation.after.invoker.ControlPanel;
import com.building.automation.after.receiver.*;

import java.util.Arrays;
import java.util.List;

/**
 * Clase principal que demuestra la arquitectura y funcionamiento del sistema
 * DESPUÉS de aplicar el patrón de diseño Command.
 * 
 * Demuestra:
 * 1. Operaciones individuales polimórficas (execute y undo individual).
 * 2. Comprobación del estado antes y después de cada acción.
 * 3. Modo Noche avanzado mediante MacroCommand (luces off, AC a 21°C, puertas cerradas, alarma activa).
 * 4. Consulta del historial de auditoría y la pila de comandos.
 * 5. Reversión completa (Undo) del Modo Noche en orden inverso.
 */
public class MainAfter {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  CASO PRÁCTICO: AUTOMATIZACIÓN DE EDIFICIOS INTELIGENTES");
        System.out.println("  IMPLEMENTACIÓN 2: ARQUITECTURA DESPUÉS DE APLICAR EL PATRÓN COMMAND");
        System.out.println("================================================================================\n");

        // 1. Instanciación de los Receptores (Receivers)
        Light livingRoomLight = new Light("Salón Principal");
        Light kitchenLight = new Light("Cocina");
        Light hallwayLight = new Light("Pasillo Principal");
        AirConditioner centralAc = new AirConditioner("Climatización Central", 26);
        SmartLock frontDoorLock = new SmartLock("Puerta Principal");
        SmartLock garageLock = new SmartLock("Acceso Garaje");
        AlarmSystem perimeterAlarm = new AlarmSystem("Seguridad Perimetral");

        List<Light> allLights = Arrays.asList(livingRoomLight, kitchenLight, hallwayLight);
        List<SmartLock> allLocks = Arrays.asList(frontDoorLock, garageLock);

        // 2. Instanciación del Invocador (Invoker)
        ControlPanel invoker = new ControlPanel();

        printSystemSnapshot("1. ESTADO INICIAL DEL SISTEMA", allLights, centralAc, allLocks, perimeterAlarm);

        // 3. Ejecución de operaciones individuales (demostrando encapsulamiento)
        System.out.println(">>> PASO A: Ejecución de operaciones individuales diurnas...");
        
        Command encenderSalon = new TurnOnLightCommand(livingRoomLight);
        Command encenderCocina = new TurnOnLightCommand(kitchenLight);
        Command regularClima = new SetTemperatureCommand(centralAc, 23);
        Command abrirPuerta = new UnlockDoorCommand(frontDoorLock);

        invoker.executeCommand(encenderSalon);
        invoker.executeCommand(encenderCocina);
        invoker.executeCommand(regularClima);
        invoker.executeCommand(abrirPuerta);

        printSystemSnapshot("2. ESTADO TRAS OPERACIONES INDIVIDUALES", allLights, centralAc, allLocks, perimeterAlarm);

        // 4. Demostración de Deshacer (Undo) de una operación individual
        System.out.println(">>> PASO B: Deshaciendo la última operación individual (Abrir Puerta)...");
        invoker.undoLastCommand();

        printSystemSnapshot("3. ESTADO TRAS UNDO DE LA ÚLTIMA OPERACIÓN", allLights, centralAc, allLocks, perimeterAlarm);

        // 5. Configuración y Ejecución del MODO NOCHE mediante MacroCommand
        System.out.println(">>> PASO C: Configurando y Ejecutando MODO NOCHE (MacroCommand)...");
        System.out.println("El Modo Noche agrupa consecutivamente:");
        System.out.println("  1. Apagar todas las luces (Salón, Cocina, Pasillo)");
        System.out.println("  2. Ajustar aire acondicionado a 21°C");
        System.out.println("  3. Bloquear todas las cerraduras (Principal, Garaje)");
        System.out.println("  4. Activar sistema de alarma perimetral\n");

        MacroCommand modoNoche = new MacroCommand("Modo Noche Edificio", Arrays.asList(
                new TurnOffLightCommand(livingRoomLight),
                new TurnOffLightCommand(kitchenLight),
                new TurnOffLightCommand(hallwayLight),
                new SetTemperatureCommand(centralAc, 21),
                new LockDoorCommand(frontDoorLock),
                new LockDoorCommand(garageLock),
                new ActivateAlarmCommand(perimeterAlarm)
        ));

        invoker.executeCommand(modoNoche);

        printSystemSnapshot("4. ESTADO TRAS ACTIVACIÓN DEL MODO NOCHE", allLights, centralAc, allLocks, perimeterAlarm);

        // 6. Consulta del historial de comandos ejecutados
        System.out.println(">>> PASO D: Inspeccionando el Historial de Comandos (CommandHistory)...");
        invoker.getHistory().printHistory();

        // 7. Deshacer el MODO NOCHE completo
        System.out.println(">>> PASO E: Deshaciendo MODO NOCHE (Undo del MacroCommand)...");
        System.out.println("Al llamar a undo(), MacroCommand recorre sus comandos en ORDEN INVERSO,");
        System.out.println("devolviendo cada dispositivo al estado exacto que tenía antes de ejecutar el macro.");
        invoker.undoLastCommand();

        printSystemSnapshot("5. ESTADO TRAS DESHACER MODO NOCHE (REVERSIÓN EXACTA)", allLights, centralAc, allLocks, perimeterAlarm);

        // 8. Consulta final del historial
        System.out.println(">>> PASO F: Estado final del Historial y Pila de Undo...");
        invoker.getHistory().printHistory();

        // 9. Resumen de beneficios académicos del Patrón Command
        System.out.println("================================================================================");
        System.out.println("  RESUMEN DE BENEFICIOS DEL PATRÓN COMMAND EN ESTE CASO PRÁCTICO:");
        System.out.println("  ✔ DESACOPLAMIENTO TOTAL: ControlPanel sólo invoca Command; no conoce");
        System.out.println("    las clases Light, AirConditioner, SmartLock ni AlarmSystem.");
        System.out.println("  ✔ EXTENSIBILIDAD (OCP): Se pueden agregar nuevos comandos y dispositivos");
        System.out.println("    sin modificar el panel invocador.");
        System.out.println("  ✔ SOPORTE LIFO UNDO: Cada comando sabe cómo revertir su propio efecto");
        System.out.println("    almacenando el estado previo del receiver.");
        System.out.println("  ✔ MACROCOMANDOS COMPUESTOS: Múltiples acciones atómicas se agrupan en una sola");
        System.out.println("    unidad lógica y se revierten en orden inverso.");
        System.out.println("================================================================================\n");
    }

    private static void printSystemSnapshot(String title, List<Light> lights, AirConditioner ac,
                                           List<SmartLock> locks, AlarmSystem alarm) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  [SNAPSHOT] " + title);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  LUCES:");
        for (Light l : lights) {
            System.out.println("    • " + l.getStatus());
        }
        System.out.println("  CLIMATIZACIÓN:");
        System.out.println("    • " + ac.getStatus());
        System.out.println("  CERRADURAS:");
        for (SmartLock sl : locks) {
            System.out.println("    • " + sl.getStatus());
        }
        System.out.println("  SISTEMA DE SEGURIDAD:");
        System.out.println("    • " + alarm.getStatus());
        System.out.println("--------------------------------------------------------------------------------\n");
    }
}

