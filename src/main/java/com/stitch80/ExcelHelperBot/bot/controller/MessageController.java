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


    private final InvoiceDTO invoiceDTO;
    private final TextSender textSender;
    private final DocumentSender documentSender;
    private final ReplyKeyboardSender replyKeyboardSender;
    private final InlineKeyboardSender inlineKeyboardSender;

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

        processKeyboardInput(excelHelperBot, message, user);
    }


    private void processKeyboardInput(ExcelHelperBot excelHelperBot, Message message, User user) {
        String text;
        switch (message.getText()) {
            case "Create invoice":
                replyKeyboardSender.sendInvDetailsMenu(user, excelHelperBot);
                break;
            case "Invoice Number":
                text = """
                        Please send the invoice number in the year in the message to bot
                        It will be used in creation of invoice number field in Excel file
                        For example: 015
                        """;
                textSender.sendText(user.getId(), text, excelHelperBot);
                invoiceDTO.setStatus("INV_NO");
                break;
            case "Invoice Date":
                inlineKeyboardSender.sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
                break;
            case "Customer Name":
                text = """
                        Please send the customer first name and last name
                        For example: Angeline Jolie
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
            case "INV_NO":
                invoiceDTO.setInvNo(message.getText());
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
            case "CUSTOMER_NAME":
                invoiceDTO.setCustomerName(message.getText());
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
            case "AMOUNT":
                invoiceDTO.setAmount(Double.parseDouble(message.getText()));
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
        }
    }


}
