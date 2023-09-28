package org.project2.brushmanager;

public class BrushInfo {
    private final int x, y;
    private final int color;

    public BrushInfo(final int x, final int y, final int color) {
        this.x = x;
        this.y = y;
        this.color = color;
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
}
