package org.project1.GUI;

import org.project1.rank.FileEntry;

import javax.swing.*;
import java.util.List;

public class ViewActorProtocol {

    public static class receiveResults {
        private final int[] buckets;
        private final List<FileEntry> files;

        public receiveResults(int[] distributionArea, List<FileEntry> maxFilesArea) {
            this.buckets = distributionArea;
            this.files = maxFilesArea;
        }

        public int[] getBuckets() {
            return buckets;
        }

        public List<FileEntry> getFiles() {
            return files;
        }
    }

    public static class receivePanels{
        private final JTextArea distributionArea;
        private final JTextArea maxFilesArea;
        private final int maxLines;

        public receivePanels(JTextArea distributionArea, JTextArea maxFilesArea, int maxLines) {
            this.distributionArea = distributionArea;
            this.maxFilesArea = maxFilesArea;
            this.maxLines = maxLines;
        }

        public int getMaxLines() {
            return maxLines;
        }

        public JTextArea getDistributionArea() {
            return distributionArea;
        }

        public JTextArea getMaxFilesArea() {
            return maxFilesArea;
        }
    }
}
