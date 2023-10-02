package org.project2.brushmanager;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public interface BrushManagerProtocols {

    public static class DrawMsg implements BrushManagerProtocols {
        private final List<Ellipse2D.Double> circle;

        public DrawMsg(final List<Ellipse2D.Double> circle) {
            this.circle = circle;
        }

        public List<Ellipse2D.Double> getCircle() {
            return circle;
        }
    }

    public static class AddBrushMsg implements BrushManagerProtocols {
        private final BrushInfo brush;

        public AddBrushMsg(BrushInfo brush) {
            this.brush = brush;
        }

        public BrushInfo getBrush() {
            return brush;
        }
    }

    public static class RemoveBrushMsg implements BrushManagerProtocols {
        private final Brush brush;
        public RemoveBrushMsg(Brush brush) {
            this.brush = brush;
        }

        public Brush getBrush() {
            return brush;
        }
    }
}
