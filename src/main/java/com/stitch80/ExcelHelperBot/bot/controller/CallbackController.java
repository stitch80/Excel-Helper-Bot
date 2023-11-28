package com.stitch80.ExcelHelperBot.bot.controller;

import com.stitch80.ExcelHelperBot.bot.ExcelHelperBot;
import com.stitch80.ExcelHelperBot.bot.keyboards.inline.MonthMenuKeyboard;
import com.stitch80.ExcelHelperBot.bot.keyboards.inline.QuarterCenturyMenuKeyboard;
import com.stitch80.ExcelHelperBot.bot.keyboards.inline.YearMenuKeyboard;
import com.stitch80.ExcelHelperBot.bot.senders.InlineKeyboardSender;
import com.stitch80.ExcelHelperBot.bot.senders.ReplyKeyboardSender;
import com.stitch80.ExcelHelperBot.dto.InvoiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

@Component
@Slf4j
public class CallbackController {

    InlineKeyboardSender inlineKeyboardSender;
    ReplyKeyboardSender replyKeyboardSender;
    private InvoiceDTO invoiceDTO;

    public CallbackController(InlineKeyboardSender inlineKeyboardSender,
                              ReplyKeyboardSender replyKeyboardSender,
                              InvoiceDTO invoiceDTO) {
        this.inlineKeyboardSender = inlineKeyboardSender;
        this.replyKeyboardSender = replyKeyboardSender;
        this.invoiceDTO = invoiceDTO;
    }

    public void processCallBack(Update update, ExcelHelperBot excelHelperBot) {
        CallbackQuery callback = update.getCallbackQuery();
        User user = callback.getFrom();
        String message = callback.getData();

        LocalDate localDate;

        if (callback.getData().contains(MonthMenuKeyboard.CONTROL_MONTH)) {
            localDate = getDateFromCallback(callback);
            System.out.println(callback.getData() + " " + callback.getMessage().getMessageId());
            inlineKeyboardSender.switchMonthMenu(callback, excelHelperBot, localDate);
        } else if (callback.getData().contains(YearMenuKeyboard.CONTROL_YEAR)) {
            localDate = getDateFromCallback(callback);
            if (callback.getData().contains("start_")) {
                inlineKeyboardSender.clearMenu(callback, excelHelperBot);
                inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
                inlineKeyboardSender.sendYearMenuKeyboard(user, excelHelperBot, localDate);
            } else {
                inlineKeyboardSender.switchYearMenu(callback, excelHelperBot, localDate);
            }
        } else if (callback.getData().contains(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY)) {
            localDate = getDateFromCallback(callback);
            if (callback.getData().contains("start_")) {
                inlineKeyboardSender.clearMenu(callback, excelHelperBot);
                inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
                inlineKeyboardSender.sendYearRangeMenuKeyboard(user, excelHelperBot, localDate);

            } else {
                inlineKeyboardSender.switchYearRangeMenu(callback, excelHelperBot, localDate);
            }
        } else {
            System.out.println(callback.getData() + " " + callback.getMessage().getMessageId());
            inlineKeyboardSender.clearMenu(callback, excelHelperBot);
            inlineKeyboardSender.deleteMessage(callback, excelHelperBot);
            invoiceDTO.setInvDate(message);
            invoiceDTO.setYear(message.substring(0, 4));
            replyKeyboardSender.sendInvoiceStatusAndInvoiceMenu(user, excelHelperBot);


        }
    }

    private LocalDate getDateFromCallback(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().contains(MonthMenuKeyboard.CONTROL_MONTH)) {
            String[] date = callbackQuery.getData().substring(MonthMenuKeyboard.CONTROL_MONTH.length()).split("-");
            return LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        }
        if (callbackQuery.getData().contains(YearMenuKeyboard.CONTROL_YEAR)) {
            String year = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
            return LocalDate.of(Integer.parseInt(year), 1, 1);
        }
        if (callbackQuery.getData().contains(QuarterCenturyMenuKeyboard.CONTROL_QUARTER_CENTURY)) {
            String yearRange = callbackQuery.getData().substring(callbackQuery.getData().lastIndexOf("_") + 1);
            return LocalDate.of(Integer.parseInt(yearRange), 1, 1);
        }
        return LocalDate.now();
    }
}
