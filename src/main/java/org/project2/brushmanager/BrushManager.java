package org.project2.brushmanager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.project2.visualiserPanel.BrushDraw;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class BrushManager extends AbstractBehavior<BrushManagerProtocols> {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private BrushInfo localBrush;
    private List<BrushInfo> outsideBrushes = new java.util.ArrayList<>();
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
        outsideBrushes.forEach(brush -> {
            System.out.println("disegno outside");
            var color = new Color(brush.getColor());
            var circle = new Ellipse2D.Double(brush.getX() - BRUSH_SIZE / 2.0, brush.getY() - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            brushDraws.add(new BrushDraw(circle, color, STROKE_SIZE));
        });
        outsideBrushes.clear();

        return this;
    }



    private Behavior<BrushManagerProtocols> addMsg(BrushManagerProtocols.AddBrushMsg msg) {
        System.out.println("dentro add");
        if(msg.getBrush().getRole().equals("local")){
            this.localBrush=msg.getBrush();
        }else{
            this.outsideBrushes.add(msg.getBrush());
            System.out.println("aggiunto outside");
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
