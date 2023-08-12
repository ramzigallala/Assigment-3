package GUI;

import akka.actor.AbstractActor;

import javax.swing.*;

public class ViewActor extends AbstractActor {
    private JTextArea distributionArea;
    private JTextArea maxFilesArea;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ViewActorProtocol.receivePanels.class, this::onBoot)
                .match(ViewActorProtocol.receiveResults.class, this::setResults)
                .build();
        }

    private void setResults(ViewActorProtocol.receiveResults msg) {
        this.distributionArea.setText(msg.getDistributionArea().toString().replace("{", " ").replace("}", "").replace(",", "\n").replace("=", "... = "));
        this.maxFilesArea.setText(msg.getMaxFilesArea().toString());
        System.out.println("Setting");
    }

    private void onBoot(ViewActorProtocol.receivePanels msg) {
        this.distributionArea = distributionArea;
        this.maxFilesArea = maxFilesArea;
    }

}