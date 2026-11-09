package com.building.automation.before.panel;

import com.building.automation.before.device.AirConditioner;
import com.building.automation.before.device.AlarmSystem;
import com.building.automation.before.device.Light;
import com.building.automation.before.device.SmartLock;

import java.util.ArrayList;
import java.util.List;

/**
 * Panel de control en la versión ANTES de aplicar el patrón Command.
 * 
 * PROBLEMA DE DISEÑO:
 * - Alto acoplamiento: Conoce directamente cada clase concreta de dispositivo (Light, AirConditioner, SmartLock, AlarmSystem).
 * - Violación del Principio Abierto/Cerrado (OCP): Si se agrega un nuevo dispositivo (ej. Persianas, Riego),
 *   este panel debe modificarse agregando nuevos campos y métodos específicos.
 * - Sin mecanismo genérico de Deshacer (Undo): No puede revertir acciones uniformemente.
 * - Sin historial de comandos ni macros reutilizables.
 */
public class ControlPanel {
    // Referencias directas y rígidas a los dispositivos concretos
    private final List<Light> lights;
    private final List<AirConditioner> airConditioners;
    private final List<SmartLock> locks;
    private final AlarmSystem alarmSystem;

    public ControlPanel(AlarmSystem alarmSystem) {
        this.lights = new ArrayList<>();
        this.airConditioners = new ArrayList<>();
        this.locks = new ArrayList<>();
        this.alarmSystem = alarmSystem;
    }

    public void registerLight(Light light) {
        lights.add(light);
    }

    public void registerAirConditioner(AirConditioner ac) {
        airConditioners.add(ac);
    }

    public void registerLock(SmartLock lock) {
        locks.add(lock);
    }

    // Métodos directos acoplados a Light
    public void turnOnLight(Light light) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando encender luz...");
        light.turnOn();
    }

    public void turnOffLight(Light light) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando apagar luz...");
        light.turnOff();
    }

    // Métodos directos acoplados a AirConditioner
    public void turnOnAirConditioner(AirConditioner ac) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando encender aire acondicionado...");
        ac.turnOn();
    }

    public void turnOffAirConditioner(AirConditioner ac) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando apagar aire acondicionado...");
        ac.turnOff();
    }

    public void setAirConditionerTemperature(AirConditioner ac, int temp) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando configurar temperatura a " + temp + "°C...");
        ac.setTemperature(temp);
    }

    // Métodos directos acoplados a SmartLock
    public void lockDoor(SmartLock lock) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando bloquear cerradura...");
        lock.lock();
    }

    public void unlockDoor(SmartLock lock) {
        System.out.println("[PanelControl - Sin Patrón] Solicitando desbloquear cerradura...");
        lock.unlock();
    }

    // Métodos directos acoplados a AlarmSystem
    public void activateAlarm() {
        System.out.println("[PanelControl - Sin Patrón] Solicitando activar alarma general...");
        if (alarmSystem != null) {
            alarmSystem.activate();
        }
    }

    public void deactivateAlarm() {
        System.out.println("[PanelControl - Sin Patrón] Solicitando desactivar alarma general...");
        if (alarmSystem != null) {
            alarmSystem.deactivate();
        }
    }

    /**
     * Modo Noche implementado directamente dentro del panel de control.
     * Hardcodea la secuencia de pasos con llamadas directas a las clases concretas:
     * 1. Apagar todas las luces.
     * 2. Ajustar aire acondicionado a 21°C.
     * 3. Cerrar todas las puertas.
     * 4. Activar sistema de alarma.
     * 
     * Limitación: No se puede parametrizar, reutilizar en otro invocador, ni deshacer de forma limpia.
     */
    public void executeNightMode(int targetTemperature) {
        System.out.println("\n>>> [PanelControl - Sin Patrón] EJECUTANDO MODO NOCHE DIRECTAMENTE <<<");
        
        System.out.println("Paso 1: Apagando todas las luces...");
        for (Light light : lights) {
            light.turnOff();
        }

        System.out.println("Paso 2: Ajustando aires acondicionados a " + targetTemperature + "°C...");
        for (AirConditioner ac : airConditioners) {
            ac.setTemperature(targetTemperature);
        }

        System.out.println("Paso 3: Asegurando todas las cerraduras electrónicas...");
        for (SmartLock lock : locks) {
            lock.lock();
        }

        System.out.println("Paso 4: Activando sistema de alarma...");
        if (alarmSystem != null) {
            alarmSystem.activate();
        }

        System.out.println(">>> [PanelControl - Sin Patrón] MODO NOCHE COMPLETADO <<<\n");
    }

    public void printCurrentDeviceStatus() {
        System.out.println("---------------- ESTADO ACTUAL DE DISPOSITIVOS (ANTES) ----------------");
        System.out.println("Luces:");
        for (Light l : lights) {
            System.out.println("  • " + l.getStatus());
        }
        System.out.println("Climatización:");
        for (AirConditioner ac : airConditioners) {
            System.out.println("  • " + ac.getStatus());
        }
        System.out.println("Cerraduras:");
        for (SmartLock lock : locks) {
            System.out.println("  • " + lock.getStatus());
        }
        System.out.println("Alarma:");
        if (alarmSystem != null) {
            System.out.println("  • " + alarmSystem.getStatus());
        }
        System.out.println("----------------------------------------------------------------------\n");
    }
}

