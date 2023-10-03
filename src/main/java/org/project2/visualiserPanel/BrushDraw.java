package org.project2.visualiserPanel;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BrushDraw {
    private final Ellipse2D.Double circle;
    private final Color color;
    private final Stroke stroke;

    public BrushDraw(Ellipse2D.Double circle, Color color, int stroke_size) {
        this.circle = circle;
        this.color = color;
        stroke = new BasicStroke(stroke_size);
    }

    public Ellipse2D.Double getCircle() {
        return circle;
    }

    public Color getColor() {
        return color;
    }

    public Stroke getStroke() {
        return stroke;
    }
}
