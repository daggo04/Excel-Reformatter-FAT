package com.eyeshare.Dag;

import java.util.HashMap;
import java.util.Map;

import com.eyeshare.Dag.profiles.NamingConvention;
import com.eyeshare.Dag.profiles.OpType;
import com.eyeshare.Dag.profiles.Operation;
import com.eyeshare.Dag.profiles.Profile;
import com.eyeshare.Dag.profiles.ProfileManager;

public class TestProfile {
    public static void main(String[] args) {
        ProfileManager profileManager = new ProfileManager();

        Profile profile = new Profile("eye-share fakturaavtale");
        profile.setTemplatePath("src/main/resources/templates/NAV_TEMPLATE.xlsx");

        // Copy columns to the first sheet
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 1, "dstSheet", 0, "dstCol", 2, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 2, "dstSheet", 0, "dstCol", 3, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 3, "dstSheet", 0, "dstCol", 4, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 4, "dstSheet", 0, "dstCol", 8, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 5, "dstSheet", 0, "dstCol", 9, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 6, "dstSheet", 0, "dstCol", 10, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 15, "dstSheet", 0, "dstCol", 25, "startRow", 1)));

        // Copy columns to the third sheet
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 0, "dstSheet", 2, "dstCol", 5, "startRow", 1)));
        profile.addOperation(new Operation<>(OpType.COPY_COLUMN, Map.of("srcSheet", 0, "srcCol", 8, "dstSheet", 2, "dstCol", 2, "startRow", 1)));

        // Copy split row values to the fourth sheet
        // Copy split row values to the fourth sheet
        HashMap<Integer, Integer> columnMap = new HashMap<>();
        for (int i = 10; i <= 14; i++) {
            columnMap.put(i, 3);
        }
        columnMap.put(0, 0);
        
        HashMap<Number, Number> doubleColumnMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : columnMap.entrySet()) {
            doubleColumnMap.put(entry.getKey().doubleValue(), entry.getValue().doubleValue());
        }
        
        Map<String, Object> copySplitRowParams = new HashMap<>();
        copySplitRowParams.put("srcSheet", 0);
        copySplitRowParams.put("dstSheet", 3);
        copySplitRowParams.put("startRow", 1);
        copySplitRowParams.put("colMap", doubleColumnMap);
        copySplitRowParams.put("includeHeaders", true);
        copySplitRowParams.put("headerCol", 2);
        
        Operation<Object> copySplitRowOp = new Operation<>(OpType.COPY_SPLIT_ROW, copySplitRowParams);
        profile.addOperation(copySplitRowOp);
        profileManager.addProfile(profile);
        
        profileManager.addProfile(profile);
    }
}

