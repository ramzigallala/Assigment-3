package org.project2.brushmanager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Brush extends AbstractBehavior<BrushProtocols> {
    public Brush(ActorContext<BrushProtocols> context) {
        super(context);
    }

    @Override
    public Receive<BrushProtocols> createReceive() {
        return newReceiveBuilder()
                .onMessage(BrushProtocols.UpdatePositionMsg.class, this::bootMsg)
                .onMessage(BrushProtocols.UpdatePositionMsg.class, this::positionMsg)
                .onMessage(BrushProtocols.UpdateColorMsg.class, this::colorMsg)
                .build();
    }

    private Behavior<BrushProtocols> positionMsg(BrushProtocols.UpdatePositionMsg msg) {

        return this;
    }

    private Behavior<BrushProtocols> colorMsg(BrushProtocols.UpdateColorMsg msg) {

        return this;
    }

    public static Behavior<BrushProtocols> create() {
        return Behaviors.setup(Brush::new);
    }

}
