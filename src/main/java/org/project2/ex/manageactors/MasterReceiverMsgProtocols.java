package org.project2.ex.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.ex.brushmanager.BrushInfo;
import org.project2.ex.brushmanager.BrushManagerProtocols;
import org.project2.ex.grid.GridActorProtocols;
import org.project2.ex.pixelgridview.PixelGridView;
import org.project2.ex.utility.CborSerializable;
import org.project2.ex.grid.PixelGrid;

public class MasterReceiverMsgProtocols {


    public static class BootMsg implements CborSerializable {
        private final ActorRef<BrushManagerProtocols> brushManager;
        private final PixelGridView pixelGridView;
        private final ActorRef<GridActorProtocols> gridActor;

        @JsonCreator
        public BootMsg(ActorRef<BrushManagerProtocols> brushManager, PixelGridView pixelGridView, ActorRef<GridActorProtocols> gridActor) {
            this.brushManager = brushManager;
            this.pixelGridView = pixelGridView;
            this.gridActor = gridActor;
        }



        public ActorRef<BrushManagerProtocols> getBrushManager() {
            return brushManager;
        }

        public PixelGridView getPixelGridView() {
            return pixelGridView;
        }

        public ActorRef<GridActorProtocols> getGridActor() {
            return gridActor;
        }
    }

    public static class SentBrush implements CborSerializable {
        private final BrushInfo brush;
        private final String name;
        @JsonCreator
        public SentBrush(BrushInfo brush, String name) {
            this.brush= brush;
            this.name = name;
        }

        public BrushInfo getBrush() {
            return brush;
        }

        public String getName() {
            return name;
        }
    }

    public static class SentGrid implements CborSerializable{
        private final PixelGrid grid;
        private final String name;
        private final long time;

        public SentGrid(PixelGrid grid, String name, long time) {
            this.grid = grid;
            this.name = name;
            this.time = time;
        }

        public PixelGrid getGrid() {
            return grid;
        }

        public String getName() {
            return name;
        }

        public long getTime() {
            return time;
        }
    }
}
