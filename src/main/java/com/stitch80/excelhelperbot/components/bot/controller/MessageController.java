package com.stitch80.excelhelperbot.components.bot.controller;

import com.stitch80.excelhelperbot.components.Invoices;
import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.bot.senders.DocumentSender;
import com.stitch80.excelhelperbot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.excelhelperbot.components.bot.senders.ReplyKeyboardSender;
import com.stitch80.excelhelperbot.components.bot.senders.TextSender;
import com.stitch80.excelhelperbot.dto.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Service
@Slf4j
public class MessageController {


    private final Invoices invoices;
    private final TextSender textSender;
    private final DocumentSender documentSender;
    private final ReplyKeyboardSender replyKeyboardSender;
    private final InlineKeyboardSender inlineKeyboardSender;

    public MessageController(
            Invoices invoices,
            TextSender textSender,
            ReplyKeyboardSender replyKeyboardSender,
            InlineKeyboardSender inlineKeyboardSender,
            DocumentSender documentSender) {
        this.invoices = invoices;
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
        Long userId = user.getId();
        switch (message.getText()) {
            case "Create invoice":
                invoices.createNewInvoice(userId);
                replyKeyboardSender.sendInvDetailsMenu(user, excelHelperBot);
                break;
            case "Invoice Number":
                text = """
                        Please send the invoice number in the year in the message to bot
                        It will be used in creation of invoice number field in Excel file
                        For example: 015
                        """;
                textSender.sendText(userId, text, excelHelperBot);
//                invoices.setStatus("INV_NO", userId);
                invoices.setStatus(Invoice.InvoiceStatus.INV_NO, userId);
                break;
            case "Invoice Date":
                inlineKeyboardSender.sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
                break;
            case "Customer Name":
                text = """
                        Please send the customer first name and last name
                        For example: Angeline Jolie
                        """;
                textSender.sendText(userId, text, excelHelperBot);
//                invoices.setStatus("CUSTOMER_NAME", userId);
                invoices.setStatus(Invoice.InvoiceStatus.CUSTOMER_NAME, userId);
                break;
            case "Amount":
                text = """
                        Please send the invoice amount
                        For example: 10000
                        """;
                textSender.sendText(userId, text, excelHelperBot);
//                invoices.setStatus("AMOUNT", userId);
                invoices.setStatus(Invoice.InvoiceStatus.AMOUNT, userId);
                break;
            case "Get Invoice":
                if (invoices.userInvoiceIsCompleted(userId)) {
                    documentSender.sendInvoiceDocument(user, invoices, excelHelperBot);
                    replyKeyboardSender.sendMainMenu(user, excelHelperBot);
                } else {
                    text = """
                            Invoice is not completed
                            Please fill all the fields
                            """;
                    textSender.sendText(userId, text, excelHelperBot);
                }
                break;
            default:

                try {
                    processUserInput(message, user, excelHelperBot);
                } catch (RuntimeException re) {
                    textSender.sendText(userId, re.getMessage() + "\nTry again!", excelHelperBot);
                }

                break;
        }
    }


    private void processUserInput(Message message, User user, ExcelHelperBot excelHelperBot) throws RuntimeException {
        Long userId = user.getId();
        String messageText = message.getText();
        switch (invoices.getInvoiceStatus(userId)) {
            case "INV_NO":
                invoices.setInvNo(messageText, userId);
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
            case "CUSTOMER_NAME":
                invoices.setCustomerName(messageText, userId);
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
            case "AMOUNT":
                invoices.setAmount(messageText, userId);
                replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);
                break;
        }
    }


}
