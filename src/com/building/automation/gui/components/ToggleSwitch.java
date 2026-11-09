package com.building.automation.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interruptor táctil deslizante (Toggle Switch) estilo panel domótico.
 * Renderiza visualmente el estado ON/OFF con animación sutil y colores modernos.
 */
public class ToggleSwitch extends JComponent {
    private boolean selected = false;
    private final List<Consumer<Boolean>> listeners = new ArrayList<>();

    private final Color activeBg = new Color(56, 189, 248);     // Celeste / Neon
    private final Color inactiveBg = new Color(51, 65, 85);     // Gris pizarra
    private final Color knobColor = Color.WHITE;

    public ToggleSwitch() {
        setPreferredSize(new Dimension(52, 28));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEnabled()) {
                    setSelected(!selected);
                    for (Consumer<Boolean> listener : listeners) {
                        listener.accept(selected);
                    }
                }
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            repaint();
        }
    }

    public void addChangeListener(Consumer<Boolean> listener) {
        listeners.add(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = height;

        // Fondo del switch
        g2.setColor(selected ? activeBg : inactiveBg);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // Perilla (Knob)
        int knobSize = height - 6;
        int knobX = selected ? (width - knobSize - 3) : 3;
        int knobY = 3;

        g2.setColor(knobColor);
        g2.fillOval(knobX, knobY, knobSize, knobSize);

        g2.dispose();
    }
}

