package com.eyeshare.Dag.profiles;

import java.util.Map;

public class Operation<E> {
    private OpType type;
    private Map<String, E> parameters;

    public Operation(OpType type, Map<String, E> parameters) {
        this.type = type;
        this.parameters = parameters;
        validateParameters();
    }

    // Getters and setters
    public OpType getType() {
        return type;
    }

    public void setType(OpType type) {
        this.type = type;
    }

    public Map<String, E> getParameters() {
        return parameters;
    }

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