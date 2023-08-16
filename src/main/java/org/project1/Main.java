package org.project1;

import org.project1.GUI.AnalyserGUI;
//TODO cambiare la configurazione del sistem cosi da dargli un file di resource che non usa il cluster. Questo è da fare senno non funziona più il main dopo due start&stop
public class Main {

    public static void main(String[] args) {
        new AnalyserGUI();
    }
}
