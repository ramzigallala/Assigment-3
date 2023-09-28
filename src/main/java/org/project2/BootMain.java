package org.project2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import org.project2.brushmanager.Brush;
import org.project2.brushmanager.BrushManager;
import org.project2.brushmanager.BrushManagerProtocols;
import org.project2.brushmanager.BrushProtocols;
import org.project2.typo.BootMainProtocols;

public class BootMain extends AbstractBehavior<BootMainProtocols.BootMsg> {
    public BootMain(ActorContext<BootMainProtocols.BootMsg> context) {
        super(context);
    }

    @Override
    public Receive<BootMainProtocols.BootMsg> createReceive() {
        return newReceiveBuilder()
                .onMessage(BootMainProtocols.BootMsg.class, this::onBootMsg)
                .build();
    }

    private Behavior<BootMainProtocols.BootMsg> onBootMsg(BootMainProtocols.BootMsg msg) {

        ActorRef<BrushManagerProtocols> brushManager = this.getContext().spawn(BrushManager.create(), "brushManager");
        ActorRef<BrushProtocols> brush1 = this.getContext().spawn(Brush.create(), "brush1");
        brush1.tell(new BrushProtocols.BootMsg(5,2,3, brushManager));


        return this;
    }
}

