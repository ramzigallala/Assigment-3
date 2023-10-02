package org.project2;

public interface MessageProtocols {
    public static class PixelGrid{
        private final int nRows;
        private final int nColumns;
        private final int[][] grid;

        public PixelGrid(int nRows, int nColumns) {
            this.nRows = nRows;
            this.nColumns = nColumns;
            grid = new int[nRows][nColumns];
        }
        public void set(final int x, final int y, final int color) {
            grid[y][x] = color;
        }

        public int get(int x, int y) {
            return grid[y][x];
        }

        public int getNumRows() {
            return this.nRows;
        }


        public int getNumColumns() {
            return this.nColumns;
        }
    }
}
