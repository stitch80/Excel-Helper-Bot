package com.stitch80.excelhelperbot.components.bot.keyboards.inline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MonthMenuKeyboard {

    public static final String CONTROL_MONTH = "control_month_";

    public InlineKeyboardMarkup constructMonthMenu(LocalDate localDate) {

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboard = InlineKeyboardMarkup.builder();

        InlineKeyboardButton moveLeft = InlineKeyboardButton.builder()
                .text("⬅️ Previous")
                .callbackData(CONTROL_MONTH + localDate.minusMonths(1))
                .build();
        InlineKeyboardButton moveRight = InlineKeyboardButton.builder()
                .text("Next ➡️")
                .callbackData(CONTROL_MONTH + localDate.plusMonths(1))
                .build();
        InlineKeyboardButton currentMonth = InlineKeyboardButton.builder()
                .text(localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US) +
                        " " + localDate.getYear())
                .callbackData(YearMenuKeyboard.CONTROL_YEAR + "start_" + localDate.getYear())
                .build();

        List<InlineKeyboardButton> curMonthRow = new ArrayList<>();
        curMonthRow.add(moveLeft);
        curMonthRow.add(currentMonth);
        curMonthRow.add(moveRight);

        List<InlineKeyboardButton> weekDaysRow = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            InlineKeyboardButton dayButton = InlineKeyboardButton.builder()
                    .text(DayOfWeek.of(i).getDisplayName(TextStyle.SHORT, Locale.US))
                    .callbackData(".")
                    .build();
            weekDaysRow.add(dayButton);
        }

        inlineKeyboard.keyboardRow(curMonthRow)
                .keyboardRow(weekDaysRow);

        LocalDate firstDayOfMonth = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        int lastDayOfMonth = localDate.getMonth().maxLength();
        int firstDayOfMonthWeekDayNumber = firstDayOfMonth.getDayOfWeek().getValue() - 1;
        int numberOfWeeks = (int) Math.ceil((lastDayOfMonth - (7 - firstDayOfMonthWeekDayNumber)) / 7.0) + 1;

        for (int i = 0; i < numberOfWeeks; i++) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                int dayOfMonth = j + (7 * i) - (firstDayOfMonthWeekDayNumber - 1);
                String text = dayOfMonth > 0 && dayOfMonth <= lastDayOfMonth
                        ? String.valueOf(dayOfMonth) : " ";
                String callback = dayOfMonth > 0 && dayOfMonth <= lastDayOfMonth
                        ? LocalDate.of(localDate.getYear(), localDate.getMonth(), dayOfMonth).toString() : ".";
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData(callback)
                        .build();
                keyboardRow.add(button);
            }
            inlineKeyboard.keyboardRow(keyboardRow);
        }

        return inlineKeyboard.build();
    }
}
