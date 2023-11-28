package com.stitch80.ExcelHelperBot.bot.senders;

import com.stitch80.ExcelHelperBot.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.bot.keyboardfactory.ReplyKeyboardFactory;
import com.stitch80.ExcelHelperBot.dto.InvoiceDTO;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ReplyKeyboardSender {

    private final InvoiceDTO invoiceDTO;
    private final TextSender textSender;
    private final ReplyKeyboardFactory replyKeyboardFactory;


    public ReplyKeyboardSender(InvoiceDTO invoiceDTO,
                               ReplyKeyboardFactory replyKeyboardFactory,
                               TextSender textSender) {
        this.invoiceDTO = invoiceDTO;
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
        System.out.println(invoiceDTO.getInvoiceStatus());
    }

    public void sendMainMenu(User user, ExcelHelperBot excelHelperBot) {
        String text = """
                Ready to create another invoice?
                Just tap on "Create invoice" button!
                """;
        sendReplyMenu(user.getId(), text, replyKeyboardFactory.constructMainMenu(), excelHelperBot);
        System.out.println(invoiceDTO.getInvoiceStatus());
    }

    public void sendInvoiceStatusAndInvoiceMenu(User user, ExcelHelperBot excelHelperBot) {
        textSender.sendText(user.getId(), invoiceDTO.constructInvoiceStatusMessage(), excelHelperBot);
        sendInvDetailsMenu(user, excelHelperBot);
        invoiceDTO.checkIfInvoiceIsCompleted();
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
