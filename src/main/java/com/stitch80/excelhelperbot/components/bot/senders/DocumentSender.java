package com.stitch80.excelhelperbot.components.bot.senders;

import com.stitch80.excelhelperbot.components.Invoices;
import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.fileprocessor.InvoiceProcessor;
import com.stitch80.excelhelperbot.dto.Invoice;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.time.LocalDate;

@Service
public class DocumentSender {


    private final String filePath;

    public DocumentSender(@Value("${file.path}") String filePath) {
        this.filePath = filePath;
    }

    public void sendInvoiceDocument(
            User user, Invoices invoices, ExcelHelperBot excelHelperBot) {

        Invoice invoice = invoices.getUserInvoice(user.getId());
//        String filePath = "/usr/local/bin/";
        XSSFWorkbook invoiceDocument = getModifiedExcelWorkbook(
                filePath,
                invoice.getYear(), invoice.getInvNo(),
                invoice.getInvDate(), invoice.getCustomerName(),
                invoice.getAmount()
        );

        FileOutputStream targetFile;
        String fileName = "RE-INV." + invoice.getInvNo() + ".xlsx";
        String fileAbsolutePath = filePath + fileName;
        try {
            targetFile = new FileOutputStream(fileAbsolutePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            invoiceDocument.write(targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file = new File(fileAbsolutePath);

        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(user.getId());
        sendDocumentRequest.setDocument(new InputFile(file));
        sendDocumentRequest.setCaption(fileName);
        try {
            excelHelperBot.execute(sendDocumentRequest);
            file.delete();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private XSSFWorkbook getModifiedExcelWorkbook(
            String filePath,
            String year, String invNo, String invDate, String customerName, double amount
    ) {
        FileInputStream sourceFile;
        try {
            sourceFile = new FileInputStream(filePath + "RE-INV.000.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(sourceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String invNumber = year + "/" + invNo;
        String[] dateInfo = invDate.split("-");
        LocalDate date = LocalDate.of(
                Integer.parseInt(dateInfo[0]),
                Integer.parseInt(dateInfo[1]),
                Integer.parseInt(dateInfo[2])
        );

        InvoiceProcessor invoiceProcessor = new InvoiceProcessor(workbook);
        return invoiceProcessor.createNewInvoice(
                invNumber,
                date,
                customerName,
                amount
        );

    }


}
