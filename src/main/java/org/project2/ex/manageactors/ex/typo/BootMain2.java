package org.project2.ex.manageactors.ex.typo;

import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import org.project2.ex.typo.BootMainProtocols.BootMsg;

import java.time.Duration;


public class BootMain2 extends AbstractBehavior<BootMsg> {
    public BootMain2(ActorContext<BootMsg> context) {
        super(context);
    }

    @Override
    public Receive<BootMsg> createReceive() {
        return newReceiveBuilder()
                .onMessage(BootMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<BootMsg> onBootMsg(BootMsg msg) {
        ActorRef<CborSerializable> actor2 = this.getContext().spawn(Actor.create("actor2"), "actor2");
        ActorRef<CborSerializable> actor3 = this.getContext().spawn(Actor.create("actor3"), "actor3");
        //indirizzo del cluster a cui chiedere di entrare (Main1)
        Address seedNodes = AddressFromURIString.parse("akka://boot@127.0.0.1:2551");

        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.manager().tell(Join.create(seedNodes));
        getContext().getSystem().scheduler()
                .scheduleOnce(
                        Duration.ofMillis(5000),
                        new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("boot2 "+cluster.state());
                            }
                        },getContext().getExecutionContext());

        return this;
    }
}
