package com.stitch80.ExcelHelperBot.bot.controller;

import com.stitch80.ExcelHelperBot.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.bot.keyboardfactory.InlineKeyboardFactory;
import com.stitch80.ExcelHelperBot.bot.keyboardfactory.ReplyKeyboardFactory;
import com.stitch80.ExcelHelperBot.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.bot.senders.ReplyKeyboardSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Component
public class CommandController {

    private ReplyKeyboardFactory replyKeyboardFactory;
    private InlineKeyboardFactory inlineKeyboardFactory;
    private ReplyKeyboardSender replyKeyboardSender;
    private InlineKeyboardSender inlineKeyboardSender;

    public CommandController(ReplyKeyboardFactory replyKeyboardFactory,
                             InlineKeyboardFactory inlineKeyboardFactory,
                             ReplyKeyboardSender replyKeyboardSender,
                             InlineKeyboardSender inlineKeyboardSender) {
        this.replyKeyboardFactory = replyKeyboardFactory;
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.replyKeyboardSender = replyKeyboardSender;
        this.inlineKeyboardSender = inlineKeyboardSender;
    }

    public void processCommand(Update update, ExcelHelperBot excelHelperBot) {
        Message message = update.getMessage();
        User user = message.getFrom();
        if (message.getText().equals("/start")) {
            replyKeyboardSender.sendStartMenu(user, excelHelperBot);
        } else if (message.getText().equals("/test")) {
            inlineKeyboardSender.sendMonthMenuKeyboard(user, excelHelperBot, LocalDate.now());
        }
    }
}
