package org.project2.ex.brushmanager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.project2.ex.visualiserPanel.BrushDraw;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.List;

public class BrushManager extends AbstractBehavior<BrushManagerProtocols> {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private BrushInfo localBrush;

    private HashMap<String, BrushInfo> outsideBrushes = new HashMap<>();
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
        List<BrushDraw> brushDraws = msg.getBrushDraws();
        brushDraws.clear();
        var localcolor = new Color(localBrush.getColor());
        var localcircle = new Ellipse2D.Double(localBrush.getX() - BRUSH_SIZE / 2.0, localBrush.getY() - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
        brushDraws.add(new BrushDraw(localcircle, localcolor, STROKE_SIZE));
        outsideBrushes.forEach((name, brush)->{
            var color = new Color(brush.getColor());
            var circle = new Ellipse2D.Double(brush.getX() - BRUSH_SIZE / 2.0, brush.getY() - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            brushDraws.add(new BrushDraw(circle, color, STROKE_SIZE));
        });


        return this;
    }



    private Behavior<BrushManagerProtocols> addMsg(BrushManagerProtocols.AddBrushMsg msg) {

        if(msg.getBrush().getRole().equals("local")){
            this.localBrush=msg.getBrush();
        }else{
            this.outsideBrushes.put(msg.getName().get(),msg.getBrush());

        }
        return this;
    }

    private Behavior<BrushManagerProtocols> removeMsg(BrushManagerProtocols.RemoveBrushMsg msg) {
        this.outsideBrushes.remove(msg.getBrush());
        return this;
    }

    public static Behavior<BrushManagerProtocols> create() {
        return Behaviors.setup(BrushManager::new);
    }


}
