package GUI;

import org.project1.rank.FileEntry;

import javax.swing.*;
import java.util.Map;
import java.util.TreeSet;

public class ViewActorProtocol {

    public static class receiveResults {
        private final int[] distributionArea;
        private final TreeSet<FileEntry> maxFilesArea;

        public receiveResults(int[] distributionArea, TreeSet<FileEntry> maxFilesArea) {
            this.distributionArea = distributionArea;
            this.maxFilesArea = maxFilesArea;
        }

        public int[] getDistributionArea() {
            return distributionArea;
        }

        public TreeSet<FileEntry> getMaxFilesArea() {
            return maxFilesArea;
        }
    }

    public static class receivePanels{
        private final JTextArea distributionArea;
        private final JTextArea maxFilesArea;

        public receivePanels(JTextArea distributionArea, JTextArea maxFilesArea) {
            this.distributionArea = distributionArea;
            this.maxFilesArea = maxFilesArea;
        }

        public JTextArea getDistributionArea() {
            return distributionArea;
        }

        public JTextArea getMaxFilesArea() {
            return maxFilesArea;
        }
    }
}
