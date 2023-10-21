package org.project2.rabbit;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class StatusManager implements Serializable {
    private final PixelGrid grid;
    private final Serializable brushManager;



    public StatusManager(PixelGrid grid, Serializable brushManager) {
        this.grid = grid;

        this.brushManager = brushManager;
    }

    public byte[] getStatus(){
        byte[] data = SerializationUtils.serialize(brushManager);



        return data;
    }

}
