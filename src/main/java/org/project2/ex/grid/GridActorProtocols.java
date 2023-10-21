package org.project2.ex.grid;

public interface GridActorProtocols {
    public static class Boot implements GridActorProtocols {
        private final PixelGrid grid;

        public Boot(PixelGrid grid) {
            this.grid = grid;
        }

        public PixelGrid getGrid() {
            return grid;
        }
    }
    public static class UpdateGrid implements GridActorProtocols{
        private final PixelGrid grid;

        public UpdateGrid(PixelGrid grid) {
            this.grid = grid;
        }

        public PixelGrid getGrid() {
            return grid;
        }
    }

    public static class UpdateCell implements GridActorProtocols{
        private final int x,y,color;

        public UpdateCell(int x, int y, int color) {
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
