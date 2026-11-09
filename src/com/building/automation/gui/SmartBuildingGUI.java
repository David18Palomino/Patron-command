package com.building.automation.gui;

import com.building.automation.after.command.*;
import com.building.automation.after.invoker.CommandHistory;
import com.building.automation.after.invoker.ControlPanel;
import com.building.automation.after.receiver.AirConditioner;
import com.building.automation.after.receiver.AlarmSystem;
import com.building.automation.after.receiver.Light;
import com.building.automation.after.receiver.SmartLock;
import com.building.automation.gui.components.ModernCardPanel;
import com.building.automation.gui.components.ToggleSwitch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interfaz Gráfica interactiva moderna para el Sistema Inteligente de Automatización de Edificios.
 * Diseñada para simular un panel táctil de domótica de alta gama (Dark Mode / Glassmorphism)
 * demostrando visualmente y en tiempo real el funcionamiento del Patrón de Diseño Command.
 */
public class SmartBuildingGUI extends JFrame {

    // Receptores (Receivers)
    private final Light livingRoomLight = new Light("Salón Principal");
    private final Light kitchenLight = new Light("Cocina");
    private final Light hallwayLight = new Light("Pasillo Principal");
    private final Light outdoorLight = new Light("Exteriores / Jardín");

    private final AirConditioner centralAc = new AirConditioner("Climatización Central", 24);
    private final SmartLock frontDoorLock = new SmartLock("Puerta Principal");
    private final SmartLock garageLock = new SmartLock("Acceso Garaje");
    private final AlarmSystem alarmSystem = new AlarmSystem("Alarma Perimetral");

    private final List<Light> allLights = Arrays.asList(livingRoomLight, kitchenLight, hallwayLight, outdoorLight);
    private final List<SmartLock> allLocks = Arrays.asList(frontDoorLock, garageLock);

    // Invocador (Invoker)
    private final ControlPanel controlPanel = new ControlPanel();

    // Componentes visuales interactivos
    private final List<ToggleSwitch> lightSwitches = new ArrayList<>();
    private final List<JLabel> lightStatusLabels = new ArrayList<>();

    private JLabel acTempLabel;
    private JLabel acStatusLabel;
    private JLabel frontLockStatusLabel;
    private JLabel garageLockStatusLabel;
    private JButton frontLockBtn;
    private JButton garageLockBtn;

    private JLabel alarmStatusLabel;
    private JButton armAlarmBtn;
    private JButton disarmAlarmBtn;

    private DefaultListModel<String> historyListModel;
    private JTextArea consoleArea;
    private JLabel undoStatusLabel;

    public SmartBuildingGUI() {
        setTitle("Edificio Inteligente - Panel Central (Patrón Command)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 860);
        setMinimumSize(new Dimension(1080, 750));
        setLocationRelativeTo(null);

        // Estilos y look general
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        initUI();
        redirectSystemStreams();
        updateAllVisuals();
    }

    private void initUI() {
        // Contenedor principal con gradiente oscuro
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(13, 17, 30),
                        getWidth(), getHeight(), new Color(26, 20, 48)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainContainer.setLayout(new BorderLayout(16, 16));
        mainContainer.setBorder(new EmptyBorder(18, 22, 18, 22));

        // 1. Barra superior (Header)
        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Panel central: Cuadrícula 2x2 de dispositivos (Clima, Iluminación, Cerraduras, Alarma)
        JPanel centerGrid = new JPanel(new GridLayout(2, 2, 16, 16));
        centerGrid.setOpaque(false);
        centerGrid.add(createClimateCard());
        centerGrid.add(createLightingCard());
        centerGrid.add(createSecurityLocksCard());
        centerGrid.add(createAlarmCard());

        // 3. Panel inferior: Historial de Comandos + Consola del Patrón Command
        JPanel bottomPanel = createBottomAuditPanel();

        // Panel divisor vertical para center y bottom
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerGrid, bottomPanel);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.66);
        splitPane.setDividerSize(6);

        mainContainer.add(splitPane, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    /**
     * Barra superior con presencia de usuarios, botones de acción global (Modo Noche, Undo, etc.)
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(16, 10));
        header.setOpaque(false);

        // Lado izquierdo: Título y píldoras de estado
        JPanel leftTitle = new JPanel();
        leftTitle.setLayout(new BoxLayout(leftTitle, BoxLayout.Y_AXIS));
        leftTitle.setOpaque(false);

        JLabel titleLbl = new JLabel("SISTEMA DE AUTOMATIZACIÓN DE EDIFICIOS");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(Color.WHITE);

        JLabel subtitleLbl = new JLabel("Arquitectura desacoplada mediante el Patrón GoF Command (Invoker -> Command -> Receiver)");
        subtitleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLbl.setForeground(new Color(148, 163, 184));

        leftTitle.add(titleLbl);
        leftTitle.add(Box.createVerticalStrut(4));
        leftTitle.add(subtitleLbl);

        // Presencia rápida estilo Image 2 ("Andrew Home", "Denise Home", etc.)
        JPanel pillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pillsPanel.setOpaque(false);
        pillsPanel.add(createPill("👤 Andrew (Admin)", new Color(30, 41, 59), new Color(56, 189, 248)));
        pillsPanel.add(createPill("👤 Denise (Gerencia)", new Color(30, 41, 59), new Color(74, 222, 128)));
        pillsPanel.add(createPill("🛡️ Sistema Seguro", new Color(30, 41, 59), new Color(168, 85, 247)));
        leftTitle.add(Box.createVerticalStrut(6));
        leftTitle.add(pillsPanel);

        // Lado derecho: Botones destacados de acción (Modo Noche, Undo, Info)
        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actionBtns.setOpaque(false);

        JButton nightModeBtn = createStyledButton("🌙 MODO NOCHE (Macro)", new Color(124, 58, 237), new Color(167, 139, 250));
        nightModeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nightModeBtn.setToolTipText("Ejecuta MacroCommand: apaga luces, clima a 21°C, asegura cerraduras y activa alarma");
        nightModeBtn.addActionListener(e -> executeNightModeMacro());

        JButton undoBtn = createStyledButton("↩️ DESHACER (Undo)", new Color(14, 116, 144), new Color(56, 189, 248));
        undoBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        undoBtn.setToolTipText("Revierte la última operación del historial mediante command.undo()");
        undoBtn.addActionListener(e -> performUndo());

        JButton historyBtn = createStyledButton("📋 Historial", new Color(51, 65, 85), Color.WHITE);
        historyBtn.addActionListener(e -> showHistoryDialog());

        JButton compareBtn = createStyledButton("⚖️ Comparar Arquitectura", new Color(30, 41, 59), new Color(226, 232, 240));
        compareBtn.addActionListener(e -> showArchitectureComparisonDialog());

        actionBtns.add(nightModeBtn);
        actionBtns.add(undoBtn);
        actionBtns.add(historyBtn);
        actionBtns.add(compareBtn);

        header.add(leftTitle, BorderLayout.WEST);
        header.add(actionBtns, BorderLayout.EAST);

        return header;
    }

    private JLabel createPill(String text, Color bg, Color fg) {
        JLabel pill = new JLabel(text);
        pill.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pill.setForeground(fg);
        pill.setBackground(bg);
        pill.setOpaque(true);
        pill.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fg.darker(), 1, true),
                new EmptyBorder(3, 10, 3, 10)
        ));
        return pill;
    }

    /**
     * 1. Tarjeta Climatización (AirConditioner)
     * Estilo dial / termostato con lectura de temperatura grande y controles +/-
     */
    private ModernCardPanel createClimateCard() {
        ModernCardPanel card = new ModernCardPanel(18, new Color(20, 26, 43), new Color(38, 50, 78));
        card.setLayout(new BorderLayout(14, 14));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Título de la tarjeta
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        JLabel title = new JLabel("❄️ CLIMATIZACIÓN (Aire Acondicionado)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        acStatusLabel = new JLabel("COOLING ACTIVO");
        acStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        acStatusLabel.setForeground(new Color(56, 189, 248));
        titleBar.add(acStatusLabel, BorderLayout.EAST);
        card.add(titleBar, BorderLayout.NORTH);

        // Centro: Lectura grande de temperatura
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        acTempLabel = new JLabel("24°C");
        acTempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        acTempLabel.setFont(new Font("Segoe UI", Font.BOLD, 54));
        acTempLabel.setForeground(Color.WHITE);

        JLabel subLabel = new JLabel("Zona Central y Oficinas • Termostato Inteligente");
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(148, 163, 184));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(acTempLabel);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(subLabel);
        centerPanel.add(Box.createVerticalGlue());

        card.add(centerPanel, BorderLayout.CENTER);

        // Controles de ajuste (+ / -) y presets
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        controlsPanel.setOpaque(false);

        JButton btnDown = createStyledButton("▼ -1°C", new Color(30, 41, 59), Color.WHITE);
        btnDown.addActionListener(e -> {
            int newTemp = centralAc.getTemperature() - 1;
            controlPanel.executeCommand(new SetTemperatureCommand(centralAc, newTemp));
            updateAllVisuals();
        });

        JButton btnUp = createStyledButton("▲ +1°C", new Color(30, 41, 59), Color.WHITE);
        btnUp.addActionListener(e -> {
            int newTemp = centralAc.getTemperature() + 1;
            controlPanel.executeCommand(new SetTemperatureCommand(centralAc, newTemp));
            updateAllVisuals();
        });

        JButton btnPreset21 = createStyledButton("21°C (Noche)", new Color(76, 29, 149), new Color(196, 181, 253));
        btnPreset21.addActionListener(e -> {
            controlPanel.executeCommand(new SetTemperatureCommand(centralAc, 21));
            updateAllVisuals();
        });

        JButton btnPreset24 = createStyledButton("24°C (Confort)", new Color(15, 76, 129), new Color(125, 211, 252));
        btnPreset24.addActionListener(e -> {
            controlPanel.executeCommand(new SetTemperatureCommand(centralAc, 24));
            updateAllVisuals();
        });

        controlsPanel.add(btnDown);
        controlsPanel.add(btnUp);
        controlsPanel.add(btnPreset21);
        controlsPanel.add(btnPreset24);

        card.add(controlsPanel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * 2. Tarjeta Iluminación (Lighting)
     * 4 zonas con conmutadores deslizantes e iconos brillantes
     */
    private ModernCardPanel createLightingCard() {
        ModernCardPanel card = new ModernCardPanel(18, new Color(20, 26, 43), new Color(38, 50, 78));
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        JLabel title = new JLabel("💡 ILUMINACIÓN INTELIGENTE (Luces)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Control de 4 Zonas");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(148, 163, 184));
        titleBar.add(subtitle, BorderLayout.EAST);
        card.add(titleBar, BorderLayout.NORTH);

        // Lista de 4 luces
        JPanel lightsListPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        lightsListPanel.setOpaque(false);

        for (int i = 0; i < allLights.size(); i++) {
            Light light = allLights.get(i);
            JPanel row = new JPanel(new BorderLayout(12, 0));
            row.setOpaque(false);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 41, 59)),
                    new EmptyBorder(4, 6, 4, 6)
            ));

            JLabel nameLbl = new JLabel("• " + light.getLocation());
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            nameLbl.setForeground(new Color(226, 232, 240));

            JLabel statusLbl = new JLabel("APAGADA");
            statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            statusLbl.setForeground(new Color(148, 163, 184));
            lightStatusLabels.add(statusLbl);

            JPanel labelGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            labelGroup.setOpaque(false);
            labelGroup.add(nameLbl);
            labelGroup.add(statusLbl);

            ToggleSwitch toggle = new ToggleSwitch();
            final int lightIndex = i;
            toggle.addChangeListener(selected -> {
                // Despacha comando a través del ControlPanel
                Command cmd = new TurnOnLightCommand(light, selected);
                controlPanel.executeCommand(cmd);
                updateAllVisuals();
            });
            lightSwitches.add(toggle);

            row.add(labelGroup, BorderLayout.WEST);
            row.add(toggle, BorderLayout.EAST);
            lightsListPanel.add(row);
        }

        card.add(lightsListPanel, BorderLayout.CENTER);

        // Botones rápidos: Encender todas / Apagar todas
        JPanel bottomBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        bottomBtns.setOpaque(false);

        JButton allOnBtn = createStyledButton("Encender Todas", new Color(30, 41, 59), Color.WHITE);
        allOnBtn.addActionListener(e -> {
            MacroCommand allOnMacro = new MacroCommand("Encender Todas las Luces");
            for (Light l : allLights) {
                allOnMacro.addCommand(new TurnOnLightCommand(l, true));
            }
            controlPanel.executeCommand(allOnMacro);
            updateAllVisuals();
        });

        JButton allOffBtn = createStyledButton("Apagar Todas", new Color(30, 41, 59), Color.WHITE);
        allOffBtn.addActionListener(e -> {
            MacroCommand allOffMacro = new MacroCommand("Apagar Todas las Luces");
            for (Light l : allLights) {
                allOffMacro.addCommand(new TurnOffLightCommand(l));
            }
            controlPanel.executeCommand(allOffMacro);
            updateAllVisuals();
        });

        bottomBtns.add(allOnBtn);
        bottomBtns.add(allOffBtn);
        card.add(bottomBtns, BorderLayout.SOUTH);

        return card;
    }

    /**
     * 3. Tarjeta Seguridad y Cerraduras (SmartLock)
     */
    private ModernCardPanel createSecurityLocksCard() {
        ModernCardPanel card = new ModernCardPanel(18, new Color(20, 26, 43), new Color(38, 50, 78));
        card.setLayout(new BorderLayout(14, 14));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        JLabel title = new JLabel("🔒 SEGURIDAD (Cerraduras Electrónicas)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Control Perimetral");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(148, 163, 184));
        titleBar.add(subtitle, BorderLayout.EAST);
        card.add(titleBar, BorderLayout.NORTH);

        // Lista de cerraduras
        JPanel locksPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        locksPanel.setOpaque(false);

        // Puerta Principal
        JPanel frontPanel = new JPanel(new BorderLayout(10, 10));
        frontPanel.setOpaque(false);
        frontPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 54, 82), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        JLabel frontTitle = new JLabel("🚪 " + frontDoorLock.getDoorName());
        frontTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        frontTitle.setForeground(Color.WHITE);

        frontLockStatusLabel = new JLabel("BLOQUEADA");
        frontLockStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        frontLockStatusLabel.setForeground(new Color(74, 222, 128));

        JPanel frontLabels = new JPanel(new GridLayout(2, 1, 2, 2));
        frontLabels.setOpaque(false);
        frontLabels.add(frontTitle);
        frontLabels.add(frontLockStatusLabel);

        frontLockBtn = createStyledButton("Desbloquear", new Color(30, 41, 59), Color.WHITE);
        frontLockBtn.addActionListener(e -> {
            boolean willLock = !frontDoorLock.isLocked();
            Command cmd = willLock ? new LockDoorCommand(frontDoorLock) : new UnlockDoorCommand(frontDoorLock);
            controlPanel.executeCommand(cmd);
            updateAllVisuals();
        });

        frontPanel.add(frontLabels, BorderLayout.WEST);
        frontPanel.add(frontLockBtn, BorderLayout.EAST);
        locksPanel.add(frontPanel);

        // Acceso Garaje
        JPanel garagePanel = new JPanel(new BorderLayout(10, 10));
        garagePanel.setOpaque(false);
        garagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 54, 82), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        JLabel garageTitle = new JLabel("🚘 " + garageLock.getDoorName());
        garageTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        garageTitle.setForeground(Color.WHITE);

        garageLockStatusLabel = new JLabel("BLOQUEADA");
        garageLockStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        garageLockStatusLabel.setForeground(new Color(74, 222, 128));

        JPanel garageLabels = new JPanel(new GridLayout(2, 1, 2, 2));
        garageLabels.setOpaque(false);
        garageLabels.add(garageTitle);
        garageLabels.add(garageLockStatusLabel);

        garageLockBtn = createStyledButton("Desbloquear", new Color(30, 41, 59), Color.WHITE);
        garageLockBtn.addActionListener(e -> {
            boolean willLock = !garageLock.isLocked();
            Command cmd = willLock ? new LockDoorCommand(garageLock) : new UnlockDoorCommand(garageLock);
            controlPanel.executeCommand(cmd);
            updateAllVisuals();
        });

        garagePanel.add(garageLabels, BorderLayout.WEST);
        garagePanel.add(garageLockBtn, BorderLayout.EAST);
        locksPanel.add(garagePanel);

        card.add(locksPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * 4. Tarjeta Sistema de Alarma (AlarmSystem)
     */
    private ModernCardPanel createAlarmCard() {
        ModernCardPanel card = new ModernCardPanel(18, new Color(20, 26, 43), new Color(38, 50, 78));
        card.setLayout(new BorderLayout(14, 14));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        JLabel title = new JLabel("🚨 SISTEMA DE ALARMAS (Perímetro)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);
        titleBar.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Central de Seguridad");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(148, 163, 184));
        titleBar.add(subtitle, BorderLayout.EAST);
        card.add(titleBar, BorderLayout.NORTH);

        // Centro: Estado grande
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        alarmStatusLabel = new JLabel("DESARMADA / INACTIVA");
        alarmStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        alarmStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        alarmStatusLabel.setForeground(new Color(148, 163, 184));

        JLabel details = new JLabel("Sensores perimetrales y volumétricos en modo espera");
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        details.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        details.setForeground(new Color(100, 116, 139));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(alarmStatusLabel);
        centerPanel.add(Box.createVerticalStrut(6));
        centerPanel.add(details);
        centerPanel.add(Box.createVerticalGlue());

        card.add(centerPanel, BorderLayout.CENTER);

        // Botones de control de alarma (Armar / Desarmar / Arm Night)
        JPanel btnsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        btnsPanel.setOpaque(false);

        armAlarmBtn = createStyledButton("🔴 ARMAR ALARMA", new Color(185, 28, 28), Color.WHITE);
        armAlarmBtn.addActionListener(e -> {
            controlPanel.executeCommand(new ActivateAlarmCommand(alarmSystem));
            updateAllVisuals();
        });

        disarmAlarmBtn = createStyledButton("🟢 DESARMAR", new Color(21, 128, 61), Color.WHITE);
        disarmAlarmBtn.addActionListener(e -> {
            controlPanel.executeCommand(new DeactivateAlarmCommand(alarmSystem));
            updateAllVisuals();
        });

        JButton armNightBtn = createStyledButton("🌙 ARM NIGHT", new Color(124, 58, 237), Color.WHITE);
        armNightBtn.addActionListener(e -> executeNightModeMacro());

        btnsPanel.add(disarmAlarmBtn);
        btnsPanel.add(armAlarmBtn);
        btnsPanel.add(armNightBtn);

        card.add(btnsPanel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * 5. Panel inferior: Inspector de Historial y Consola en vivo
     */
    private JPanel createBottomAuditPanel() {
        JPanel bottomContainer = new JPanel(new GridLayout(1, 2, 16, 0));
        bottomContainer.setOpaque(false);

        // Lado izquierdo: Historial de comandos (Undo Stack)
        ModernCardPanel historyCard = new ModernCardPanel(16, new Color(16, 21, 36), new Color(30, 41, 59));
        historyCard.setLayout(new BorderLayout(8, 8));
        historyCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel histHeader = new JPanel(new BorderLayout());
        histHeader.setOpaque(false);
        JLabel histTitle = new JLabel("📚 Historial de Comandos & Pila Undo (LIFO)");
        histTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        histTitle.setForeground(new Color(226, 232, 240));
        histHeader.add(histTitle, BorderLayout.WEST);

        undoStatusLabel = new JLabel("Pila: 0 operaciones");
        undoStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        undoStatusLabel.setForeground(new Color(148, 163, 184));
        histHeader.add(undoStatusLabel, BorderLayout.EAST);
        historyCard.add(histHeader, BorderLayout.NORTH);

        historyListModel = new DefaultListModel<>();
        JList<String> historyJList = new JList<>(historyListModel);
        historyJList.setBackground(new Color(22, 28, 48));
        historyJList.setForeground(new Color(203, 213, 225));
        historyJList.setFont(new Font("Consolas", Font.PLAIN, 12));
        historyJList.setSelectionBackground(new Color(51, 65, 85));
        JScrollPane histScroll = new JScrollPane(historyJList);
        histScroll.setBorder(BorderFactory.createLineBorder(new Color(42, 54, 82), 1));
        historyCard.add(histScroll, BorderLayout.CENTER);

        bottomContainer.add(historyCard);

        // Lado derecho: Consola de ejecución de la arquitectura
        ModernCardPanel consoleCard = new ModernCardPanel(16, new Color(16, 21, 36), new Color(30, 41, 59));
        consoleCard.setLayout(new BorderLayout(8, 8));
        consoleCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel conHeader = new JPanel(new BorderLayout());
        conHeader.setOpaque(false);
        JLabel conTitle = new JLabel("🖥️ Traza de Ejecución Arquitectónica (Consola)");
        conTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        conTitle.setForeground(new Color(226, 232, 240));
        conHeader.add(conTitle, BorderLayout.WEST);

        JButton clearBtn = createStyledButton("Limpiar", new Color(30, 41, 59), new Color(148, 163, 184));
        clearBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        clearBtn.addActionListener(e -> consoleArea.setText(""));
        conHeader.add(clearBtn, BorderLayout.EAST);
        consoleCard.add(conHeader, BorderLayout.NORTH);

        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(new Color(11, 15, 26));
        consoleArea.setForeground(new Color(134, 239, 172)); // Verde terminal
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        consoleArea.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        consoleScroll.setBorder(BorderFactory.createLineBorder(new Color(42, 54, 82), 1));
        consoleCard.add(consoleScroll, BorderLayout.CENTER);

        bottomContainer.add(consoleCard);

        return bottomContainer;
    }

    /**
     * Ejecuta el Modo Noche encapsulado en MacroCommand
     */
    private void executeNightModeMacro() {
        MacroCommand modoNoche = new MacroCommand("Modo Noche Edificio", Arrays.asList(
                new TurnOffLightCommand(livingRoomLight),
                new TurnOffLightCommand(kitchenLight),
                new TurnOffLightCommand(hallwayLight),
                new TurnOffLightCommand(outdoorLight),
                new SetTemperatureCommand(centralAc, 21),
                new LockDoorCommand(frontDoorLock),
                new LockDoorCommand(garageLock),
                new ActivateAlarmCommand(alarmSystem)
        ));

        controlPanel.executeCommand(modoNoche);
        updateAllVisuals();
    }

    /**
     * Deshace la última operación (Undo polimórfico)
     */
    private void performUndo() {
        boolean success = controlPanel.undoLastCommand();
        if (!success) {
            JOptionPane.showMessageDialog(this,
                    "No hay comandos registrados en el historial para deshacer.",
                    "Pila de Undo Vacía",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        updateAllVisuals();
    }

    /**
     * Sincroniza todos los componentes de la interfaz con el estado real de los Receivers
     */
    private void updateAllVisuals() {
        // 1. Clima
        acTempLabel.setText(centralAc.getTemperature() + "°C");
        if (centralAc.isOn()) {
            acStatusLabel.setText("COOLING ACTIVO (" + centralAc.getTemperature() + "°C)");
            acStatusLabel.setForeground(new Color(56, 189, 248));
        } else {
            acStatusLabel.setText("STANDBY / OFF");
            acStatusLabel.setForeground(new Color(148, 163, 184));
        }

        // 2. Luces
        for (int i = 0; i < allLights.size(); i++) {
            Light light = allLights.get(i);
            boolean on = light.isOn();
            lightSwitches.get(i).setSelected(on);
            JLabel statusLbl = lightStatusLabels.get(i);
            if (on) {
                statusLbl.setText("ENCENDIDA (ON)");
                statusLbl.setForeground(new Color(250, 204, 21)); // Amarillo cálido
            } else {
                statusLbl.setText("APAGADA (OFF)");
                statusLbl.setForeground(new Color(100, 116, 139));
            }
        }

        // 3. Cerraduras
        boolean frontLocked = frontDoorLock.isLocked();
        frontLockStatusLabel.setText(frontLocked ? "BLOQUEADA (SEGURA)" : "DESBLOQUEADA (ABIERTA)");
        frontLockStatusLabel.setForeground(frontLocked ? new Color(74, 222, 128) : new Color(248, 113, 113));
        frontLockBtn.setText(frontLocked ? "Desbloquear" : "Bloquear");

        boolean garageLocked = garageLock.isLocked();
        garageLockStatusLabel.setText(garageLocked ? "BLOQUEADA (SEGURA)" : "DESBLOQUEADA (ABIERTA)");
        garageLockStatusLabel.setForeground(garageLocked ? new Color(74, 222, 128) : new Color(248, 113, 113));
        garageLockBtn.setText(garageLocked ? "Desbloquear" : "Bloquear");

        // 4. Alarma
        boolean armed = alarmSystem.isArmed();
        if (armed) {
            alarmStatusLabel.setText("🔴 ARMADA / MODO ALERTA MÁXIMA");
            alarmStatusLabel.setForeground(new Color(248, 113, 113));
        } else {
            alarmStatusLabel.setText("🟢 DESARMADA / PERÍMETRO INACTIVO");
            alarmStatusLabel.setForeground(new Color(74, 222, 128));
        }

        // 5. Historial visual
        historyListModel.clear();
        CommandHistory history = controlPanel.getHistory();
        List<String> audit = history.getAuditTrail();
        for (int i = audit.size() - 1; i >= 0; i--) {
            historyListModel.addElement(audit.get(i));
        }
        undoStatusLabel.setText("Pila Undo: " + history.size() + " comando(s) reversibles");
    }

    /**
     * Muestra ventana modal con el historial detallado de comandos
     */
    private void showHistoryDialog() {
        JDialog dialog = new JDialog(this, "Auditoría de Comandos (CommandHistory)", true);
        dialog.setSize(680, 480);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(15, 23, 42));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Historial Completo de Operaciones y Auditoría");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(22, 30, 52));
        area.setForeground(new Color(226, 232, 240));
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setMargin(new Insets(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("=== TRAZA CRONOLÓGICA DE AUDITORÍA ===\n\n");
        for (String log : controlPanel.getHistory().getAuditTrail()) {
            sb.append("  ").append(log).append("\n");
        }
        sb.append("\n=== PILA ACTUAL DE DESHACER (LIFO UNDO STACK) ===\n");
        List<Command> stack = controlPanel.getHistory().getStackSnapshot();
        if (stack.isEmpty()) {
            sb.append("  (Pila vacía)\n");
        } else {
            int idx = 1;
            for (Command cmd : stack) {
                sb.append("  ").append(idx++).append(". ").append(cmd.getDescription()).append("\n");
            }
        }
        area.setText(sb.toString());

        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        JButton closeBtn = createStyledButton("Cerrar", new Color(51, 65, 85), Color.WHITE);
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.setOpaque(false);
        bp.add(closeBtn);
        panel.add(bp, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    /**
     * Muestra comparativa arquitectónica para sustentación
     */
    private void showArchitectureComparisonDialog() {
        JDialog dialog = new JDialog(this, "Comparativa Arquitectónica: ANTES vs DESPUÉS de Command", true);
        dialog.setSize(860, 560);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(new Color(15, 23, 42));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Análisis Académico: ¿Por qué aplicar el Patrón Command?");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setBackground(new Color(22, 30, 52));
        text.setForeground(new Color(226, 232, 240));
        text.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setMargin(new Insets(12, 14, 12, 14));

        text.setText(
                "1. ANTES DE APLICAR COMMAND (ALTO ACOPLAMIENTO):\n" +
                "  • El Invocador (ControlPanel) conoce DIRECTAMENTE a Light, AirConditioner, SmartLock y AlarmSystem.\n" +
                "  • Violación del Principio Abierto/Cerrado (OCP): Añadir un nuevo dispositivo implica modificar el panel.\n" +
                "  • Sin Deshacer (Undo): No hay forma polimórfica ni genérica de revertir acciones.\n" +
                "  • Sin Historial: Las operaciones se invocan y se pierden en el tiempo.\n" +
                "  • Modo Noche Rígido: Código 'spaghetti' hardcodeado con llamadas directas.\n\n" +
                "2. DESPUÉS DE APLICAR COMMAND (DESACOPLAMIENTO GOF):\n" +
                "  • El Invocador (ControlPanel) SÓLO conoce la interfaz Command (execute() y undo()).\n" +
                "  • Receptores (Receivers): Retienen la lógica del hardware pero no saben quién los llama.\n" +
                "  • Comandos Concretos: Encapsulan una solicitud como un objeto de primer nivel (First-class citizen).\n" +
                "  • CommandHistory: Registra la pila LIFO para permitir Undo en cualquier momento.\n" +
                "  • MacroCommand: Permite componer secuencias complejas (ej. Modo Noche) y revertirlas en orden INVERSO.\n" +
                "  • Extensibilidad Total: Nuevos comandos pueden añadirse sin tocar ControlPanel ni la GUI."
        );

        panel.add(new JScrollPane(text), BorderLayout.CENTER);

        JButton closeBtn = createStyledButton("Entendido", new Color(14, 116, 144), Color.WHITE);
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.setOpaque(false);
        bp.add(closeBtn);
        panel.add(bp, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.brighter(), 1, true),
                new EmptyBorder(7, 14, 7, 14)
        ));
        return btn;
    }

    /**
     * Redirige System.out hacia el JTextArea de la consola visual en tiempo real
     */
    private void redirectSystemStreams() {
        PrintStream originalOut = System.out;
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                originalOut.write(b);
                appendToConsole(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                originalOut.write(b, off, len);
                appendToConsole(new String(b, off, len));
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    private void appendToConsole(String text) {
        SwingUtilities.invokeLater(() -> {
            if (consoleArea != null) {
                consoleArea.append(text);
                consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartBuildingGUI gui = new SmartBuildingGUI();
            gui.setVisible(true);
        });
    }
}

