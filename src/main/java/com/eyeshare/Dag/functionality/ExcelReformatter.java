package com.eyeshare.Dag.functionality;

import com.eyeshare.Dag.handlers.ExcelHandler;
import com.eyeshare.Dag.profiles.Profile;

public class ExcelReformatter {

    private ExcelHandler excelHandler;
    private Profile profile;

    public ExcelReformatter(String sourceFilePath, Profile profile) {
        //this.excelHandler = new ExcelHandler(sourceFilePath);
        this.profile = profile;
    }

    public void applyProfile() {
        // Implement applying the profile transformations using the ExcelHandler
    }
}
    