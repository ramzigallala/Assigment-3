package org.project1.GUI;

import akka.actor.AbstractActor;

import javax.swing.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewActor extends AbstractActor {
    private Optional<JTextArea> distributionArea;
    private Optional<JTextArea> maxFilesArea;
    private int maxLines;
    private Optional<Long> numElements=Optional.empty();
    private long elementsElaborated=0L;
    private String resultsDistribution;
    private String resultMaxFiles;
    private boolean flagConsole=true;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ViewActorProtocol.receivePanels.class, this::onBoot)
                .match(ViewActorProtocol.receiveResults.class, this::setResults)
                .match(ViewActorProtocol.consoleInfo.class, this::setResultsConsole)
                .match(ViewActorProtocol.setNumElements.class, this::setNumElements)
                .build();
        }

    private void setNumElements(ViewActorProtocol.setNumElements msg) {

        numElements = Optional.of(msg.getNumElements());
        //System.out.println("viewActor numElements: "+numElements);
        //System.out.println("viewActor elementsElaborated: "+elementsElaborated);
        //caso in cui finisce prima che arrivi il numero totali di elementi
        printConsole();

    }

    private void setResultsConsole(ViewActorProtocol.consoleInfo msg) {
        maxLines=msg.getMaxLines();
        flagConsole=true;
    }

    private void setResults(ViewActorProtocol.receiveResults msg) {
        elementsElaborated= elementsElaborated+1;
        var buckets = msg.getBuckets();
        resultsDistribution = IntStream.range(0, buckets.length)
                .mapToObj(i -> (maxLines - (i*(maxLines/ (buckets.length - 1)))) + "..."  + " : " + buckets[i])
                .collect(Collectors.joining("\n"));
        resultMaxFiles = msg.getFiles().stream().map(e -> e.lines() + ": " + e.name()).toList().toString().replace(",", "\n");
        if(flagConsole){
            printConsole();
        }else {
            distributionArea.get().setText(resultsDistribution);
            maxFilesArea.get().setText(resultMaxFiles);
        }
    }

    private void printConsole() {
        if(numElements.isPresent() && elementsElaborated==numElements.get()){
            System.out.println(resultsDistribution);
            System.out.println(resultMaxFiles);
        }
    }

    private void onBoot(ViewActorProtocol.receivePanels msg) {
        this.distributionArea = Optional.of(msg.getDistributionArea());
        this.maxFilesArea = Optional.of(msg.getMaxFilesArea());
        this.maxLines = msg.getMaxLines();
        flagConsole=false;
    }

}