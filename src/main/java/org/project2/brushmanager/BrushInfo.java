package org.project2.brushmanager;

public class BrushInfo {
    private int x, y;
    private int color;
    private final String role;

    public BrushInfo(final int x, final int y, final int color, String role) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.role=role;
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
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setColor(int color){
        this.color=color;
    }

    public String getRole() {
        return role;
    }
}
