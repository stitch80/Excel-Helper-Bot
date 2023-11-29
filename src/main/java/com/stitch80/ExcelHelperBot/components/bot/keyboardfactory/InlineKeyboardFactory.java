package com.stitch80.ExcelHelperBot.components.bot.keyboardfactory;

import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.MonthMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.QuarterCenturyMenuKeyboard;
import com.stitch80.ExcelHelperBot.components.bot.keyboards.inline.YearMenuKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;

@Component
public class InlineKeyboardFactory {

    private final MonthMenuKeyboard monthMenuKeyboard;
    private final YearMenuKeyboard yearMenuKeyboard;

    private final QuarterCenturyMenuKeyboard quarterCenturyMenuKeyboard;

    public InlineKeyboardFactory(MonthMenuKeyboard monthMenuKeyboard,
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
