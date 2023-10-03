package org.project2.brushmanager;

import org.project2.visualiserPanel.BrushDraw;

import java.util.List;

public interface BrushManagerProtocols {

    public static class DrawMsg implements BrushManagerProtocols {
        private final List<BrushDraw> brushDraws;

        public DrawMsg(final List<BrushDraw> brushDraws) {
            this.brushDraws = brushDraws;
        }

        public List<BrushDraw> getBrushDraws() {
            return brushDraws;
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
