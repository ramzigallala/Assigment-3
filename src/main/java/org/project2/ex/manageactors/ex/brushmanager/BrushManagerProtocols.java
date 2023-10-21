package org.project2.ex.manageactors.ex.brushmanager;

import org.project2.ex.manageactors.ex.visualiserPanel.BrushDraw;

import java.util.List;
import java.util.Optional;

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
        private final Optional<String> name;

        public AddBrushMsg(BrushInfo brush, Optional<String> name) {
            this.brush = brush;
            this.name = name;
        }

        public BrushInfo getBrush() {
            return brush;
        }

        public Optional<String> getName() {
            return name;
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
