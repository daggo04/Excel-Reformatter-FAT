package com.eyeshare.Dag;


import java.io.IOException;

import com.eyeshare.Dag.functionality.ExcelReformatter;
import com.eyeshare.Dag.profiles.Profile;
import com.eyeshare.Dag.profiles.ProfileManager;

public class TestProfileExecution {
    public static void main(String[] args) {
        try {
            String sourceFilePath = "test_input/Fakturavtale(95).xlsx";
            String templateFilePath = "src/main/resources/templates/NAV_TEMPLATE.xlsx";
            String outputFilePath = "test_output/output.xlsx";

            // Intialize the ProfileManager
            ProfileManager profileManager = new ProfileManager();

            // Load the profile
            Profile profile = profileManager.loadProfile("TestProfile");

            // Initialize the ExcelReformatter with source and template workbooks
            ExcelReformatter reformatter = new ExcelReformatter(sourceFilePath, templateFilePath);

            // Apply the profile
            reformatter.applyProfile(profile);

            // Save the output workbook
            reformatter.saveOutputWorkbook(outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}







