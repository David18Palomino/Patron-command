package com.building.automation;

import com.building.automation.after.MainAfter;
import com.building.automation.before.MainBefore;
import com.building.automation.gui.SmartBuildingGUI;

import javax.swing.SwingUtilities;
import java.util.Scanner;

/**
 * Lanzador unificado del proyecto para facilitar la ejecución y sustentación universitaria.
 * Permite seleccionar interactivamente qué módulo ejecutar:
 *  1. Consola ANTES de aplicar Command (Alto acoplamiento)
 *  2. Consola DESPUÉS de aplicar Command (Patrón GoF Command completo + Undo + Modo Noche)
 *  3. Interfaz Gráfica interactiva moderna (Dashboard táctil de domótica)
 */
public class MainLauncher {

    public static void main(String[] args) {
        if (args.length > 0) {
            String mode = args[0].trim().toLowerCase();
            switch (mode) {
                case "before":
                case "1":
                case "sin-patron":
                    MainBefore.main(args);
                    return;
                case "after":
                case "2":
                case "con-patron":
                    MainAfter.main(args);
                    return;
                case "gui":
                case "3":
                case "visual":
                    launchGUI();
                    return;
            }
        }

        System.out.println("================================================================================");
        System.out.println("   SISTEMA INTELIGENTE DE AUTOMATIZACIÓN DE EDIFICIOS - PATRÓN COMMAND");
        System.out.println("   Caso Práctico Universitario - Patrones de Diseño de Software");
        System.out.println("================================================================================");
        System.out.println("Seleccione el modo de ejecución que desea iniciar:\n");
        System.out.println("  [1] Ejecutar Versión ANTES de aplicar Command (Consola)");
        System.out.println("      -> Muestra el problema de alto acoplamiento e imposibilidad de Undo.\n");
        System.out.println("  [2] Ejecutar Versión DESPUÉS de aplicar Command (Consola)");
        System.out.println("      -> Demuestra el patrón GoF: Invocador, Comandos, Modo Noche y Undo LIFO.\n");
        System.out.println("  [3] Iniciar Interfaz Gráfica Moderna (GUI Dashboard)");
        System.out.println("      -> Panel táctil de domótica interactivo inspirado en las imágenes de diseño.\n");
        System.out.println("  [0] Salir");
        System.out.println("================================================================================");
        System.out.print("Ingrese una opción (1, 2, 3 o 0): ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "3";

        switch (input) {
            case "1":
                MainBefore.main(args);
                break;
            case "2":
                MainAfter.main(args);
                break;
            case "3":
                launchGUI();
                break;
            case "0":
                System.out.println("Saliendo del programa...");
                break;
            default:
                System.out.println("Opción no reconocida. Iniciando la Interfaz Gráfica por defecto...");
                launchGUI();
                break;
        }
    }

    private static void launchGUI() {
        System.out.println("\nIniciando la Interfaz Gráfica del Edificio Inteligente...");
        SwingUtilities.invokeLater(() -> {
            SmartBuildingGUI gui = new SmartBuildingGUI();
            gui.setVisible(true);
        });
    }
}

