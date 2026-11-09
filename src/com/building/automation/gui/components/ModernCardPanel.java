package com.building.automation.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel con esquinas redondeadas, fondo oscuro y borde sutil,
 * simulando las tarjetas de control táctiles de domótica moderna.
 */
public class ModernCardPanel extends JPanel {
    private final int cornerRadius;
    private Color backgroundColor = new Color(24, 30, 48);
    private Color borderColor = new Color(42, 54, 82);

    public ModernCardPanel() {
        this(16);
    }

    public ModernCardPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    public ModernCardPanel(int radius, Color bg, Color border) {
        this.cornerRadius = radius;
        this.backgroundColor = bg;
        this.borderColor = border;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo redondeado
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Borde elegante
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
    }

    public void setCardBackground(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }
}

