package com.stitch80.excelhelperbot.components.bot.senders;

import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.bot.keyboardfactory.InlineKeyboardFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Service
public class InlineKeyboardSender {

    private final InlineKeyboardFactory inlineKeyboardFactory;

    public InlineKeyboardSender(InlineKeyboardFactory inlineKeyboardFactory) {
        this.inlineKeyboardFactory = inlineKeyboardFactory;
    }

    public void sendMonthMenuKeyboard(User user, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        SendMessage sendMessageRequest = SendMessage.builder()
                .chatId(user.getId())
                .text("Choose invoice date")
                .replyMarkup(inlineKeyboardFactory.constructMonthMenu(localDate))
                .build();
        try {
            excelHelperBot.execute(sendMessageRequest);
        } catch (
                TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchMonthMenu(CallbackQuery callback, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        User user = callback.getFrom();
        Message message = callback.getMessage();
        String queryId = callback.getId();
        EditMessageText newText = EditMessageText.builder()
                .chatId(user.getId())
                .text("Choose invoice date")
                .messageId(message.getMessageId())
                .build();

        EditMessageReplyMarkup newMenu = EditMessageReplyMarkup.builder()
                .chatId(user.getId())
                .messageId(message.getMessageId())
                .replyMarkup(inlineKeyboardFactory.constructMonthMenu(localDate))
                .build();

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId)
                .build();

        try {
            excelHelperBot.execute(newText);
            excelHelperBot.execute(newMenu);
            excelHelperBot.execute(close);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendYearMenuKeyboard(CallbackQuery callback, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        User user = callback.getFrom();

        SendMessage sendMessageRequest = SendMessage.builder()
                .chatId(user.getId())
                .text("Choose month")
                .replyMarkup(inlineKeyboardFactory.constructYearMenu(localDate))
                .build();
        try {
            excelHelperBot.execute(sendMessageRequest);
        } catch (
                TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchYearMenu(CallbackQuery callback, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        User user = callback.getFrom();
        Message message = callback.getMessage();
        String queryId = callback.getId();
        EditMessageReplyMarkup newMenu = EditMessageReplyMarkup.builder()
                .chatId(user.getId())
                .messageId(message.getMessageId())
                .replyMarkup(inlineKeyboardFactory.constructYearMenu(localDate))
                .build();

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId)
                .build();

        try {
            excelHelperBot.execute(newMenu);
            excelHelperBot.execute(close);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendYearRangeMenuKeyboard(CallbackQuery callback, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        User user = callback.getFrom();

        SendMessage sendMessageRequest = SendMessage.builder()
                .chatId(user.getId())
                .text("Choose year")
                .replyMarkup(inlineKeyboardFactory.constructQuarterCenturyMenu(localDate))
                .build();
        try {
            excelHelperBot.execute(sendMessageRequest);
        } catch (
                TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchYearRangeMenu(CallbackQuery callback, ExcelHelperBot excelHelperBot, LocalDate localDate) {

        User user = callback.getFrom();
        Message message = callback.getMessage();
        String queryId = callback.getId();
        EditMessageReplyMarkup newMenu = EditMessageReplyMarkup.builder()
                .chatId(user.getId())
                .messageId(message.getMessageId())
                .replyMarkup(inlineKeyboardFactory.constructQuarterCenturyMenu(localDate))
                .build();

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId)
                .build();

        try {
            excelHelperBot.execute(newMenu);
            excelHelperBot.execute(close);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public void clearMenu(CallbackQuery callback, ExcelHelperBot excelHelperBot) {
        User user = callback.getFrom();
        Message message = callback.getMessage();
        String queryId = callback.getId();
        EditMessageReplyMarkup newMenu = EditMessageReplyMarkup.builder()
                .chatId(user.getId())
                .messageId(message.getMessageId())
                .replyMarkup(null)
                .build();

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId)
                .build();

        try {
            excelHelperBot.execute(newMenu);
            excelHelperBot.execute(close);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMessage(CallbackQuery callback, ExcelHelperBot excelHelperBot) {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(callback.getFrom().getId())
                .messageId(callback.getMessage().getMessageId())
                .build();

        try {
            excelHelperBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
