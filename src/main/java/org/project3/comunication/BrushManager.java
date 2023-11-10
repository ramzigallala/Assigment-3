package org.project3.comunication;


import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class BrushManager implements Serializable {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private List<Brush> brushes = new java.util.ArrayList<>();

    public void draw(final Graphics2D g) {
        brushes.forEach(brush -> {
            g.setColor(new Color(brush.color));
            var circle = new java.awt.geom.Ellipse2D.Double(brush.x - BRUSH_SIZE / 2.0, brush.y - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            // draw the polygon
            g.fill(circle);
            g.setStroke(new BasicStroke(STROKE_SIZE));
            g.setColor(Color.BLACK);
            g.draw(circle);
        });
    }

    public List<Brush> getBrushes() {
        return brushes;
    }

    public void addBrush(final Brush brush) {
        brushes.add(brush);
    }

    public void removeBrush(final Brush brush) {
        brushes.remove(brush);
    }

    public static class Brush implements Serializable{
        private int x, y;
        private int color;
        private final int id;

        public Brush(final int x, final int y, final int color, int id) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.id = id;
        }

        public void updatePosition(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
        // write after this getter and setters
        public int getX(){
            return this.x;
        }
        public int getY(){
            return this.y;
        }
        public int getColor(){
            return this.color;
        }

        public int getId() {
            return id;
        }

        public void setColor(int color){
            this.color = color;
        }
    }
}
