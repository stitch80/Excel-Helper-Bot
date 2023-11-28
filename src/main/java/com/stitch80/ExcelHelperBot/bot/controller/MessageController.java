package com.stitch80.ExcelHelperBot.bot.controller;

import com.stitch80.ExcelHelperBot.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.bot.senders.DocumentSender;
import com.stitch80.ExcelHelperBot.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.bot.senders.ReplyKeyboardSender;
import com.stitch80.ExcelHelperBot.bot.senders.TextSender;
import com.stitch80.ExcelHelperBot.dto.InvoiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Component
@Slf4j
public class MessageController {


    private InvoiceDTO invoiceDTO;
    private TextSender textSender;
    private DocumentSender documentSender;
    private ReplyKeyboardSender replyKeyboardSender;
    private InlineKeyboardSender inlineKeyboardSender;

    public MessageController(
            InvoiceDTO invoiceDTO,
            TextSender textSender,
            ReplyKeyboardSender replyKeyboardSender,
            InlineKeyboardSender inlineKeyboardSender,
            DocumentSender documentSender) {
        this.invoiceDTO = invoiceDTO;
        this.textSender = textSender;
        this.replyKeyboardSender = replyKeyboardSender;
        this.inlineKeyboardSender = inlineKeyboardSender;
        this.documentSender = documentSender;
    }

    public void processMessage(Update update, ExcelHelperBot excelHelperBot) {
        Message message = update.getMessage();
        User user = message.getFrom();

        processInput(excelHelperBot, message, user);
        log.info(user.getUserName() + " sent the message " + message.getText());
//        sendText(user.getId(), message.getText());
//        copyMessage(user.getId(), message.getMessageId());
    }


    private void processInput(ExcelHelperBot excelHelperBot, Message message, User user) {
        String text;
        switch (message.getText()) {
            case "Create invoice":
                replyKeyboardSender.sendInvDetailsMenu(user, excelHelperBot);
                break;
//            case "Year":
//                text = """
//                        Please send the year of invoice in the message to bot
//                        It will be used in creation of invoice number field in Excel file
//                        For example: 2023
//                        """;
//                textSender.sendText(user.getId(), text, excelHelperBot);
//                invoiceDTO.setStatus("YEAR");
//                System.out.println(invoiceDTO.getInvoiceStatus());
//                break;
            case "Invoice Number":
                text = """
                        Please send the invoice number in the year in the message to bot
                        It will be used in creation of invoice number field in Excel file
                        For example: 015
                        """;
                textSender.sendText(user.getId(), text, excelHelperBot);
//                currentStatus = InvoiceStatus.INV_NO.toString();
                invoiceDTO.setStatus("INV_NO");
                break;
            case "Invoice Date":
//                text = """
//                        Please send the invoice date in the format YYYY-MM-DD
//                        For example: 2023-12-15
//                        """;
//                textSender.sendText(user.getId(), text, excelHelperBot);
                inlineKeyboardSender.sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
//                invoiceDTO.setStatus("INV_DATE");
                break;
            case "Customer Name":
                text = """
                        Please send the customer first name and last name
                        For example: Amalie Bruun
                        """;
                textSender.sendText(user.getId(), text, excelHelperBot);
                invoiceDTO.setStatus("CUSTOMER_NAME");
                break;
            case "Amount":
                text = """
                        Please send the invoice amount
                        For example: 10000
                        """;
                textSender.sendText(user.getId(), text, excelHelperBot);
                invoiceDTO.setStatus("AMOUNT");
                break;
            case "Get Invoice":
//                sendDocumentTest(user);
                if (invoiceDTO.isCompleted()) {
                    System.out.println(invoiceDTO);
                    documentSender.sendInvoiceDocument(user, invoiceDTO, excelHelperBot);
                    replyKeyboardSender.sendMainMenu(user, excelHelperBot);
                } else {
                    text = """
                            Invoice is not completed
                            Please fill all the fields
                            """;
                    textSender.sendText(user.getId(), text, excelHelperBot);
                }
                break;
            default:

                processUserInput(message, user, excelHelperBot);

                break;
        }
    }


    private void processUserInput(Message message, User user, ExcelHelperBot excelHelperBot) {
        switch (invoiceDTO.getInvoiceStatus()) {
//            case "YEAR":
//                invoiceDTO.setYear(message.getText());
//                System.out.println(invoiceDTO.getYear());
//                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
//                break;
            case "INV_NO":
                invoiceDTO.setInvNo(message.getText());
                System.out.println(invoiceDTO.getInvNo());
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
//            case "INV_DATE":
//                invoiceDTO.setInvDate(message.getText());
//                invoiceDTO.setYear(message.getText().substring(0, 4));
//                System.out.println(invoiceDTO.getInvDate());
//                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
//                break;
            case "CUSTOMER_NAME":
                invoiceDTO.setCustomerName(message.getText());
                System.out.println(invoiceDTO.getCustomerName());
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
            case "AMOUNT":
                invoiceDTO.setAmount(Double.parseDouble(message.getText()));
                System.out.println(invoiceDTO.getAmount());
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
        }
    }


}
