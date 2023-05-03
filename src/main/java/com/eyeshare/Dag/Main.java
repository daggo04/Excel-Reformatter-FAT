package com.eyeshare.Dag;

import javax.swing.SwingUtilities;

import com.eyeshare.Dag.profiles.ProfileManager;
import com.eyeshare.Dag.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        ProfileManager profileManager = new ProfileManager();

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(profileManager);
            mainFrame.setVisible(true);
        });
    }
}