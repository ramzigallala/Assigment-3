package org.project2.brushmanager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class BrushManager extends AbstractBehavior<BrushManagerProtocols> {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private List<BrushInfo> brushes = new java.util.ArrayList<>();
    public BrushManager(ActorContext<BrushManagerProtocols> context) {
        super(context);
    }

    @Override
    public Receive<BrushManagerProtocols> createReceive() {
        return newReceiveBuilder()
                .onMessage(BrushManagerProtocols.RemoveBrushMsg.class, this::removeMsg)
                .onMessage(BrushManagerProtocols.AddBrushMsg.class, this::addMsg)
                .onMessage(BrushManagerProtocols.DrawMsg.class, this::drawMsg)
                .build();
    }

    private Behavior<BrushManagerProtocols> drawMsg(BrushManagerProtocols.DrawMsg msg) {
        Graphics2D g = msg.getGraphics2D();
        brushes.forEach(brush -> {
            g.setColor(new Color(brush.getColor()));
            var circle = new Ellipse2D.Double(brush.getX() - BRUSH_SIZE / 2.0, brush.getY() - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            // draw the polygon
            g.fill(circle);
            g.setStroke(new BasicStroke(STROKE_SIZE));
            g.setColor(Color.BLACK);
            g.draw(circle);
        });
        return this;
    }

    private Behavior<BrushManagerProtocols> addMsg(BrushManagerProtocols.AddBrushMsg msg) {
        this.brushes.add(msg.getBrush());
        return this;
    }

    private Behavior<BrushManagerProtocols> removeMsg(BrushManagerProtocols.RemoveBrushMsg msg) {
        this.brushes.remove(msg.getBrush());
        return this;
    }

    public static Behavior<BrushManagerProtocols> create() {
        return Behaviors.setup(BrushManager::new);
    }


}
