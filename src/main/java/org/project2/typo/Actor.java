package org.project2.typo;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import org.project2.typo.ActorProtocols.*;

public class Actor extends AbstractBehavior<CborSerializable> {
    public Actor(ActorContext<CborSerializable> context) {
        super(context);
    }


    @Override
    public Receive<CborSerializable> createReceive() {
        return newReceiveBuilder()
                .onMessage(SimpleMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<CborSerializable> onBootMsg(SimpleMsg msg) {
        System.out.println("Message arrived: "+java.time.LocalDateTime.now()+" Receiver: "+msg.getAddressReceiver());
        return this;
    }
    public static Behavior<CborSerializable> create(String name) {

        return Behaviors.setup(contex -> {
                                contex.getSystem()
                                        .receptionist()
                                        .tell(Receptionist.register(ServiceKey.create(CborSerializable.class,"simpleActor"),contex.getSelf()));
            System.out.println("Registered: "+name);
            return new Actor(contex).createReceive();
        });
    }
}
