package com.stitch80.excelhelperbot.components.bot.controller;

import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.excelhelperbot.components.bot.senders.ReplyKeyboardSender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Service
public class CommandController {

    private final ReplyKeyboardSender replyKeyboardSender;
    private final InlineKeyboardSender inlineKeyboardSender;

    public CommandController(ReplyKeyboardSender replyKeyboardSender,
                             InlineKeyboardSender inlineKeyboardSender) {
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
