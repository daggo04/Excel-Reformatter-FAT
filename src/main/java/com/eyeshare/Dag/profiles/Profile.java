package com.eyeshare.Dag.profiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile
 * Object representing a single profile
 */
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

    /**
     * Get the name of the profile
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the path to the template file
     * @return String templatePath
     */
    public String getTemplatePath() {
        return templatePath;
    }

    /**
     * Set the path to the template file
     * @param templatePath
     */
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * Get the description of the profile
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the profile
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the naming convention of the output files
     * @return {@link NamingConvention} namingConvention
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * Set the naming convention of the output files
     * Naming convention is represented by the {@link NamingConvention} enum
     * @param namingConvention
     */
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * Set the name of the profile
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the list of operations to be performed on the spreadsheet
     * @return {@link List}<{@link Operation}<?>> operations
     */
    public List<Operation<?>> getOperations() {
        return operations;
    }

    /**
     * Set the list of operations to be performed on the spreadsheet
     * The list of operations is represented by a {@link List}<{@link Operation}<?>>
     * @param operations
     */
    public void setOperations(List<Operation<?>> operations) {
        this.operations = operations;
    }

    /**
     * Add an operation to the list of operations to be performed on the spreadsheet
     * The list of operations is represented by a {@link List}<{@link Operation}<?>>
     * The operation is represented by an {@link Operation}<?>. 
     * @param operation
     */
    public void addOperation(Operation<?> operation) {
        this.operations.add(operation);
    }
}