package com.stitch80.ExcelHelperBot.components.bot.senders;

import com.stitch80.ExcelHelperBot.components.Invoices;
import com.stitch80.ExcelHelperBot.components.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.components.bot.keyboardfactory.ReplyKeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ReplyKeyboardSender {

    private final Invoices invoices;
    private final TextSender textSender;
    private final ReplyKeyboardFactory replyKeyboardFactory;


    public ReplyKeyboardSender(Invoices invoices,
                               ReplyKeyboardFactory replyKeyboardFactory,
                               TextSender textSender) {
        this.invoices = invoices;
        this.replyKeyboardFactory = replyKeyboardFactory;
        this.textSender = textSender;
    }

    public void sendStartMenu(User user, ExcelHelperBot excelHelperBot) {
        String text = """
                Welcome to this helper bot
                At the moment it can help only with creation of invoices from template
                But it's just a beginning 😀
                """;
        sendReplyMenu(user.getId(), text, replyKeyboardFactory.constructMainMenu(), excelHelperBot);
    }

    public void sendMainMenu(User user, ExcelHelperBot excelHelperBot) {
        String text = """
                Ready to create another invoice?
                Just tap on "Create invoice" button!
                """;
        sendReplyMenu(user.getId(), text, replyKeyboardFactory.constructMainMenu(), excelHelperBot);
    }

    public void sendInvoiceStatusAndInvoiceMenu(User user, ExcelHelperBot excelHelperBot) {
        Long userId = user.getId();
        textSender.sendText(userId, invoices.constructInvoiceStatusMessage(userId), excelHelperBot);
        sendInvDetailsMenu(user, excelHelperBot);
        invoices.checkIfInvoiceIsCompleted(userId);
    }

    public void sendInvDetailsMenu(User user, ExcelHelperBot excelHelperBot) {
        String text;
        text = """
                Please tap the buttons to provide necessary information for invoice
                """;
        sendReplyMenu(user.getId(), text, replyKeyboardFactory.constructInvoiceDetailsMenu(), excelHelperBot);
    }

    private void sendReplyMenu(
            Long userId, String text,
            ReplyKeyboardMarkup kb,
            DefaultAbsSender excelHelperBot) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(userId.toString())
                .text(text)
                .replyMarkup(kb)
                .build();

        try {
            excelHelperBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
