package com.stitch80.excelhelperbot.components.bot.controller;

import com.stitch80.excelhelperbot.components.Invoices;
import com.stitch80.excelhelperbot.components.bot.ExcelHelperBot;
import com.stitch80.excelhelperbot.components.bot.keyboards.inline.MonthMenuKeyboard;
import com.stitch80.excelhelperbot.components.bot.keyboards.inline.QuarterCenturyMenuKeyboard;
import com.stitch80.excelhelperbot.components.bot.keyboards.inline.YearMenuKeyboard;
import com.stitch80.excelhelperbot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.excelhelperbot.components.bot.senders.ReplyKeyboardSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Service
@Slf4j
public class CallbackController {

    private final Invoices invoices;
    InlineKeyboardSender inlineKeyboardSender;
    ReplyKeyboardSender replyKeyboardSender;

    public CallbackController(InlineKeyboardSender inlineKeyboardSender,
                              ReplyKeyboardSender replyKeyboardSender,
                              Invoices invoices) {
        this.inlineKeyboardSender = inlineKeyboardSender;
        this.replyKeyboardSender = replyKeyboardSender;
        this.invoices = invoices;
    }

    public void processCallBack(Update update, ExcelHelperBot excelHelperBot) {
        CallbackQuery callback = update.getCallbackQuery();
        User user = callback.getFrom();
        String callbackMessage = callback.getData();

        LocalDate localDate = getDateFromCallback(callback);

        if (callbackMessage.contains(MonthMenuKeyboard.CONTROL_MONTH)) {
            inlineKeyboardSender.switchMonthMenu(callback, excelHelperBot, localDate);
        } else if (callbackMessage.contains(YearMenuKeyboard.CONTROL_YEAR)) {
            sendYearMenu(excelHelperBot, callbackMessage, callback, localDate);
        } else if (callbackMessage.contains(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY)) {
            sendYearRangeMenu(excelHelperBot, callbackMessage, callback, localDate);
        } else {
            clearMenuForNextMenu(excelHelperBot, callback);
            saveInvoiceDate(user, callbackMessage);
            replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);


        }
    }

    private void saveInvoiceDate(User user, String callbackMessage) {
        Long userId = user.getId();
        invoices.setInvDate(callbackMessage, userId);
        invoices.setYear(callbackMessage.substring(0, 4), userId);
    }

    private void sendYearRangeMenu(ExcelHelperBot excelHelperBot, String callbackMessage, CallbackQuery callback, LocalDate localDate) {
        if (callbackMessage.contains("start_")) {
            clearMenuForNextMenu(excelHelperBot, callback);
            inlineKeyboardSender.sendYearRangeMenuKeyboard(callback, excelHelperBot, localDate);

        } else {
            inlineKeyboardSender.switchYearRangeMenu(callback, excelHelperBot, localDate);
        }
    }

    private void sendYearMenu(ExcelHelperBot excelHelperBot, String callbackMessage, CallbackQuery callback, LocalDate localDate) {
        if (callbackMessage.contains("start_")) {
            clearMenuForNextMenu(excelHelperBot, callback);
            inlineKeyboardSender.sendYearMenuKeyboard(callback, excelHelperBot, localDate);
        } else {
            inlineKeyboardSender.switchYearMenu(callback, excelHelperBot, localDate);
        }
    }

    private void clearMenuForNextMenu(ExcelHelperBot excelHelperBot, CallbackQuery callback) {
        inlineKeyboardSender.clearMenu(callback, excelHelperBot);
        inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
    }

    private LocalDate getDateFromCallback(CallbackQuery callbackQuery) {
        String callbackMessage = callbackQuery.getData();
        if (callbackMessage.contains(MonthMenuKeyboard.CONTROL_MONTH)) {
            String[] date = callbackMessage.substring(MonthMenuKeyboard.CONTROL_MONTH.length()).split("-");
            return LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        if (
                callbackMessage.contains(YearMenuKeyboard.CONTROL_YEAR) ||
                        callbackMessage.contains(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY)
        ) {
            String year = callbackMessage.substring(callbackMessage.lastIndexOf("_") + 1);
            return LocalDate.of(Integer.parseInt(year), 1, 1);
        }
        return LocalDate.now();
    }
}
