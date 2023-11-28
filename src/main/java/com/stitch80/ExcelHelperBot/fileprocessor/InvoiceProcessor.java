package com.stitch80.ExcelHelperBot.fileprocessor;

import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;

public class InvoiceProcessor {

    private final static CellAddress INV_NO = new CellAddress(8, 5);
    private final static CellAddress INV_DATE = new CellAddress(9, 5);
    private final static CellAddress CUSTOMER_NAME = new CellAddress(9, 2);
    private final static CellAddress AMOUNT1 = new CellAddress(15, 5);
    private final static CellAddress AMOUNT2 = new CellAddress(20, 5);
    private final XSSFWorkbook currentWorkbook;


    public InvoiceProcessor(XSSFWorkbook currentWorkbook) {
        this.currentWorkbook = currentWorkbook;
    }

    public XSSFWorkbook createNewInvoice(
            String invNo, LocalDate invDate, String customerName, double amount
    ) {
        modifySheet("INV", invNo, invDate, customerName, amount);
        modifySheet("RE", invNo, invDate, customerName, amount);

        return currentWorkbook;
    }

    private void modifySheet(
            String sheetName,
            String invNo, LocalDate invDate, String customerName,
            double amount
    ) {
        currentWorkbook.setForceFormulaRecalculation(true);

        XSSFSheet sheet = currentWorkbook.getSheet(sheetName);
        //set invoice number
        XSSFCell cell = sheet.getRow(INV_NO.getRow()).getCell(INV_NO.getColumn());
        cell.setCellValue(invNo);

        //set invoice date
        cell = sheet.getRow(INV_DATE.getRow()).getCell(INV_DATE.getColumn());
        cell.setCellValue(invDate);

        //set customer name
        cell = sheet.getRow(CUSTOMER_NAME.getRow()).getCell(CUSTOMER_NAME.getColumn());
        cell.setCellValue(customerName);

        //set amount1
        cell = sheet.getRow(AMOUNT1.getRow()).getCell(AMOUNT1.getColumn());
        cell.setCellValue(amount);

        // set amount2
        cell = sheet.getRow(AMOUNT2.getRow()).getCell(AMOUNT2.getColumn());
        cell.setCellValue(amount);


    }
}
