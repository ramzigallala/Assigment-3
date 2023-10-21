package org.project2.rabbit;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class StatusManager implements Serializable {
    private final PixelGrid grid;
    private final BrushManager brushManager;



    public StatusManager(PixelGrid grid, BrushManager brushManager) {
        this.grid = grid;

        this.brushManager = brushManager;
    }

    public PixelGrid getGrid() {
        return grid;
    }

    public BrushManager getBrushManager() {
        return brushManager;
    }

    public byte[] getStatus(){
        byte[] data = SerializationUtils.serialize(brushManager);






        return SerializationUtils.serialize(this);
    }

}
