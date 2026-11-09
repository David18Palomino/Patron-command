package com.building.automation.before;

import com.building.automation.before.device.AirConditioner;
import com.building.automation.before.device.AlarmSystem;
import com.building.automation.before.device.Light;
import com.building.automation.before.device.SmartLock;
import com.building.automation.before.panel.ControlPanel;

/**
 * Clase principal para probar la versión ANTES de aplicar el patrón Command.
 * Demuestra el funcionamiento y evidencia los problemas de acoplamiento.
 */
public class MainBefore {
    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  CASO PRÁCTICO: AUTOMATIZACIÓN DE EDIFICIOS INTELIGENTES");
        System.out.println("  IMPLEMENTACIÓN 1: ARQUITECTURA ANTES DE APLICAR EL PATRÓN COMMAND");
        System.out.println("================================================================================\n");

        // 1. Instanciación de dispositivos concretos
        Light livingRoomLight = new Light("Salón Principal");
        Light kitchenLight = new Light("Cocina");
        AirConditioner centralAc = new AirConditioner("Zona Salón y Oficinas", 26);
        SmartLock frontDoorLock = new SmartLock("Puerta Principal");
        SmartLock garageLock = new SmartLock("Acceso Garaje");
        AlarmSystem buildingAlarm = new AlarmSystem("Perímetro Edificio");

        // 2. Instanciación del panel de control fuertemente acoplado
        ControlPanel panel = new ControlPanel(buildingAlarm);
        panel.registerLight(livingRoomLight);
        panel.registerLight(kitchenLight);
        panel.registerAirConditioner(centralAc);
        panel.registerLock(frontDoorLock);
        panel.registerLock(garageLock);

        System.out.println("[ESTADO INICIAL DE LOS DISPOSITIVOS]");
        panel.printCurrentDeviceStatus();

        // 3. Simulación de uso diurno: encender luces, abrir puertas, etc.
        System.out.println(">>> 1. Realizando operaciones diurnas individuales...");
        panel.turnOnLight(livingRoomLight);
        panel.turnOnLight(kitchenLight);
        panel.unlockDoor(frontDoorLock);
        panel.unlockDoor(garageLock);
        panel.turnOnAirConditioner(centralAc);
        panel.setAirConditionerTemperature(centralAc, 24);
        panel.deactivateAlarm();

        System.out.println("\n[ESTADO TRAS OPERACIONES DIURNAS]");
        panel.printCurrentDeviceStatus();

        // 4. Ejecución de Modo Noche (Directo, hardcodeado)
        System.out.println(">>> 2. Activando Modo Noche directamente desde el ControlPanel...");
        panel.executeNightMode(21);

        System.out.println("[ESTADO TRAS MODO NOCHE]");
        panel.printCurrentDeviceStatus();

        // 5. Análisis del problema arquitectónico
        System.out.println("================================================================================");
        System.out.println("  ANÁLISIS DE LIMITACIONES EN ESTA VERSIÓN (SIN PATRÓN COMMAND):");
        System.out.println("  1. ACOPLAMIENTO FUERTE: ControlPanel depende de las 4 clases concretas");
        System.out.println("     (Light, AirConditioner, SmartLock, AlarmSystem).");
        System.out.println("  2. SIN HISTORIAL DE COMANDOS: El panel no almacena registro de qué acciones");
        System.out.println("     se ejecutaron, quién las ordenó ni en qué orden ocurrieron.");
        System.out.println("  3. IMPOSIBILIDAD DE DESHACER (UNDO): ¿Cómo volvemos el Modo Noche atrás?");
        System.out.println("     Habría que escribir manualmente lógica de reversión dispersa y frágil.");
        System.out.println("  4. VIOLACIÓN DE OCP (Open/Closed Principle): Agregar un nuevo dispositivo");
        System.out.println("     (ej. PersianasMotorizadas) obliga a modificar y recompilar ControlPanel.");
        System.out.println("================================================================================\n");
    }
}

