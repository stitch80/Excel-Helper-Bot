package com.stitch80.ExcelHelperBot.bot.senders;

import com.stitch80.ExcelHelperBot.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.dto.InvoiceDTO;
import com.stitch80.ExcelHelperBot.fileprocessor.InvoiceProcessor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.time.LocalDate;

@Component
public class DocumentSender {

    public void sendInvoiceDocument(User user, InvoiceDTO invoiceDTO, ExcelHelperBot excelHelperBot) {

        String filePath = "/Users/stitch80/IdeaProjects/ExcelHelperBot/src/main/resources/";
        XSSFWorkbook invoiceDocument = getModifiedExcelWorkbook(
                filePath,
                invoiceDTO.getYear(), invoiceDTO.getInvNo(),
                invoiceDTO.getInvDate(), invoiceDTO.getCustomerName(),
                invoiceDTO.getAmount()
        );

        FileOutputStream targetFile = null;
        String fileName = "RE-INV." + invoiceDTO.getInvNo() + ".xlsx";
        String fileAbsolutePath = filePath + fileName;
        try {
            targetFile = new FileOutputStream(fileAbsolutePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            invoiceDocument.write(targetFile);
//            invoiceDocument.close();
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
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private XSSFWorkbook getModifiedExcelWorkbook(
            String filePath,
            String year, String invNo, String invDate, String customerName, double amount
    ) {
        FileInputStream sourceFile = null;
        try {
            sourceFile = new FileInputStream(filePath + "RE-INV.000.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XSSFWorkbook workbook = null;
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
        System.out.println(date);
        return invoiceProcessor.createNewInvoice(
                invNumber,
                date,
                customerName,
                amount
        );

    }


}
