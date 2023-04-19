package com.eyeshare.Dag.view;

import javax.swing.*;

import com.eyeshare.Dag.functionality.ExcelReformatter;
import com.eyeshare.Dag.profiles.ProfileManager;

public class MainFrame extends JFrame {
    // Managers
    private ProfileManager profileManager;


    public MainFrame(ProfileManager profileManager) {
        setTitle("Excel Reformatter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.profileManager = profileManager;

        setContentPane(new MainPanel(profileManager)); // Set MainPanel as the default content pane
        pack(); // Adjust the size according to the components
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(new ProfileManager());
            mainFrame.setVisible(true);
        });
    }
}
