package com.eyeshare.Dag.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.eyeshare.Dag.functionality.ExcelReformatter;
import com.eyeshare.Dag.profiles.ProfileManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    // Managers
    private ProfileManager profileManager;

    // Data fields
    private JList<String> sourceFilesList;
    private DefaultListModel<String> sourceFilesListModel;
    private JList<String> operationsList;
    private DefaultListModel<String> operationsListModel;

    // Components
    private JScrollPane sourceFilesScrollPane;
    private JButton chooseFilesButton;
    private JButton chooseDestinationButton;
    private JComboBox<String> profileComboBox;
    private JButton manageProfilesButton;
    private JButton applyProfileButton;

    public MainPanel(ProfileManager profileManager) {
        this.profileManager = profileManager;

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Source Files List
        this.sourceFilesListModel = new DefaultListModel<>();
        this.sourceFilesList = new JList<>(sourceFilesListModel);
        sourceFilesScrollPane = new JScrollPane(sourceFilesList);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(sourceFilesScrollPane, gbc);

        // Choose Files Button
        chooseFilesButton = new JButton("Choose Files");
        chooseFilesButton.addActionListener(e -> chooseFiles());
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(chooseFilesButton, gbc);

        // Choose Destination Button
        chooseDestinationButton = new JButton("Choose Destination");
        chooseDestinationButton.addActionListener(e -> chooseDestination());
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(chooseDestinationButton, gbc);

        // Operations List
        operationsListModel = new DefaultListModel<>();
        operationsList = new JList<>(operationsListModel);
        JScrollPane operationsScrollPane = new JScrollPane(operationsList);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(operationsScrollPane, gbc);

        // Profile ComboBox
        profileComboBox = new JComboBox<>();
        profileComboBox.addActionListener(e -> onProfileSelectionChanged());   
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(profileComboBox, gbc);
        updateProfileComboBox();
        

        // Manage Profiles Button
        manageProfilesButton = new JButton("Manage Profiles");
        manageProfilesButton.addActionListener(e -> manageProfiles());
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(manageProfilesButton, gbc);

        // Apply Profile Button
        applyProfileButton = new JButton("Apply Profile");
        applyProfileButton.addActionListener(e -> applyProfile());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(applyProfileButton, gbc);
    }

    private void chooseFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Source Files");
        fileChooser.setMultiSelectionEnabled(true); // Allow multiple file selection
    
        // Set file filter for Excel files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
        fileChooser.setFileFilter(filter);
    
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
    
            // Clear the existing list and add the newly selected files
            sourceFilesListModel.clear();
            for (File file : selectedFiles) {
                sourceFilesListModel.addElement(file.getName());
            }
        }
    }

    private void chooseDestination() {
        // Implement destination folder chooser logic
    }

    private void manageProfiles() {
        // Switch to ProfileManagementPanel
    }

    private void applyProfile() {
        // Apply the selected profile
    }

    private void updateProfileComboBox() {
        // Remove all existing items
        profileComboBox.removeAllItems();
    
        // Add the profile names from the ProfileManager
        for (String profileName : profileManager.getProfileNames()) {
            profileComboBox.addItem(profileName);
        }
    }
    private void onProfileSelectionChanged() {
        String selectedProfileName = (String) profileComboBox.getSelectedItem();
        if (selectedProfileName != null) {
            updateOperationsList(selectedProfileName);
        }
    }

    private void updateOperationsList(String profileName) {
        // Clear the existing list
        operationsListModel.clear();
    
        // Add the pretty operation strings for the selected profile
        ArrayList<String> prettyOperations = profileManager.operationsAsPrettyString(profileName);
        for (String operationString : prettyOperations) {
            operationsListModel.addElement(operationString);
        }
    }
    
}