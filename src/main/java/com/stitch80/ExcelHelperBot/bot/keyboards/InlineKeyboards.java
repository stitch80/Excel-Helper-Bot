package com.stitch80.ExcelHelperBot.bot.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
public class InlineKeyboards {

    private MonthMenuKeyboard monthMenuKeyboard;
    private YearMenuKeyboard yearMenuKeyboard;

    private QuarterCenturyMenuKeyboard quarterCenturyMenuKeyboard;

    public InlineKeyboards(MonthMenuKeyboard monthMenuKeyboard,
                           YearMenuKeyboard yearMenuKeyboard,
                           QuarterCenturyMenuKeyboard quarterCenturyMenuKeyboard) {
        this.monthMenuKeyboard = monthMenuKeyboard;
        this.yearMenuKeyboard = yearMenuKeyboard;
        this.quarterCenturyMenuKeyboard = quarterCenturyMenuKeyboard;
    }

    public InlineKeyboardMarkup constructMonthMenu(LocalDate localDate) {
        return monthMenuKeyboard.constructMonthMenu(localDate);
    }

    public InlineKeyboardMarkup constructYearMenu(LocalDate localDate) {
        return yearMenuKeyboard.constructYearMenu(localDate);
    }

    public InlineKeyboardMarkup constructQuarterCenturyMenu(LocalDate localDate) {
        return quarterCenturyMenuKeyboard.constructQuarterCenturyMenu(localDate);
    }
}
