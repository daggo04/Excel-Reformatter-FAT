package com.eyeshare.Dag.profiles;

import java.util.Map;


/**
 * Operation
 * Object representing a single operation to be performed on a spreadsheet
 */
public class Operation<E> {
    private OpType type;
    private Map<String, E> parameters;

    public Operation(OpType type, Map<String, E> parameters) {
        this.type = type;
        this.parameters = parameters;
        validateParameters();
    }

    // Getters and setters
    /**
     * Get the type of the operation
     * @return
     */
    public OpType getType() {
        return type;
    }

    /**
     * Set the type of the operation
     * @param type
     */
    public void setType(OpType type) {
        this.type = type;
    }

    /**
     * Get the parameters for the operation
     * @return Map<String, E> parameters
     */
    public Map<String, E> getParameters() {
        return parameters;
    }

    /**
     * Set the parameters for the operation
     * @param parameters
     */
    public void setParameters(Map<String, E> parameters) {
        this.parameters = parameters;
    }

    private void validateParameters() {
        switch (type) {
            case COPY_COLUMN:
                if (!parameters.containsKey("srcSheet") ||
                    !parameters.containsKey("srcCol") ||
                    !parameters.containsKey("dstSheet") ||
                    !parameters.containsKey("dstCol")) {
                    throw new IllegalArgumentException("Invalid parameters for COPY_COLUMN operation");
                }
                break;
            case COPY_ROWS:
                if (!parameters.containsKey("srcSheet") ||
                    !parameters.containsKey("srcCol") ||
                    !parameters.containsKey("startRrow") ||
                    !parameters.containsKey("endRow")) {
                    throw new IllegalArgumentException("Invalid parameters for COPY_COLUMN operation");
                }
            case COPY_SPLIT_ROW:
                if (!parameters.containsKey("srcSheet") ||
                    !parameters.containsKey("dstSheet") ||
                    !parameters.containsKey("startRow") ||
                    !parameters.containsKey("colMap")||
                    !parameters.containsKey("includeHeaders")||
                    !parameters.containsKey("headerCol")){
                    throw new IllegalArgumentException("Invalid parameters for COPY_SPLIT_ROW operation");
                }
                break;
            // Add more cases for other operation types
            default:
                throw new IllegalArgumentException("Invalid operation type");
        }
    }
    
}