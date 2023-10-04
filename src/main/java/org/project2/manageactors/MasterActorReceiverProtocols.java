package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.project2.utility.CborSerializable;


public class MasterActorReceiverProtocols {
    public static class InfoMsg implements CborSerializable {
        private final ActorRef<CborSerializable> addressReceiver;
        @JsonCreator
        public InfoMsg(ActorRef<CborSerializable> addressReceiver) {
            this.addressReceiver = addressReceiver;
        }

        public ActorRef<CborSerializable> getAddressReceiver() {
            return this.addressReceiver;
        }
    }
}
