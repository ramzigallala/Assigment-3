package org.project2.manageactors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import org.project2.utility.CborSerializable;

public class MasterActorReceiver extends AbstractBehavior<CborSerializable> {
    public MasterActorReceiver(ActorContext<CborSerializable> context) {
        super(context);
    }

    @Override
    public Receive<CborSerializable> createReceive() {
        return newReceiveBuilder()
                .onMessage(MasterActorReceiverProtocols.InfoMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<CborSerializable> onBootMsg(MasterActorReceiverProtocols.InfoMsg msg) {
        System.out.println("Message arrived: "+java.time.LocalDateTime.now()+" Receiver: "+msg.getAddressReceiver());
        return this;
    }

    public static Behavior<CborSerializable> create(String name) {

        return Behaviors.setup(contex -> {
            contex.getSystem()
                    .receptionist()
                    .tell(Receptionist.register(ServiceKey.create(CborSerializable.class,"tunnel"),contex.getSelf()));
            System.out.println("Registered Actor: "+name);
            return new MasterActorReceiver(contex).createReceive();
        });
    }
}
