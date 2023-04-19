package com.eyeshare.Dag.utils;

import com.eyeshare.Dag.profiles.OpType;
import com.eyeshare.Dag.profiles.Operation;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OperationDeserializer implements JsonDeserializer<Operation<?>> {

    @Override
    public Operation<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String opTypeString = jsonObject.get("type").getAsString();
        OpType opType = OpType.valueOf(opTypeString);

        JsonObject params = jsonObject.get("parameters").getAsJsonObject();
        Map<String, Object> parameters = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : params.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (key.equals("colMap")) {
                Map<Number, Number> colMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> colMapEntry : value.getAsJsonObject().entrySet()) {
                    Double colMapKey = Double.valueOf(colMapEntry.getKey());
                    Double colMapValue = colMapEntry.getValue().getAsDouble();
                    colMap.put(colMapKey, colMapValue);
                }
                parameters.put(key, colMap);
            } else {
                parameters.put(key, context.deserialize(value, Object.class));
            }
        }

        return new Operation<>(opType, parameters);
    }
}
