package com.eyeshare.Dag;

import java.io.IOException;
import java.util.HashMap;

import com.eyeshare.Dag.handlers.ExcelHandler;

public class TestExcelHandler {
    public static void main(String[] args) {
        try {
            String sourceFilePath = "test_input/Fakturavtale(95).xlsx";
            String templateFilePath = "src/main/resources/templates/NAV_TEMPLATE.xlsx";
            String outputFilePath = "test_output/output.xlsx";

            // Initialize the ExcelHandler with source and template workbooks
            ExcelHandler excelHandler = new ExcelHandler(sourceFilePath, templateFilePath);

            // Copy columns to the first sheet
            excelHandler.copyColumn(0,1, 0, 2, 1); // Copy column B to C
            excelHandler.copyColumn(0, 2, 0, 3, 1); // Copy column C to D
            excelHandler.copyColumn(0, 3, 0, 4, 1); // Copy column D to E
            excelHandler.copyColumn(0, 4, 0, 8, 1); // Copy column E to I
            excelHandler.copyColumn(0, 5, 0, 9, 1); // Copy column F to J
            excelHandler.copyColumn(0, 6, 0, 10, 1); // Copy column G to K
            excelHandler.copyColumn(0, 15, 0, 25, 1); // Copy column P to Z

            // Copy columns to the third sheet
            excelHandler.copyColumn(0, 0, 2, 5, 1); // Copy column A to F
            excelHandler.copyColumn(0, 8, 2, 2, 1); // Copy column I to C

            // Copy split row values to the fourth sheet
            HashMap<Integer, Integer> columnMap = new HashMap<>();
            for (int i = 10; i <= 14; i++) {
                columnMap.put(i, 3);
            }
            columnMap.put(0,0);
            System.out.println(columnMap);
            excelHandler.copySplitRow(0, 3, 1, columnMap, true, 2);

            // Save the output workbook
            excelHandler.saveOutputWorkbook(outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
