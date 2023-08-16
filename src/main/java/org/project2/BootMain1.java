package org.project2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Join;
import org.project2.BootMainProtocols.*;

public class BootMain1 extends AbstractBehavior<BootMsg> {
    public BootMain1(ActorContext<BootMsg> context) {
        super(context);
    }

    @Override
    public Receive<BootMsg> createReceive() {
        return newReceiveBuilder()
                .onMessage(BootMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<BootMsg> onBootMsg(BootMsg msg) {
        //definisco un attore per il receptionist
        ActorRef<Receptionist.Listing> listener = this.getContext().spawn(Listener.create(), "listener");

        //accedo al cluster
        Cluster cluster = Cluster.get(getContext().getSystem());
        cluster.manager().tell(Join.create(cluster.selfMember().address()));

        return this;
    }
}
/* //Se vuoi fare prove usando un timer
getContext().getSystem().scheduler()
                .scheduleOnce(
                        Duration.ofMillis(5000),
                        new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("boot2 "+cluster.state());
                            }
                        },getContext().getExecutionContext());
 */
