package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.brushmanager.BrushInfo;
import org.project2.brushmanager.BrushManagerProtocols;
import org.project2.pixelgridview.PixelGridView;
import org.project2.utility.CborSerializable;

public class MasterReceiverMsgProtocols {


    public static class BootMsg implements CborSerializable {
        private final ActorRef<BrushManagerProtocols> brushManager;
        private final PixelGridView pixelGridView;

        @JsonCreator
        public BootMsg(ActorRef<BrushManagerProtocols> brushManager, PixelGridView pixelGridView) {
            this.brushManager = brushManager;
            this.pixelGridView = pixelGridView;
        }



        public ActorRef<BrushManagerProtocols> getBrushManager() {
            return brushManager;
        }

        public PixelGridView getPixelGridView() {
            return pixelGridView;
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
}
