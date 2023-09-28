package org.project2.brushmanager;

import java.awt.*;

public interface BrushManagerProtocols {

    public static class DrawMsg implements BrushManagerProtocols {
        private final Graphics2D graphics2D;

        public DrawMsg(Graphics2D graphics2D) {
            this.graphics2D = graphics2D;
        }

        public Graphics2D getGraphics2D() {
            return graphics2D;
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
