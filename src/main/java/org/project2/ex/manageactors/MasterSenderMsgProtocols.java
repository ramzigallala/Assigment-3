package org.project2.ex.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.ex.brushmanager.BrushInfo;
import org.project2.ex.utility.CborSerializable;
import org.project2.ex.grid.PixelGrid;

import java.util.Set;


public class MasterSenderMsgProtocols {
    public static class actorsMsg implements CborSerializable {
        private final Set<ActorRef<CborSerializable>> masterReceivers;


        @JsonCreator
        public actorsMsg(Set<ActorRef<CborSerializable>> masterReceivers) {
            this.masterReceivers = masterReceivers;
        }


        public Set<ActorRef<CborSerializable>> getMasterReceivers() {
            return masterReceivers;
        }
    }




    public static class SendBrush implements CborSerializable {
        private final BrushInfo brushInfo;

        public SendBrush(BrushInfo brushInfo) {
            this.brushInfo = brushInfo;
        }

        public BrushInfo getBrushInfo() {
            return brushInfo;
        }
    }

    public static class SendGrid implements CborSerializable {
        private final PixelGrid grid;
        private final long time;

        public SendGrid(PixelGrid grid, long time) {
            this.grid = grid;
            this.time = time;
        }

        public PixelGrid getGrid() {
            return grid;
        }

        public long getTime() {
            return time;
        }
    }

}
