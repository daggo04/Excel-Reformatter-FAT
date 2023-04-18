package com.eyeshare.Dag.handlers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Dag O.B.H on 2023.18.04.
 * <p>This class is responsible for handling the Excel files.</p>
 * 
 * 
 */
public class ExcelHandler {
    private Workbook source;
    private Workbook template;
    private Workbook output;


    // Constructors
    /**
     * Constructor for a new ExcelHandler object.
     * @param filePath The path to the file to be loaded.
     * @throws IOException if the file cannot be found.
     */
    public ExcelHandler(String filePath) throws IOException {
        this.source = new XSSFWorkbook(new FileInputStream(filePath));
        this.template = null;
    }

    /**
     * Constructor for a new ExcelHandler object.
     * @param sourceFilePath The path to the source file to be loaded.
     * @param templateFilePath The path to the template file to be loaded.
     * @throws IOException if the file cannot be found.
     */
    public ExcelHandler(String sourceFilePath, String templateFilePath) throws IOException {
            try {
        FileInputStream sourceInputStream = new FileInputStream(sourceFilePath);
        FileInputStream templateInputStream = new FileInputStream(templateFilePath);
        this.source = WorkbookFactory.create(sourceInputStream);
        this.template = WorkbookFactory.create(templateInputStream);

        // Initialize the output workbook as a copy of the template workbook
        ByteArrayOutputStream templateBytes = new ByteArrayOutputStream();
        this.template.write(templateBytes);
        ByteArrayInputStream outputBytes = new ByteArrayInputStream(templateBytes.toByteArray());
        this.output = WorkbookFactory.create(outputBytes);

        sourceInputStream.close();
        templateInputStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Constructor for a new ExcelHandler object.
     * @param source The source workbook to be loaded.
     * @param template The template workbook to be loaded.
     */
    public ExcelHandler(Workbook source, Workbook template) {
        this.source = source;
        this.template = template;
    }

    /**
     * Constructor for a new ExcelHandler object.
     * @param source The source workbook to be loaded.
     */
    public ExcelHandler(Workbook source) {
        this.source = source;
        this.template = null;
    }


    public void saveOutputWorkbook(String outputFilePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
            output.write(fileOut);
        }
        closeWorkbooks();
    }

    public void mergeTemplateOutput() {
        output = new XSSFWorkbook();
        for (int i = 0; i < template.getNumberOfSheets(); i++) {
            Sheet templateSheet = template.getSheetAt(i);
            Sheet outputSheet = output.createSheet(templateSheet.getSheetName());
            copySheet(templateSheet, outputSheet);
        }
    }
    
    public void copyRows(int sourceSheetIndex, int targetSheetIndex, int startRow, int endRow) {
        Sheet sourceSheet = source.getSheetAt(sourceSheetIndex);
        Sheet targetSheet = output.getSheetAt(targetSheetIndex);
    
        for (int i = startRow; i <= endRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = targetSheet.createRow(i);
            copyRow(sourceRow, targetRow);
        }
    }
    
    public void copyColumn(int sourceSheetIndex, int sourceColumn, int targetSheetIndex, int targetColumn, int startRow) {
        Sheet sourceSheet = source.getSheetAt(sourceSheetIndex);
        Sheet targetSheet = output.getSheetAt(targetSheetIndex);
        int lastRowNum = sourceSheet.getLastRowNum();
    
        for (int i = startRow; i <= lastRowNum; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = targetSheet.getRow(i);
    
            if (sourceRow != null) {
                Cell sourceCell = sourceRow.getCell(sourceColumn);
                if (targetRow == null) {
                    targetRow = targetSheet.createRow(i);
                }
                Cell targetCell = targetRow.createCell(targetColumn);
                copyCell(sourceCell, targetCell);
            }
        }
    }

    public void copySplitRow(int sourceSheetIndex, int targetSheetIndex, int startRow, HashMap<Integer, Integer> columnMap, boolean includeHeaders, int headerCol) {
        Sheet sourceSheet = source.getSheetAt(sourceSheetIndex);
        Sheet targetSheet = output.getSheetAt(targetSheetIndex);
    
        int lastSourceRow = sourceSheet.getLastRowNum();
        int targetRowIdx = startRow;

        HashMap<Integer, List<Integer>> invertedColMap = invertColumnMap(columnMap);
        System.out.println(invertedColMap);
        
        int splits = 1;
        int keyToSplit = 0;
        for (int key : invertedColMap.keySet()) {
            if (invertedColMap.get(key).size() > splits) {
                splits = invertedColMap.get(key).size();
                keyToSplit = key;
            }
        }

        for (int i = startRow; i < lastSourceRow; i++) {
            Row sourceRow = sourceSheet.getRow(i);
            if (sourceRow != null) {
                for (int j = 0; j < splits; j++) {
                    Row targetRow = targetSheet.createRow(targetRowIdx);
                    for (int key : invertedColMap.keySet()) {
                        if (key == keyToSplit) {
                            Cell sourceCell = sourceRow.getCell(invertedColMap.get(key).get(j));
                            Cell targetCell = targetRow.createCell(key);
                            copyCell(sourceCell, targetCell);
                            if (includeHeaders) {
                                Cell headerCell = sourceSheet.getRow(0).getCell(invertedColMap.get(key).get(j));
                                Cell targetHeaderCell = targetRow.createCell(headerCol);
                                copyCell(headerCell, targetHeaderCell);
                            }

                        } else {
                            Cell sourceCell = sourceRow.getCell(key);
                            Cell targetCell = targetRow.createCell(invertedColMap.get(key).get(0));
                            copyCell(sourceCell, targetCell);
                            }
                        }
                        targetRowIdx++;
                    }
                }
            }
        }
    
    
    
    
    

    
    //Helper methods
    private void copyCell(Cell sourceCell, Cell destinationCell) {
        if (sourceCell == null) {
        // If the source cell is null, set the target cell to blank
            destinationCell.setBlank();
        return;
    }
        CellType cellType = sourceCell.getCellType();


        switch (cellType) {
            case STRING:
                destinationCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    destinationCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    destinationCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                destinationCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                destinationCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
                destinationCell.setBlank();
                break;
            default:
                break;
        }
    }

    private void copySheet(Sheet sourceSheet, Sheet destinationSheet) {
        for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            if (sourceRow == null) continue;
            Row destinationRow = destinationSheet.createRow(i);
            copyRow(sourceRow, destinationRow);
        }
    }

    private void copyRow(Row sourceRow, Row destinationRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell == null) continue;
            Cell destinationCell = destinationRow.createCell(i);
            copyCell(sourceCell, destinationCell);
        }
    }

    private void pasteRow(int targetSheetIndex, int targetRowIndex, Row rowToPaste) {
        Sheet targetSheet = output.getSheetAt(targetSheetIndex);
        Row targetRow = targetSheet.createRow(targetRowIndex);
    
        if (rowToPaste != null) {
            int lastCellNum = rowToPaste.getLastCellNum();
    
            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                Cell sourceCell = rowToPaste.getCell(cellIndex);
                Cell targetCell = targetRow.createCell(cellIndex);
    
                if (sourceCell != null) {
                    copyCell(sourceCell, targetCell);
                }
            }
        }
    }

    private void copyRowToOutput(int sourceSheetIndex, int sourceRowIndex, int targetSheetIndex, int targetRowIndex) {
        Sheet sourceSheet = source.getSheetAt(sourceSheetIndex);
        Sheet targetSheet = output.getSheetAt(targetSheetIndex);
    
        Row sourceRow = sourceSheet.getRow(sourceRowIndex);
        Row targetRow = targetSheet.createRow(targetRowIndex);
    
        if (sourceRow != null) {
            int lastCellNum = sourceRow.getLastCellNum();
    
            for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
                Cell sourceCell = sourceRow.getCell(cellIndex);
                Cell targetCell = targetRow.createCell(cellIndex);
    
                if (sourceCell != null) {
                    copyCell(sourceCell, targetCell);
                }
            }
        }
    }

    private HashMap<Integer, List<Integer>> invertColumnMap(HashMap<Integer, Integer> columnMap) {
        HashMap<Integer, List<Integer>> invertedMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : columnMap.entrySet()) {
            int sourceColumn = entry.getKey();
            int targetColumn = entry.getValue();
            invertedMap.putIfAbsent(targetColumn, new ArrayList<>());
            invertedMap.get(targetColumn).add(sourceColumn);
        }
        return invertedMap;
    }

    private Cell getCell(int sheet, int row, int column) {
        Sheet currentSheet = source.getSheetAt(sheet);
        Row currentRow = currentSheet.getRow(row);
        return currentRow.getCell(column);
    }

    private void setCell(int sheet, int row, int column, Cell cell) {
        Sheet currentSheet = output.getSheetAt(sheet);
        Row currentRow = currentSheet.getRow(row);
        if (currentRow == null) {
            currentRow = currentSheet.createRow(row);
        }
        Cell newCell = currentRow.createCell(column);
        newCell.setCellValue(cell.getStringCellValue());
    }

    private List<Cell> getColumn(int sheet, int column) {
        Sheet currentSheet = source.getSheetAt(sheet);
        List<Cell> columnData = new ArrayList<>();
        for (Row row : currentSheet) {
            columnData.add(row.getCell(column));
        }
        return columnData;
    }

    private void setColumn(int sheet, int column, List<Cell> cells) {
        Sheet currentSheet = output.getSheetAt(sheet);
        for (int i = 0; i < cells.size(); i++) {
            Row row = currentSheet.getRow(i);
            if (row == null) {
                row = currentSheet.createRow(i);
            }
            Cell newCell = row.createCell(column);
            newCell.setCellValue(cells.get(i).getStringCellValue());
        }
    }

    private Row getRow(int sheet, int rowIndex) {
        Sheet currentSheet = source.getSheetAt(sheet);
        return currentSheet.getRow(rowIndex);
    }

    private void closeWorkbooks() {
        if (source != null) {
            try {
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (template != null) {
            try {
                template.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



