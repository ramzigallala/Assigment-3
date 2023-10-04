package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.brushmanager.BrushInfo;
import org.project2.utility.CborSerializable;

import java.util.Set;


public class MasterSenderMsgProtocols {
    public static class actorsMsg implements CborSerializable {
        private final Set<ActorRef<CborSerializable>> actors;


        @JsonCreator
        public actorsMsg(Set<ActorRef<CborSerializable>> actors) {
            this.actors = actors;
        }


        public Set<ActorRef<CborSerializable>> getActors() {
            return actors;
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
