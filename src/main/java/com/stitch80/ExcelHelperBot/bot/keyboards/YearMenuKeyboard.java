package com.stitch80.ExcelHelperBot.bot.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class YearMenuKeyboard {

    public static final String CONTROL_YEAR = "control_year_";

    public InlineKeyboardMarkup constructYearMenu(LocalDate localDate) {

        // Create inline keyboard builder
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboard = InlineKeyboardMarkup.builder();

        //Create buttons for the first row (current year and buttons)
        InlineKeyboardButton moveLeft = InlineKeyboardButton.builder()
                .text("⬅️ Previous")
                .callbackData(CONTROL_YEAR + localDate.minusYears(1))
                .build();
//        System.out.println(localDate.minusMonths(1));
        InlineKeyboardButton moveRight = InlineKeyboardButton.builder()
                .text("Next ➡️")
                .callbackData(CONTROL_YEAR + localDate.plusYears(1))
                .build();
//        System.out.println(localDate.plusMonths(1));
        InlineKeyboardButton currentYear = InlineKeyboardButton.builder()
                .text(String.valueOf(localDate.getYear()))
                .callbackData(MonthMenuKeyboard.CONTROL_MONTH + localDate.getYear())
                .build();

        //Add buttons to the first row
        List<InlineKeyboardButton> curYearRow = new ArrayList<>();
        curYearRow.add(moveLeft);
        curYearRow.add(currentYear);
        curYearRow.add(moveRight);

        //Add the first row to the keyboard
        inlineKeyboard.keyboardRow(curYearRow);

        //Create buttons for the months
        for (int i = 0; i < 3; i++) {
            List<InlineKeyboardButton> monthRow = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                InlineKeyboardButton monthButton = InlineKeyboardButton.builder()
                        .text(Month.of((i * 4) + (j + 1)).getDisplayName(TextStyle.SHORT, Locale.US))
                        .callbackData(MonthMenuKeyboard.CONTROL_MONTH +
                                LocalDate.of(localDate.getYear(), (i * 4) + (j + 1), 1))
                        .build();
                //Add button to the row
                monthRow.add(monthButton);
            }
            //Add row to the keyboard
            inlineKeyboard.keyboardRow(monthRow);
        }

        return inlineKeyboard.build();
    }
}
