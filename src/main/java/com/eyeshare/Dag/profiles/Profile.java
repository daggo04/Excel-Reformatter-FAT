package com.eyeshare.Dag.profiles;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String name;
    private String templatePath;
    private String description;
    private List<Operation<?>> operations;
    private NamingConvention namingConvention;

    public Profile(String name) {
        this.name = name;
        this.namingConvention = NamingConvention.PRESERVE_NAME;
        this.operations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Operation<?>> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation<?>> operations) {
        this.operations = operations;
    }

    public void addOperation(Operation<?> operation) {
        this.operations.add(operation);
    }
}