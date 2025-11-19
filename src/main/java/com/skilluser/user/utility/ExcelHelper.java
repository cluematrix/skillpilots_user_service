package com.skilluser.user.utility;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelHelper
{

    public static ByteArrayInputStream generateMasterTemplate() throws IOException
    {
        try (Workbook workbook = new XSSFWorkbook())
        {
            Sheet stateSheet = workbook.createSheet("States");
            Sheet universitySheet = workbook.createSheet("Universities");
            Sheet collegeSheet = workbook.createSheet("Colleges");

            // State headers
            Row stateHeader = stateSheet.createRow(0);
            stateHeader.createCell(0).setCellValue("State Name");
            stateHeader.createCell(1).setCellValue("Country");

            // University headers
            Row uniHeader = universitySheet.createRow(0);
            uniHeader.createCell(0).setCellValue("University Name");
            uniHeader.createCell(1).setCellValue("Address");
            uniHeader.createCell(2).setCellValue("State Name (Existing)");

            // College headers
            Row collegeHeader = collegeSheet.createRow(0);
            collegeHeader.createCell(0).setCellValue("College Name");
            collegeHeader.createCell(1).setCellValue("City");
            collegeHeader.createCell(2).setCellValue("District");
            collegeHeader.createCell(3).setCellValue("Type");
            collegeHeader.createCell(4).setCellValue("State Name (Existing)");
            collegeHeader.createCell(5).setCellValue("University Name (Existing)");

            // Auto-size columns
            for (int i = 0; i < 6; i++)
            {
                stateSheet.autoSizeColumn(i);
                universitySheet.autoSizeColumn(i);
                collegeSheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
