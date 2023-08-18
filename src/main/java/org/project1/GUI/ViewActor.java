package org.project1.GUI;

import akka.actor.AbstractActor;

import javax.swing.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewActor extends AbstractActor {
    private JTextArea distributionArea;
    private JTextArea maxFilesArea;
    private int maxLines;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ViewActorProtocol.receivePanels.class, this::onBoot)
                .match(ViewActorProtocol.receiveResults.class, this::setResults)
                .build();
        }

    private void setResults(ViewActorProtocol.receiveResults msg) {
        var buckets = msg.getDistributionArea();
        String results = IntStream.range(0, buckets.length)
                .mapToObj(i -> (maxLines - (i*(maxLines/ (buckets.length - 1)))) + "..."  + " : " + buckets[i])
                .collect(Collectors.joining("\n"));
        distributionArea.setText(results);
        maxFilesArea.setText(msg.getMaxFilesArea().stream().map(e -> e.lines() + ": " + e.name()).toList().toString().replace(",", "\n"));
    }

    private void onBoot(ViewActorProtocol.receivePanels msg) {
        this.distributionArea = msg.getDistributionArea();
        this.maxFilesArea = msg.getMaxFilesArea();
        this.maxLines = msg.getMaxLines();
    }

}