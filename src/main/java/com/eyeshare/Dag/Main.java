package com.eyeshare.Dag;

import javax.swing.SwingUtilities;

import com.eyeshare.Dag.view.ExcelReformatterGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExcelReformatterGUI().setVisible(true);
        });
    }
}