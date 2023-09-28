package org.project2.typo;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

import java.util.Set;

public class Listener extends AbstractBehavior<Receptionist.Listing> {


    public Listener(ActorContext<Receptionist.Listing> context) {
        super(context);
    }

    @Override
    public Receive<Receptionist.Listing> createReceive() {
        return newReceiveBuilder()
                .onMessage(Receptionist.Listing.class, this::onMsg)
                .build();
    }

    private Behavior<Receptionist.Listing> onMsg(Receptionist.Listing msg) {

        //System.out.println("dentro listener "+ msg.toString());
        //ottengo gli attori
        Set<ActorRef<CborSerializable>> actors = msg.getServiceInstances(ServiceKey.create(CborSerializable.class,"simpleActor"));
        actors.forEach(actor -> {
            actor.tell(new ActorProtocols.SimpleMsg(actor));
            System.out.println(java.time.LocalDateTime.now()+" Message sent to actor: "+actor);
        });

        return Behaviors.same();
    }

    public static Behavior<Receptionist.Listing> create() {

        return Behaviors.setup(
                context -> {
                    context.getSystem()
                            .receptionist()
                            .tell(Receptionist.subscribe(
                                            ServiceKey.create(CborSerializable.class,"simpleActor"), context.getSelf()));

                    System.out.println("Ready");
                    return new Listener(context).createReceive();
                });
    }
}
