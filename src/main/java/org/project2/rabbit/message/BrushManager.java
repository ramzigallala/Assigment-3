package org.project2.rabbit.message;


import java.awt.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

public class BrushManager implements Serializable {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    class SerializableComparator implements Serializable, Comparator<Brush> {

        @Override
        public int compare(Brush o1, Brush o2) {
            return o1.getConsumerTag().hashCode();
        }
    }
    private TreeSet<Brush> brushes = new TreeSet<>(new SerializableComparator());

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

    public void addBrush(final Brush brush) {
        //brushes.add(brush);
        brushes.add(brush);

    }

    public void addBrushes(TreeSet<Brush> brushes){
        brushes.addAll(brushes);
    }

    public void removeBrush(final String consumerTag) {
        brushes.removeIf(brush -> brush.getConsumerTag().equals(consumerTag));
    }

    public void setBrushes(TreeSet<Brush> brushes) {
        this.brushes = brushes;
    }

    public TreeSet<Brush> getBrushes() {
        return brushes;
    }

    public static class Brush implements Serializable{
        private int x, y;
        private int color;
        private final String consumerTag;

        public Brush(final int x, final int y, final int color, String consumerTag) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.consumerTag=consumerTag;
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

        public String getConsumerTag() {
            return consumerTag;
        }

        public void setColor(int color){
            this.color = color;
        }
    }
}
