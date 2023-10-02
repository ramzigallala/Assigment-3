package org.project2;

import akka.actor.typed.ActorRef;
import org.project2.brushmanager.BrushManager;
import org.project2.brushmanager.BrushManagerProtocols;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class VisualiserPanel extends JPanel {
    private static final int STROKE_SIZE = 1;
    private final ActorRef<BrushManagerProtocols> brushManager;
    private final MessageProtocols.PixelGrid grid;
    private final int w,h;

    private List<Ellipse2D.Double> circle= new ArrayList<>();

    public VisualiserPanel(MessageProtocols.PixelGrid grid, ActorRef<BrushManagerProtocols> brushManager, int w, int h){
        setSize(w,h);
        this.grid = grid;
        this.w = w;
        this.h = h;
        this.brushManager = brushManager;
        this.setPreferredSize(new Dimension(w, h));
    }

    public void paint(Graphics g){
        System.out.println("ridisegno");
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.clearRect(0, 0, this.getWidth(), this.getHeight());

        int dx = w / grid.getNumColumns();
        int dy = h / grid.getNumRows();
        g2.setStroke(new BasicStroke(STROKE_SIZE));
        for (int i = 0; i < grid.getNumRows(); i++) {
            int y = i * dy;
            g2.drawLine(0, y, w, y);
        }

        for (int i = 0; i < grid.getNumColumns(); i++) {
            int x = i * dx;
            g2.drawLine(x, 0, x, h);
        }

        for (int row = 0; row < grid.getNumRows(); row++) {
            int y = row * dy;
            for (int column = 0; column < grid.getNumColumns(); column++) {
                int x = column * dx;
                int color = grid.get(column, row);
                if (color != 0) {
                    g2.setColor(new Color(color));
                    g2.fillRect(x + STROKE_SIZE, y + STROKE_SIZE, dx - STROKE_SIZE, dy - STROKE_SIZE);
                }
            }
        }
        if(!circle.isEmpty()){
            circle.forEach(c -> {
                g2.fill(c);
                g2.draw(c);
            });
        }


        //disegna il mouse
        brushManager.tell(new BrushManagerProtocols.DrawMsg(circle));
        System.out.println("finito ridisegno");
    }
}
