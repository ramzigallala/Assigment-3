package org.project2.brushmanager;

public interface BrushProtocols {
    public static class UpdatePositionMsg implements BrushProtocols {
        private final int x, y;

        public UpdatePositionMsg(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static class UpdateColorMsg implements BrushProtocols {
        private final int color;

        public UpdateColorMsg(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }
    public static class BootMsg implements BrushProtocols {
        private final int x, y;
        private final int color;


        public BootMsg(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getColor() {
            return color;
        }
    }
}
