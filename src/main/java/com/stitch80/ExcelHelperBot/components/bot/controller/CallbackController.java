package com.stitch80.ExcelHelperBot.components.bot.controller;

import com.stitch80.ExcelHelperBot.components.Invoices;
import com.stitch80.ExcelHelperBot.components.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.MonthMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.QuarterCenturyMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.YearMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.components.bot.senders.ReplyKeyboardSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Component
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
            if (callbackMessage.contains("start_")) {
                inlineKeyboardSender.clearMenu(callback, excelHelperBot);
                inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
                inlineKeyboardSender.sendYearMenuKeyboard(callback, excelHelperBot, localDate);
            } else {
                inlineKeyboardSender.switchYearMenu(callback, excelHelperBot, localDate);
            }
        } else if (callbackMessage.contains(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY)) {
            if (callbackMessage.contains("start_")) {
                inlineKeyboardSender.clearMenu(callback, excelHelperBot);
                inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
                inlineKeyboardSender.sendYearRangeMenuKeyboard(callback, excelHelperBot, localDate);

            } else {
                inlineKeyboardSender.switchYearRangeMenu(callback, excelHelperBot, localDate);
            }
        } else {
            inlineKeyboardSender.clearMenu(callback, excelHelperBot);
            inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
            Long userId = user.getId();
            invoices.setInvDate(callbackMessage, userId);
            invoices.setYear(callbackMessage.substring(0, 4), userId);
            replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);


        }
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
