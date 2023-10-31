package org.project2.rabbit.message;

import org.apache.commons.lang3.SerializationUtils;
import org.project2.rabbit.message.BrushManager;
import org.project2.rabbit.message.PixelGrid;

import java.io.Serializable;

public class StatusManager implements Serializable {
    private final PixelGrid grid;
    private final BrushManager brushManager;
    private final boolean requestStart;

    public StatusManager() {
        this.grid = null;
        this.requestStart=true;
        this.brushManager = null;

    }

    public StatusManager(PixelGrid grid, BrushManager brushManager) {
        this.grid = grid;
        this.requestStart=false;
        this.brushManager = brushManager;
    }

    public PixelGrid getGrid() {
        return grid;
    }

    public BrushManager getBrushManager() {
        return brushManager;
    }

    public byte[] getStatus(){
        return SerializationUtils.serialize(this);
    }

    public boolean isRequestStart() {
        return requestStart;
    }
}
