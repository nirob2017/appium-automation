package com.test.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public List<String> extractDataFromExcel(String filePath, String sheetName) {
        List<String> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            // Assuming the second row contains data
            Row dataRow = sheet.getRow(1);

            for (int i = 0; i < dataRow.getLastCellNum(); i++) {
                Cell cell = dataRow.getCell(i);
                String value = "";

                if (cell != null) {
                    if (cell.getCellType() == CellType.STRING) {
                        value = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf(cell.getNumericCellValue());
                    }
                }
                data.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
