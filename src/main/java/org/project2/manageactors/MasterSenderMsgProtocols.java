package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.brushmanager.BrushInfo;
import org.project2.utility.CborSerializable;

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

}
