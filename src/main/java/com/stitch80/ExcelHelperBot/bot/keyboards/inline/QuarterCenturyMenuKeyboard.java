package com.stitch80.ExcelHelperBot.bot.keyboards.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuarterCenturyMenuKeyboard {

    public static final String CONTROL_QUARTER_CENTURY = "control_quarter_century_";

    public InlineKeyboardMarkup constructQuarterCenturyMenu(LocalDate localDate) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboard = InlineKeyboardMarkup.builder();


        //Create buttons for the first row (current year and buttons)
        InlineKeyboardButton moveLeft = InlineKeyboardButton.builder()
                .text("⬅️ Previous")
                .callbackData(CONTROL_QUARTER_CENTURY + localDate.minusYears(25).getYear())
                .build();
//        System.out.println(localDate.minusMonths(1));
        InlineKeyboardButton moveRight = InlineKeyboardButton.builder()
                .text("Next ➡️")
                .callbackData(CONTROL_QUARTER_CENTURY + localDate.plusYears(25).getYear())
                .build();
//        System.out.println(localDate.plusMonths(1));
        int rangeStart = localDate.getYear() - (localDate.getYear() % 25) + 1;
        int rangeEnd = rangeStart + 25 - 1;
        InlineKeyboardButton currentYearRange = InlineKeyboardButton.builder()
                .text(rangeStart + " - " + rangeEnd)
                .callbackData(" ")
                .build();

        //Add buttons to the first row
        List<InlineKeyboardButton> curYearRangeRow = new ArrayList<>();
        curYearRangeRow.add(moveLeft);
        curYearRangeRow.add(currentYearRange);
        curYearRangeRow.add(moveRight);

        //Add the first row to the keyboard
        inlineKeyboard.keyboardRow(curYearRangeRow);

        for (int i = 0; i < 5; i++) {
            List<InlineKeyboardButton> yearRow = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                InlineKeyboardButton yearButton = InlineKeyboardButton.builder()
                        .text(String.valueOf(rangeStart + (i * 5) + j))
                        .callbackData(YearMenuKeyboard.CONTROL_YEAR + "start_" + (rangeStart + (i * 5) + j))
                        .build();
                yearRow.add(yearButton);
            }
            inlineKeyboard.keyboardRow(yearRow);
        }

        return inlineKeyboard.build();
    }
}
