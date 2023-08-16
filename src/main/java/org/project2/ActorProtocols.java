package org.project2;

import akka.actor.typed.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;

public class ActorProtocols {
    public static class SimpleMsg implements CborSerializable{
        private final ActorRef<CborSerializable> addressReceiver;
        @JsonCreator
        public SimpleMsg(ActorRef<CborSerializable> addressReceiver) {
            this.addressReceiver = addressReceiver;
        }

        public ActorRef<CborSerializable> getAddressReceiver() {
            return this.addressReceiver;
        }
    }

}
