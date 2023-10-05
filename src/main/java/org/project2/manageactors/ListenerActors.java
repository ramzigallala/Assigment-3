package org.project2.manageactors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;
import org.project2.brushmanager.BrushInfo;
import org.project2.utility.CborSerializable;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ListenerActors extends AbstractBehavior<Receptionist.Listing> {
    ActorRef<CborSerializable> masterSenderMsg=null;
    public ListenerActors(ActorContext<Receptionist.Listing> context) {
        super(context);
    }

    @Override
    public Receive<Receptionist.Listing> createReceive() {
        return newReceiveBuilder()
                .onMessage(Receptionist.Listing.class, this::onMsg)
                .build();
    }

    private Behavior<Receptionist.Listing> onMsg(Receptionist.Listing msg) {

        //ottengo gli attori
        Set<ActorRef<CborSerializable>> actors = msg.getServiceInstances(ServiceKey.create(CborSerializable.class,"tunnel"));
        //System.out.println("dentro listener "+ actors);
        Set<ActorRef<CborSerializable>> masterActors = new HashSet<>();
        Set<ActorRef<CborSerializable>> brushesInfo = new HashSet<>();

        actors.forEach(actor -> {
            //System.out.println("listener "+ actor.path());
            if(actor.path().name().contains("masterSender")){
                masterActors.add(actor);
            }else{
                brushesInfo.add(actor);
            }

        });
        System.out.println("listener, masterSender "+ masterActors.size()+" brushes "+brushesInfo.size());


        masterActors.forEach(actor -> {
            //System.out.println("listener, masterSender "+ actor.path());

            actor.tell(new MasterSenderMsgProtocols.actorsMsg(brushesInfo));
        });


        return Behaviors.same();
    }

    public static Behavior<Receptionist.Listing> create() {

        return Behaviors.setup(
                context -> {
                    context.getSystem()
                            .receptionist()
                            .tell(Receptionist.subscribe(
                                    ServiceKey.create(CborSerializable.class,"tunnel"), context.getSelf()));

                    System.out.println("Ready");
                    return new ListenerActors(context).createReceive();
                });
    }
}
