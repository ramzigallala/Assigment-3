package org.project2.brushmanager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Brush extends AbstractBehavior<BrushProtocols> {
    private BrushInfo brushInfo;
    public Brush(ActorContext<BrushProtocols> context) {
        super(context);
    }

    @Override
    public Receive<BrushProtocols> createReceive() {
        return newReceiveBuilder()
                .onMessage(BrushProtocols.BootMsg.class, this::bootMsg)
                .onMessage(BrushProtocols.UpdatePositionMsg.class, this::positionMsg)
                .onMessage(BrushProtocols.UpdateColorMsg.class, this::colorMsg)
                .build();
    }

    private Behavior<BrushProtocols> bootMsg(BrushProtocols.BootMsg msg) {
        brushInfo = msg.getBrushInfo();
        msg.getBrushManager().tell(new BrushManagerProtocols.AddBrushMsg(brushInfo, msg.getName()));

        return this;
    }

    private Behavior<BrushProtocols> positionMsg(BrushProtocols.UpdatePositionMsg msg) {
        brushInfo.setPosition(msg.getX(), msg.getY());
        //System.out.println(msg.getX()+" "+ msg.getY());
        return this;
    }

    private Behavior<BrushProtocols> colorMsg(BrushProtocols.UpdateColorMsg msg) {
        brushInfo.setColor(msg.getColor());
        return this;
    }

    public static Behavior<BrushProtocols> create() {
        return Behaviors.setup(Brush::new);
    }

}
